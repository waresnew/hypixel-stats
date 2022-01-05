package com.newwares.hypixelstats.utils;

import com.newwares.hypixelstats.config.ModConfig;
import com.newwares.hypixelstats.hypixel.GameMode;
import com.newwares.hypixelstats.hypixel.Player;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class StatDisplayUtils {
    public static Player stat(String gametype, String mode, String uuid, String username) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        switch (gametype) {
            case "BEDWARS": {
                if (ModConfig.getInstance().isEnabledBedwars()) {
                    return HypixelApi.setStats(GameMode.valueOf("BEDWARS"), uuid.replace("-", ""), username);
                }
                break;
            }

            case "SPEED_UHC": {
                if (ModConfig.getInstance().isEnabledSpeedUhc()) {
                    return HypixelApi.setStats(GameMode.valueOf("SPEED_UHC"), uuid.replace("-", ""), username);
                }
                break;
            }

            case "SKYWARS": {
                switch (mode) {
                    case "ranked_normal": {
                        if (ModConfig.getInstance().isEnabledRankedSkywars()) {
                            return HypixelApi.setStats(GameMode.valueOf("RANKED_SKYWARS"), uuid.replace("-", ""), username);
                        }
                        break;
                    }
                    case "teams_normal":
                    case "solo_normal": {
                        if (ModConfig.getInstance().isEnabledNormalSkywars()) {
                            return HypixelApi.setStats(GameMode.valueOf("NORMAL_SKYWARS"), uuid.replace("-", ""), username);
                        }
                        break;
                    }
                    case "teams_insane":
                    case "solo_insane": {
                        if (ModConfig.getInstance().isEnabledInsaneSkywars()) {
                            return HypixelApi.setStats(GameMode.valueOf("INSANE_SKYWARS"), uuid.replace("-", ""), username);
                        }
                        break;
                    }
                }
                break;
            }
        }
        return null;
    }

    public static void printStats(Player player) {
        if (player == null) {
            return;
        }
        ChatUtils.print(String.valueOf(ChatColour.BOLD) + ChatColour.GREEN + "+" + ChatColour.RESET + " " + player.translate());
    }
}
