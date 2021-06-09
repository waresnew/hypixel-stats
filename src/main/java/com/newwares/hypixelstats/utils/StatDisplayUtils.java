package com.newwares.hypixelstats.utils;

import com.newwares.hypixelstats.api.modes.Player;

import java.util.ArrayList;

public class StatDisplayUtils {
    public static void printStats(ArrayList<Player> players) {
        StringBuilder statString = new StringBuilder();
        int i = 0;
        for (Player player : players) {
            if (!player.isBot()) {
                if (player.isNicked()) {
                    statString = new StringBuilder(ChatColour.RED + "[NICKED]" + player.getUsername());
                } else {
                    i += 1;
                    if (players.size() < i) {
                        statString.append("[").append(player.getLevel()).append("] ").append(player.getRankColour()).append(player.getUsername()).append(ChatColour.RESET).append("WS: ").append(player.getWs()).append(" WLR: ").append(player.getWlr()).append(" KDR: ").append(player.getKdr()).append(" ").append(player.translate());
                        statString.append("\n");
                    } else {
                        statString.append("[").append(player.getLevel()).append("] ").append(player.getRankColour()).append(player.getUsername()).append(ChatColour.RESET).append("WS: ").append(player.getWs()).append(" WLR: ").append(player.getWlr()).append(" KDR: ").append(player.getKdr()).append(" ").append(player.translate());
                    }
                }
            }
            ChatUtils.print(statString.toString());
        }
    }

    public static void printStats(Player player, boolean join) {
        if (!player.isBot()) {
            if (player.isNicked()) {
                ChatUtils.print(ChatColour.RED + "[NICKED]" + player.getUsername());
            } else {
                if (join) {
                    ChatUtils.print(String.valueOf(ChatColour.BOLD) + ChatColour.GREEN + "+" + ChatColour.RESET + "[" + player.getLevel() + "] " + player.getRankColour() + player.getUsername() + ChatColour.RESET + "WS: " + player.getWs() + " WLR: " + player.getWlr() + " KDR: " + player.getKdr() + " " + player.translate());
                } else {
                    ChatUtils.print(String.valueOf(ChatColour.BOLD) + ChatColour.RED + "-" + ChatColour.RESET + "[" + player.getLevel() + "] " + player.getRankColour() + player.getUsername() + ChatColour.RESET + "WS: " + player.getWs() + " WLR: " + player.getWlr() + " KDR: " + player.getKdr() + " " + player.translate());
                }
            }
        }
    }
}
