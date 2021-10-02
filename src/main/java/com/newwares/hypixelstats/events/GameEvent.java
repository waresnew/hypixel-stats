package com.newwares.hypixelstats.events;

import com.newwares.hypixelstats.utils.ChatColour;
import com.newwares.hypixelstats.utils.ChatUtils;
import com.newwares.hypixelstats.utils.ScoreboardUtils;
import com.newwares.hypixelstats.utils.StatDisplayUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameEvent {
    private World world;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START || Minecraft.getMinecraft().theWorld == null) {
            return;
        }
        if (world != Minecraft.getMinecraft().theWorld) {
            this.world = Minecraft.getMinecraft().theWorld;
            new Thread(() -> {
                String gametype;
                String mode = "";
                try {
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
                    Collection<NetworkPlayerInfo> players = Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap();
                    ArrayList<NetworkPlayerInfo> copy = new ArrayList<>(players);
                    for (NetworkPlayerInfo playerInfo : copy) {
                        if (Integer.parseInt(playerInfo.getGameProfile().getId().toString().replace("-", "").substring(12, 13)) == 1) {
                            ChatUtils.print(ChatColour.RED.getColourCode() + playerInfo.getGameProfile().getName() + " is nicked!");
                        } else {
                            if (!ChatReceivedEvent.playerList.contains(playerInfo.getGameProfile().getId().toString())) {
                                StatDisplayUtils.stat(gametype, mode, playerInfo.getGameProfile().getId().toString(), playerInfo.getGameProfile().getName(), true);
                                ChatReceivedEvent.playerList.add(playerInfo.getGameProfile().getId().toString());
                            }
                        }

                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            ChatReceivedEvent.clearPlayerList();
        }
    }

}
