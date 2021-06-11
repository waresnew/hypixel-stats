package com.newwares.hypixelstats.api;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerCache {
    private static PlayerCache playerCache;

    private final ConcurrentHashMap<String, Object> nameCache = new ConcurrentHashMap<>();


    private PlayerCache() {
    }

    public static PlayerCache getInstance() {
        if (playerCache == null) {
            playerCache = new PlayerCache();
        }
        return playerCache;
    }

    public void updateCache(String uuid, Object player) {
        nameCache.put(uuid, player);
    }

    public <T> T getCache(String uuid, Class<T> type) {
        if (nameCache.containsKey(uuid)) {
            return type.cast(nameCache.get(uuid));
        } else {
            return null;
        }
    }
}
