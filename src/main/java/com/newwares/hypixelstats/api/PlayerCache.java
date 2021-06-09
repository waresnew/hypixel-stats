package com.newwares.hypixelstats.api;

import com.newwares.hypixelstats.api.modes.*;

import java.util.HashMap;

public class PlayerCache {
    private static PlayerCache playerCache;
    private final HashMap<String, BedwarsPlayer> bedwarsPlayers = new HashMap<>();
    private final HashMap<String, NormalSkywarsPlayer> normalSkywarsPlayers = new HashMap<>();
    private final HashMap<String, InsaneSkywarsPlayer> insaneSkywarsPlayers = new HashMap<>();
    private final HashMap<String, RankedSkywarsPlayer> rankedSkywarsPlayers = new HashMap<>();
    private final HashMap<String, SpeedUHCPlayer> speedUHCPlayers = new HashMap<>();

    public static PlayerCache getInstance() {
        if (playerCache == null) {
            playerCache = new PlayerCache();
        }
        return playerCache;
    }

    public HashMap<String, BedwarsPlayer> getBedwarsPlayers() {
        return bedwarsPlayers;
    }

    public void updateBedwarsPlayers(String uuid, BedwarsPlayer player) {
        bedwarsPlayers.put(uuid, player);
    }

    public HashMap<String, NormalSkywarsPlayer> getNormalSkywarsPlayers() {
        return normalSkywarsPlayers;
    }

    public void updateNormalSkywarsPlayers(String uuid, NormalSkywarsPlayer player) {
        normalSkywarsPlayers.put(uuid, player);
    }

    public HashMap<String, RankedSkywarsPlayer> getRankedSkywarsPlayers() {
        return rankedSkywarsPlayers;
    }

    public void updateRankedSkywarsPlayers(String uuid, RankedSkywarsPlayer player) {
        rankedSkywarsPlayers.put(uuid, player);
    }

    public HashMap<String, SpeedUHCPlayer> getSpeedUHCPlayers() {
        return speedUHCPlayers;
    }

    public void updateSpeedUHCPlayers(String uuid, SpeedUHCPlayer player) {
        speedUHCPlayers.put(uuid, player);
    }

    public HashMap<String, InsaneSkywarsPlayer> getInsaneSkywarsPlayers() {
        return insaneSkywarsPlayers;
    }

    public void updateInsaneSkywarsPlayers(String uuid, InsaneSkywarsPlayer player) {
        insaneSkywarsPlayers.put(uuid, player);
    }
}
