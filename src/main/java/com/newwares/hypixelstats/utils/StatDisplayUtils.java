package com.newwares.hypixelstats.utils;

import com.newwares.hypixelstats.api.modes.Player;

import java.util.ArrayList;

public class StatDisplayUtils {
    public static void printStats(ArrayList<Player> players) {
        StringBuilder statString = new StringBuilder();
        for (Player player : players) {
            if (!player.isBot()) {
                if (player.isNicked()) {
                    statString.append(ChatColour.RED.getColourCode()).append("[NICKED]").append(player.getUsername());
                } else {
                    statString.append("[").append(player.getLevel()).append(ChatColour.RESET.getColourCode()).append("] ").append(player.getRankColour().getColourCode()).append(player.getUsername()).append(ChatColour.RESET.getColourCode()).append(" WS: ").append(player.getWs()).append(ChatColour.RESET.getColourCode()).append(" WLR: ").append(player.getWlr()).append(ChatColour.RESET.getColourCode()).append(" KDR: ").append(player.getKdr()).append(ChatColour.RESET.getColourCode()).append(" ").append(player.translate()).append("\n");
                }
            }
        }
        ChatUtils.print(statString.toString());
    }

    public static void printStats(Player player, boolean join) {
        if (!player.isBot()) {
            if (player.isNicked()) {
                ChatUtils.print(ChatColour.RED.getColourCode() + "[NICKED]" + player.getUsername());
            } else {
                StringBuilder statString = new StringBuilder();
                if (join) {
                    statString.append(ChatColour.BOLD.getColourCode()).append(ChatColour.GREEN.getColourCode()).append("+").append(ChatColour.RESET.getColourCode()).append("[").append(player.getLevel()).append(ChatColour.RESET.getColourCode()).append("] ").append(player.getRankColour().getColourCode()).append(player.getUsername()).append(ChatColour.RESET.getColourCode()).append(" WS: ").append(player.getWs()).append(ChatColour.RESET.getColourCode()).append(" WLR: ").append(player.getWlr()).append(ChatColour.RESET.getColourCode()).append(" KDR: ").append(player.getKdr()).append(ChatColour.RESET.getColourCode()).append(" ").append(player.translate()).append("\n");
                } else {
                    statString.append(ChatColour.BOLD.getColourCode()).append(ChatColour.RED.getColourCode()).append("-").append(ChatColour.RESET.getColourCode()).append("[").append(player.getLevel()).append(ChatColour.RESET.getColourCode()).append("] ").append(player.getRankColour().getColourCode()).append(player.getUsername()).append(ChatColour.RESET.getColourCode()).append(" WS: ").append(player.getWs()).append(ChatColour.RESET.getColourCode()).append(" WLR: ").append(player.getWlr()).append(ChatColour.RESET.getColourCode()).append(" KDR: ").append(player.getKdr()).append(ChatColour.RESET.getColourCode()).append(" ").append(player.translate()).append("\n");
                }
                ChatUtils.print(statString.toString());
            }
        }
    }
}
