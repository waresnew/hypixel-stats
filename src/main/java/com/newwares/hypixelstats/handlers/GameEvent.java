package com.newwares.hypixelstats.handlers;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.config.ModConfig;
import com.newwares.hypixelstats.config.NickCache;
import com.newwares.hypixelstats.hypixel.Player;
import com.newwares.hypixelstats.mixins.pseudo.DenickerInvoker;
import com.newwares.hypixelstats.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.TreeMap;

public class GameEvent {
    private static final HashSet<String> playerList = new HashSet<>();
    public static ArrayList<NetworkPlayerInfo> playerinfos = new ArrayList<>();
    private String mode;
    private String gametype;
    private boolean receivedLocraw = false;
    private World world;
    public static boolean someoneHas1Ping = false;


    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().theWorld != null) {
            if (world != Minecraft.getMinecraft().theWorld) {
                world = Minecraft.getMinecraft().theWorld;
                playerinfos.clear();
                playerList.clear();
                receivedLocraw = false;
                someoneHas1Ping = false;
            }

            if (receivedLocraw) {
                new Thread(() -> {
                    ArrayList<NetworkPlayerInfo> copy = new ArrayList<>(playerinfos);
                    for (NetworkPlayerInfo networkInfo : copy) {
                        if (!playerList.contains(networkInfo.getGameProfile().getId().toString().replace("-", ""))) {
                            if (Integer.parseInt(networkInfo.getGameProfile().getId().toString().replace("-", "").substring(12, 13)) == 1) {
                                playerList.add(networkInfo.getGameProfile().getId().toString().replace("-", ""));
                                if (someoneHas1Ping && networkInfo.getResponseTime() > 1) {
                                    continue;
                                }
                                try {
                                    String[] result = DenickerInvoker.denick(networkInfo.getGameProfile());
                                    if (result != null) {
                                        if ((gametype != null) && (gametype.equals("BEDWARS") || gametype.equals("SPEED_UHC") || gametype.equals("SKYWARS"))) {
                                            try {
                                                Player player = StatDisplayUtils.stat(gametype, mode, result[1], result[0]);
                                                if (!player.getRankColour().name().matches("GREEN|AQUA|GREY")) {
                                                    NickCache.getInstance().updateCache(networkInfo.getGameProfile().getName().replaceAll("§[0123456789abcdefklmnor]", ""), result[1]);
                                                    ChatUtils.print(ChatColour.RED + networkInfo.getGameProfile().getName() + " is nicked! (" + result[0] + ")");
                                                    StatDisplayUtils.printStats(player);
                                                } else {
                                                    retrieveNickCache(networkInfo);
                                                }
                                            } catch (IOException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    } else {
                                        retrieveNickCache(networkInfo);
                                    }
                                } catch (IllegalStateException ignored) {
                                    ChatUtils.print(ChatColour.RED + networkInfo.getGameProfile().getName() + " is nicked!");
                                }
                            }
                        }
                    }
                    for (NetworkPlayerInfo networkInfo : copy) {
                        if (!playerList.contains(networkInfo.getGameProfile().getId().toString().replace("-", ""))) {
                            playerList.add(networkInfo.getGameProfile().getId().toString().replace("-", ""));
                            if (someoneHas1Ping && networkInfo.getResponseTime() > 1) {
                                continue;
                            }
                            if ((gametype != null) && (gametype.equals("BEDWARS") || gametype.equals("SPEED_UHC") || gametype.equals("SKYWARS"))) {
                                try {
                                    StatDisplayUtils.printStats(StatDisplayUtils.stat(gametype, mode, networkInfo.getGameProfile().getId().toString(), networkInfo.getGameProfile().getName()));
                                } catch (IOException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                }).start();
            }
        }
    }

    private void retrieveNickCache(NetworkPlayerInfo networkInfo) {
        TreeMap<String, Long> map = NickCache.getInstance().sortByValue(NickCache.getInstance().getCache(networkInfo.getGameProfile().getName().replaceAll("§[0123456789abcdefklmnor]", "")));
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm");
        if (map != null && !map.isEmpty()) {
            ChatUtils.print(ChatColour.RED + networkInfo.getGameProfile().getName() + "is nicked! (" + MojangApi.uuidToUsername(map.firstKey()) + " " + format.format(new Date(map.firstEntry().getValue())) + ")");
        } else {
            ChatUtils.print(ChatColour.RED + networkInfo.getGameProfile().getName() + " is nicked!");
        }
    }

    @SubscribeEvent(receiveCanceled = true, priority = EventPriority.LOW)
    public void onClientChatReceived(ClientChatReceivedEvent event) {

        final JsonObject[] jsonObject = new JsonObject[1];
        String msg = event.message.getUnformattedText();
        if (msg.startsWith("{\"server\":")) {
            System.out.println(msg);
            if (!event.isCanceled()) {
                event.setCanceled(true);
            }
            new Thread(() -> {
                jsonObject[0] = JsonUtils.parseJson(msg).getAsJsonObject();
                boolean invalidMode = false;
                if (jsonObject[0].get("mode") == null) {
                    gametype = null;
                    mode = null;
                    invalidMode = true;
                } else if (jsonObject[0].get("gametype") == null) {
                    gametype = null;
                    mode = null;
                    invalidMode = true;
                } else if (jsonObject[0].get("map") == null) {
                    gametype = null;
                    mode = null;
                    invalidMode = true;
                }
                receivedLocraw = true;
                if (invalidMode) {
                    return;
                }
                mode = jsonObject[0].get("mode").getAsString();
                gametype = jsonObject[0].get("gametype").getAsString();
            }).start();
        } else if (msg.startsWith("Your new API key is")) {
            try {
                ModConfig.getInstance().setApiKey(msg.replace("Your new API key is ", ""));
            } catch (IOException e) {
                e.printStackTrace();
            }
            ChatUtils.print(ChatColour.GREEN + "HypixelStats found and set hypixel key");
        }
    }


}
