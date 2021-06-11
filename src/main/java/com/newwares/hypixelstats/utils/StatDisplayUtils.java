package com.newwares.hypixelstats.utils;

import com.newwares.hypixelstats.api.modes.Player;

public class StatDisplayUtils {

    public static void printStats(Player player, boolean join) {
        if (!player.isBot()) {
            if (player.isNicked()) {
                ChatUtils.print(ChatColour.RED.getColourCode() + "[NICKED]" + player.getUsername());
            } else {
                StringBuilder statString = new StringBuilder();
                if (join) {
                    System.out.println(player.getRankColour().getColourCode());
                    statString.append(ChatColour.BOLD.getColourCode()).append(ChatColour.GREEN.getColourCode()).append("+").append(ChatColour.RESET.getColourCode()).append(" ").append(player.translate());
                } else {
                    statString.append(ChatColour.BOLD.getColourCode()).append(ChatColour.RED.getColourCode()).append("-").append(ChatColour.RESET.getColourCode()).append(" ").append(player.translate());
                }
                ChatUtils.print(statString.toString());
            }
        }
    }
}
