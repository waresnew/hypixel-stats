package com.newwares.hypixelstats.handlers;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.config.ConfigData;
import com.newwares.hypixelstats.utils.ChatColour;
import com.newwares.hypixelstats.utils.ChatUtils;
import com.newwares.hypixelstats.utils.JsonUtils;
import com.newwares.hypixelstats.utils.StatDisplayUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class GameEvent {
    private static final ArrayList<String> playerList = new ArrayList<>();
    public static HashMap<UUID, NetworkPlayerInfo> playerInfoMap = new HashMap<>();
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
                receivedLocraw = false;
            }

            if (receivedLocraw) {
                new Thread(() -> {
                    try {
                        if ((gametype != null) && (gametype.equals("BEDWARS") || gametype.equals("SPEED_UHC") || gametype.equals("SKYWARS"))) {
                            HashSet<NetworkPlayerInfo> values = new HashSet<>(playerInfoMap.values());
                            for (NetworkPlayerInfo playerInfo : values) {
                                if (!playerList.contains(playerInfo.getGameProfile().getId().toString())) {
                                    playerList.add(playerInfo.getGameProfile().getId().toString());
                                    if (Integer.parseInt(playerInfo.getGameProfile().getId().toString().replace("-", "").substring(12, 13)) == 1) {
                                        ChatUtils.print(playerInfo.getGameProfile().getId().toString().replace("-", ""));
                                        ChatUtils.print(ChatColour.RED + playerInfo.getGameProfile().getName() + " is nicked!");
                                        continue;
                                    }
                                    StatDisplayUtils.stat(gametype, mode, playerInfo.getGameProfile().getId().toString(), playerInfo.getGameProfile().getName(), true);

                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    @SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGH)
    public void onClientChatReceived(ClientChatReceivedEvent event) {

        final JsonObject[] jsonObject = new JsonObject[1];
        String msg = event.message.getUnformattedText();
        if (msg.startsWith("{\"server\":")) {
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