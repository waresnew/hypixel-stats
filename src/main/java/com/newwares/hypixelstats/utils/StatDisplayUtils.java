package com.newwares.hypixelstats.utils;

import com.newwares.hypixelstats.api.HypixelApi;
import com.newwares.hypixelstats.api.modes.Player;
import com.newwares.hypixelstats.config.ConfigData;

import java.io.IOException;
import java.util.ArrayList;

public class StatDisplayUtils {
    private static final ArrayList<String> playerList = new ArrayList<>();

    public static void clearPlayerList() {
        playerList.clear();
    }

    public static void stat(String gametype, String mode, String uuid, String username, boolean join) throws IOException, InterruptedException {
        if (!playerList.contains(uuid)) {
            switch (gametype) {
                case "BEDWARS": {
                    if (ConfigData.getInstance().isEnabledBedwars()) {
                        StatDisplayUtils.printStats(new HypixelApi("BEDWARS", uuid.replace("-", ""), username).setStats(), join);
                        playerList.add(uuid);
                    }
                    break;
                }

                case "SPEED_UHC": {
                    if (ConfigData.getInstance().isEnabledSpeedUhc()) {
                        StatDisplayUtils.printStats(new HypixelApi("SPEED_UHC", uuid.replace("-", ""), username).setStats(), join);
                        playerList.add(uuid);
                    }
                    break;
                }

                case "SKYWARS": {
                    switch (mode) {
                        case "ranked_normal": {
                            if (ConfigData.getInstance().isEnabledRankedSkywars()) {
                                StatDisplayUtils.printStats(new HypixelApi("RANKED_SKYWARS", uuid.replace("-", ""), username).setStats(), join);
                                playerList.add(uuid);
                            }
                            break;
                        }
                        case "teams_normal":
                        case "solo_normal": {
                            if (ConfigData.getInstance().isEnabledNormalSkywars()) {
                                StatDisplayUtils.printStats(new HypixelApi("NORMAL_SKYWARS", uuid.replace("-", ""), username).setStats(), join);
                                playerList.add(uuid);
                            }
                            break;
                        }
                        case "teams_insane":
                        case "solo_insane": {
                            if (ConfigData.getInstance().isEnabledInsaneSkywars()) {
                                StatDisplayUtils.printStats(new HypixelApi("INSANE_SKYWARS", uuid.replace("-", ""), username).setStats(), join);
                                playerList.add(uuid);
                            }
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    public static void printStats(Player player, boolean join) {
        if (!player.isBot()) {
            if (player.isNicked()) {
                ChatUtils.print(ChatColour.RED.getColourCode() + "[NICKED]" + player.getUsername());
            } else {
                StringBuilder statString = new StringBuilder();
                if (join) {
                    statString.append(ChatColour.BOLD.getColourCode()).append(ChatColour.GREEN.getColourCode()).append("+").append(ChatColour.RESET.getColourCode()).append(" ").append(player.translate());
                } else {
                    statString.append(ChatColour.BOLD.getColourCode()).append(ChatColour.RED.getColourCode()).append("-").append(ChatColour.RESET.getColourCode()).append(" ").append(player.translate());
                }
                ChatUtils.print(statString.toString());
            }
        }
    }
}
