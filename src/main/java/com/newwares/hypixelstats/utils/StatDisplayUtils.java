package com.newwares.hypixelstats.utils;

import com.newwares.hypixelstats.api.modes.Player;

import java.util.ArrayList;

public class StatDisplayUtils {

    public static void printStats(ArrayList<Player> players) {
        for (Player player : players) {
            if (player.isNicked()) {

            }
            String genericStatString = "[" + player.getLevel() + "]" + " " +
        }
    }

    public static void printStats(Player player) {

    }
}
