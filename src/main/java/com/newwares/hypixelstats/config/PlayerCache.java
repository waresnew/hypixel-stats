package com.newwares.hypixelstats.config;

import com.google.gson.reflect.TypeToken;
import com.newwares.hypixelstats.hypixel.GameMode;
import com.newwares.hypixelstats.hypixel.Player;
import com.newwares.hypixelstats.utils.JsonUtils;
import org.apache.commons.io.FileUtils;
import org.nustaq.serialization.FSTConfiguration;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class PlayerCache {
    private static PlayerCache playerCache;
    private File cacheFile;

    private HashMap<String, HashMap<String, Player>> nameCache = new HashMap<>();
    private final FSTConfiguration fstConfig = FSTConfiguration.createDefaultConfiguration();


    private PlayerCache() {
    }

    public HashMap<String, HashMap<String, Player>> getCache() {
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
        cacheFile = new File(directory.getPath() + "\\HypixelStats\\cache.ser");
        if (!cacheFile.exists()) {
            cacheFile.createNewFile();
            byte[] bytes = fstConfig.asByteArray(nameCache);
            FileOutputStream fileOutputStream = new FileOutputStream(cacheFile);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        nameCache = (HashMap<String, HashMap<String, Player>>) fstConfig.asObject(FileUtils.readFileToByteArray(cacheFile));
    }

    public void updateCache(String uuid, Player player) throws IOException {
        nameCache.computeIfAbsent(uuid, k -> new HashMap<>());
        nameCache.get(uuid).put(player.getClass().getSimpleName(), player);
        byte[] bytes = fstConfig.asByteArray(nameCache);
        FileOutputStream fileOutputStream = new FileOutputStream(cacheFile);
        fileOutputStream.write(bytes);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    /**
     * adds a default player (stats are all 0)
     */
    public void updateCache(String uuid, String username, GameMode player) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        nameCache.computeIfAbsent(uuid, k -> new HashMap<>());
        nameCache.get(uuid).put(player.getClass().getSimpleName(), player.getType().getConstructor(String.class, String.class).newInstance(uuid, username));
        byte[] bytes = fstConfig.asByteArray(nameCache);
        FileOutputStream fileOutputStream = new FileOutputStream(cacheFile);
        fileOutputStream.write(bytes);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public void removePlayer(String uuid) {
        nameCache.remove(uuid);
        try {
            byte[] bytes = fstConfig.asByteArray(nameCache);
            FileOutputStream fileOutputStream = new FileOutputStream(cacheFile);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public <T extends Player> T getCache(String uuid, Class<T> type) throws IOException {
        if (nameCache.get(uuid) != null) {
            return type.cast(nameCache.get(uuid).get(type.getSimpleName()));
        } else {
            return null;
        }
    }
}
