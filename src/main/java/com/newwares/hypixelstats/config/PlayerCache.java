package com.newwares.hypixelstats.config;

import com.google.gson.reflect.TypeToken;
import com.newwares.hypixelstats.players.GameMode;
import com.newwares.hypixelstats.players.Player;
import com.newwares.hypixelstats.utils.JsonUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class PlayerCache {
    private static PlayerCache playerCache;
    private File cacheFile;

    private HashMap<String, HashMap<String, Player>> nameCache = new HashMap<>();


    private PlayerCache() {
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
        if (!new File(directory.getPath() + "\\HypixelStats\\cache.json").exists()) {
            new File(directory.getPath() + "\\HypixelStats\\cache.json").createNewFile();
            cacheFile = new File(directory.getPath() + "\\HypixelStats\\cache.json");
            FileWriter fileWriter = new FileWriter(cacheFile);
            fileWriter.write("{}");
            fileWriter.flush();
            fileWriter.close();
            FileReader fileReader = new FileReader(cacheFile);
            StringBuilder json = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                json.append(line);
            }
            Type type = new TypeToken<HashMap<String, HashMap<String, Object>>>() {
            }.getType();
            nameCache = JsonUtils.getGson().fromJson(json.toString(), type);
        }
        cacheFile = new File(directory.getPath() + "\\HypixelStats\\cache.json");
    }

    public void updateCache(String uuid, Player player) throws IOException {
        nameCache.computeIfAbsent(uuid, k -> new HashMap<>());
        nameCache.get(uuid).put(player.getClass().getSimpleName(), player);
        FileWriter fileWriter = new FileWriter(cacheFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        JsonUtils.getGson().toJson(nameCache, bufferedWriter);
        bufferedWriter.close();
    }

    /**
     * adds a default player (stats are all 0)
     */
    public void updateCache(String uuid, String username, GameMode player) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        nameCache.computeIfAbsent(uuid, k -> new HashMap<>());
        nameCache.get(uuid).put(player.getClass().getSimpleName(), player.getType().getConstructor(String.class, String.class).newInstance(uuid, username));
        FileWriter fileWriter = new FileWriter(cacheFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        JsonUtils.getGson().toJson(nameCache, bufferedWriter);
        bufferedWriter.close();
    }

    public void removePlayer(String uuid) throws IOException {
        nameCache.remove(uuid);
        FileWriter fileWriter = new FileWriter(cacheFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        JsonUtils.getGson().toJson(nameCache, bufferedWriter);
        bufferedWriter.close();
    }

    public <T extends Player> T getCache(String uuid, Class<T> type) {
        if (nameCache.get(uuid) != null) {
            Player player = nameCache.get(uuid).get(type.getSimpleName());
            if (player != null && System.currentTimeMillis() - player.getTimeCreated() >= 4 * 24 * 60 * 60 * 1000) {
                nameCache.remove(uuid);
                return null;
            }
            return type.cast(nameCache.get(uuid).get(type.getSimpleName()));
        } else {
            return null;
        }
    }
}
