package com.newwares.hypixelstats.events;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.api.MojangApi;
import com.newwares.hypixelstats.config.ConfigData;
import com.newwares.hypixelstats.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChatReceivedEvent {
    String mode;
    String gametype;
    public static final ArrayList<String> playerList = new ArrayList<>();

    public static void clearPlayerList() {
        playerList.clear();
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        gametype = ScoreboardUtils.getBoardTitle().replaceAll("§[0123456789abcdefklmnor]", "");
        List<String> sidebarScores = ScoreboardUtils.getSidebarLines();
        for (String sidebarScore : sidebarScores) {
            if (sidebarScore.matches("Players: .+/(12|24).+")) {
                for (String sidebarScore2 : sidebarScores) {
                    if (sidebarScore2.contains("§cInsane")) {
                        mode = "solo_insane";
                    } else if (sidebarScore2.contains("§aNormal")) {
                        mode = "solo_normal";
                    }
                }
            } else if (sidebarScore.matches("Players: .+/4.+")) {
                mode = "ranked_normal";
            }
        }
        String msg = event.message.getUnformattedText();
        if (msg.startsWith("Your new API key is")) {
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
                            if (!playerList.contains(uuid)) {
                                playerList.add(uuid);
                                StatDisplayUtils.stat(gametype, mode, uuid, username, false);

                            }
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
                            if (!playerList.contains(uuid)) {
                                playerList.add(uuid);
                                StatDisplayUtils.stat(gametype, mode, uuid, username, true);

                            }
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
