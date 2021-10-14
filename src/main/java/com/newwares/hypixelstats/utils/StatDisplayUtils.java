package com.newwares.hypixelstats.utils;

import com.newwares.hypixelstats.api.modes.GameMode;
import com.newwares.hypixelstats.api.modes.Player;
import com.newwares.hypixelstats.config.ConfigData;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class StatDisplayUtils {
    public static void stat(String gametype, String mode, String uuid, String username) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        switch (gametype) {
            case "BEDWARS": {
                if (ConfigData.getInstance().isEnabledBedwars()) {
                    StatDisplayUtils.printStats(HypixelApi.setStats(GameMode.valueOf("BEDWARS"), uuid.replace("-", ""), username));
                }
                break;
            }

            case "SPEED_UHC": {
                if (ConfigData.getInstance().isEnabledSpeedUhc()) {
                    StatDisplayUtils.printStats(HypixelApi.setStats(GameMode.valueOf("SPEED_UHC"), uuid.replace("-", ""), username));
                }
                break;
            }

            case "SKYWARS": {
                switch (mode) {
                    case "ranked_normal": {
                        if (ConfigData.getInstance().isEnabledRankedSkywars()) {
                            StatDisplayUtils.printStats(HypixelApi.setStats(GameMode.valueOf("RANKED_SKYWARS"), uuid.replace("-", ""), username));
                        }
                        break;
                    }
                    case "teams_normal":
                    case "solo_normal": {
                        if (ConfigData.getInstance().isEnabledNormalSkywars()) {
                            StatDisplayUtils.printStats(HypixelApi.setStats(GameMode.valueOf("NORMAL_SKYWARS"), uuid.replace("-", ""), username));
                        }
                        break;
                    }
                    case "teams_insane":
                    case "solo_insane": {
                        if (ConfigData.getInstance().isEnabledInsaneSkywars()) {
                            StatDisplayUtils.printStats(HypixelApi.setStats(GameMode.valueOf("INSANE_SKYWARS"), uuid.replace("-", ""), username));
                        }
                        break;
                    }
                }
                break;
            }
        }
    }

    public static void printStats(Player player) {
        if (player == null) {
            return;
        }
        ChatUtils.print(String.valueOf(ChatColour.BOLD) + ChatColour.GREEN + "+" + ChatColour.RESET + " " + player.translate());
    }
}
