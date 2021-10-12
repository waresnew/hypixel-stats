package com.newwares.hypixelstats.handlers;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.newwares.hypixelstats.config.ConfigData;
import com.newwares.hypixelstats.mixins.pseudo.DenickerInvoker;
import com.newwares.hypixelstats.utils.ChatColour;
import com.newwares.hypixelstats.utils.ChatUtils;
import com.newwares.hypixelstats.utils.JsonUtils;
import com.newwares.hypixelstats.utils.StatDisplayUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;
import java.util.ArrayList;

public class GameEvent {
    private static final ArrayList<String> playerList = new ArrayList<>();
    public static ArrayList<GameProfile> playerinfos = new ArrayList<>();
    private String mode;
    private String gametype;
    private boolean receivedLocraw = false;
    private World world;

    public static void clearPlayerList() {
        playerList.clear();
    }


    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().theWorld != null) {
            if (world != Minecraft.getMinecraft().theWorld) {
                world = Minecraft.getMinecraft().theWorld;
                playerinfos.clear();
                receivedLocraw = false;
            }

            if (receivedLocraw && Minecraft.getMinecraft().playerController.getCurrentGameType().getName().equals("adventure")) {
                new Thread(() -> {
                    ArrayList<GameProfile> copy = new ArrayList<>(playerinfos);
                    for (GameProfile gameProfile : copy) {
                        if (!playerList.contains(gameProfile.getId().toString().replace("-", ""))) {
                            if (Integer.parseInt(gameProfile.getId().toString().replace("-", "").substring(12, 13)) == 1) {
                                playerList.add(gameProfile.getId().toString().replace("-", ""));
                                try {
                                    String[] result = DenickerInvoker.denick(gameProfile);
                                    if (result != null) {
                                        ChatUtils.print(ChatColour.RED + gameProfile.getName() + " is nicked! (" + result[0] + ")");
                                        if ((gametype != null) && (gametype.equals("BEDWARS") || gametype.equals("SPEED_UHC") || gametype.equals("SKYWARS"))) {
                                            try {
                                                StatDisplayUtils.stat(gametype, mode, result[1], result[0], true);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    } else {
                                        ChatUtils.print(ChatColour.RED + gameProfile.getName() + " is nicked!");
                                    }
                                } catch (IllegalStateException ignored) {
                                    ChatUtils.print(ChatColour.RED + gameProfile.getName() + " is nicked!");
                                }
                            }
                        }
                    }
                    for (GameProfile gameProfile : copy) {
                        if (!playerList.contains(gameProfile.getId().toString().replace("-", ""))) {
                            playerList.add(gameProfile.getId().toString().replace("-", ""));
                            if ((gametype != null) && (gametype.equals("BEDWARS") || gametype.equals("SPEED_UHC") || gametype.equals("SKYWARS"))) {
                                try {
                                    StatDisplayUtils.stat(gametype, mode, gameProfile.getId().toString(), gameProfile.getName(), true);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                }).start();
            }
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
                ConfigData.getInstance().setApiKey(msg.replace("Your new API key is ", ""));
            } catch (IOException e) {
                e.printStackTrace();
            }
            ChatUtils.print(ChatColour.GREEN + "HypixelStats found and set api key");
        }
    }


}
