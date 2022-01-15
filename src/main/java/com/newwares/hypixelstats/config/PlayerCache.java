package com.newwares.hypixelstats.config;

import com.google.gson.reflect.TypeToken;
import com.newwares.hypixelstats.hypixel.GameMode;
import com.newwares.hypixelstats.hypixel.Player;
import com.newwares.hypixelstats.utils.JsonUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerCache {
    private static PlayerCache playerCache;
    private File cacheFile;

    private ConcurrentHashMap<String, ConcurrentHashMap<String, Player>> nameCache = new ConcurrentHashMap<>();

    private PlayerCache() {
    }

    public ConcurrentHashMap<String, ConcurrentHashMap<String, Player>> getCache() {
        return nameCache;
    }

    public static PlayerCache getInstance() {
        if (playerCache == null) {
            playerCache = new PlayerCache();
        }
        return playerCache;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void init(File directory) throws IOException {
        if (!new File(directory.getPath() + "\\HypixelStats").exists()) {
            new File(directory.getPath() + "\\HypixelStats").mkdir();
        }
        cacheFile = new File(directory.getPath() + "\\HypixelStats\\cache.json");
        if (!getCacheFile().exists()) {
            getCacheFile().createNewFile();
            FileWriter fileWriter = new FileWriter(getCacheFile());
            fileWriter.write("{}");
            fileWriter.flush();
            fileWriter.close();
        }
        FileReader fileReader = new FileReader(getCacheFile());
        StringBuilder json = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            json.append(line);
        }
        Type type = new TypeToken<ConcurrentHashMap<String, ConcurrentHashMap<String, Player>>>() {
        }.getType();
        nameCache = JsonUtils.getGson().fromJson(json.toString(), type);
    }

    public void updateCache(String uuid, Player player) throws IOException {
        nameCache.computeIfAbsent(uuid, k -> new ConcurrentHashMap<>());
        nameCache.get(uuid).put(player.getClass().getSimpleName(), player);
        FileWriter fileWriter = new FileWriter(getCacheFile());
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        ConcurrentHashMap<String, ConcurrentHashMap<String, Player>> copy = new ConcurrentHashMap<>(nameCache);
        JsonUtils.getGson().toJson(copy, bufferedWriter);
        bufferedWriter.close();
    }

    /**
     * adds a default player (stats are all 0)
     */
    public void updateCache(String uuid, String username, GameMode player) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        nameCache.computeIfAbsent(uuid, k -> new ConcurrentHashMap<>());
        updateCache(uuid, player.getType().getConstructor(String.class, String.class).newInstance(uuid, username));
    }

    public void removePlayer(String uuid) {
        nameCache.remove(uuid);
        try {
            FileWriter fileWriter = new FileWriter(getCacheFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            ConcurrentHashMap<String, ConcurrentHashMap<String, Player>> copy = new ConcurrentHashMap<>(nameCache);
            JsonUtils.getGson().toJson(copy, bufferedWriter);
            bufferedWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public <T extends Player> T getCache(String uuid, Class<T> type) {
        if (nameCache.get(uuid) != null) {
            return type.cast(nameCache.get(uuid).get(type.getSimpleName()));
        } else {
            return null;
        }
    }

    public File getCacheFile() {
        return cacheFile;
    }
}
