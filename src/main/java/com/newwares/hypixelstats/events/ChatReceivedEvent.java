package com.newwares.hypixelstats.events;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.api.MojangApi;
import com.newwares.hypixelstats.config.ConfigData;
import com.newwares.hypixelstats.utils.ChatColour;
import com.newwares.hypixelstats.utils.ChatUtils;
import com.newwares.hypixelstats.utils.JsonUtils;
import com.newwares.hypixelstats.utils.StatDisplayUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class ChatReceivedEvent {
    String mode;
    String gametype;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onClientChatReceived(ClientChatReceivedEvent event) {

        final JsonObject[] jsonObject = new JsonObject[1];
        String msg = event.message.getUnformattedText();
        if (msg.startsWith("{\"server\":")) {
            // event.setCanceled(true);
            new Thread(() -> {
                jsonObject[0] = JsonUtils.parseJson(msg).getAsJsonObject();
                if (jsonObject[0].get("mode") != null && jsonObject[0].get("map") != null) {
                    mode = jsonObject[0].get("mode").getAsString();
                    gametype = jsonObject[0].get("gametype").getAsString();
                    try {
                        Collection<NetworkPlayerInfo> players = Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap();
                        ArrayList<NetworkPlayerInfo> copy;
                        synchronized (players) {
                            copy = new ArrayList<>(players);
                        }
                        if (gametype.equals("BEDWARS") || gametype.equals("SPEED_UHC") || gametype.equals("SKYWARS")) {
                            for (NetworkPlayerInfo playerInfo : copy) {
                                if (Integer.parseInt(playerInfo.getGameProfile().getId().toString().replace("-", "").substring(12, 13)) == 1) {
                                    ChatUtils.print(ChatColour.RED.getColourCode() + playerInfo.getGameProfile().getName() + " is nicked!");
                                } else {
                                    StatDisplayUtils.stat(gametype, mode, playerInfo.getGameProfile().getId().toString(), playerInfo.getGameProfile().getName(), true);

                                }
                            }
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        } else if (msg.startsWith("Your new API key is")) {
            try {
                ConfigData.getInstance().setApiKey(msg.replace("Your new API key is ", ""));
            } catch (IOException e) {
                e.printStackTrace();
            }
            ChatUtils.print(ChatColour.GREEN.getColourCode() + "HypixelStats found and set api key");
        } else if (event.message.getFormattedText().contains("§r§e has quit!")) {
            new Thread(() -> {
                if (mode != null && gametype != null) {
                    try {
                        String username = event.message.getUnformattedText().substring(0, event.message.getUnformattedText().indexOf(" has quit"));
                        String uuid = MojangApi.usernameToUuid(username);
                        if (uuid != null) {
                            StatDisplayUtils.stat(gametype, mode, uuid, username, false);
                        } else {
                            ChatUtils.print(ChatColour.RED.getColourCode() + username + " is nicked!");
                        }

                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else if (event.message.getFormattedText().contains("§r§e has joined")) {
            new Thread(() -> {
                if (mode != null && gametype != null) {
                    try {
                        String username = event.message.getUnformattedText().substring(0, event.message.getUnformattedText().indexOf(" has joined"));
                        String uuid = MojangApi.usernameToUuid(username);
                        if (uuid != null) {
                            StatDisplayUtils.stat(gametype, mode, uuid, username, true);
                        } else {
                            ChatUtils.print(ChatColour.RED.getColourCode() + username + " is nicked!");
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }


}
