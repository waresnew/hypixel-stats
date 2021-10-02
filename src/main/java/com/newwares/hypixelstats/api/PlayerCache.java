package com.newwares.hypixelstats.api;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerCache {
    private static PlayerCache playerCache;

    private final HashMap<String, Object> nameCache = new HashMap<>();


    private PlayerCache() {
    }

    public static PlayerCache getInstance() {
        if (playerCache == null) {
            playerCache = new PlayerCache();
        }
        return playerCache;
    }

    public void updateCache(String uuid, Object player) {
        nameCache.put(uuid + player.getClass().toString(), player);
    }

    public <T> T getCache(String uuid, Class<T> type) {
        if (nameCache.containsKey(uuid + type.toString())) {
            return type.cast(nameCache.get(uuid + type));
        } else {
            return null;
        }
    }
}
