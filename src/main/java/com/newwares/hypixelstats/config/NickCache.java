package com.newwares.hypixelstats.config;

import com.google.gson.reflect.TypeToken;
import com.newwares.hypixelstats.utils.JsonUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class NickCache {
    private static NickCache instance;
    private ConcurrentHashMap<String, ConcurrentHashMap<String, Long>> nickCache = new ConcurrentHashMap<>();
    private File nickFile;

    private NickCache() {

    }

    public static NickCache getInstance() {
        if (instance == null) {
            instance = new NickCache();
        }
        return instance;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void init(File directory) throws IOException {
        if (!new File(directory.getPath() + "\\HypixelStats").exists()) {
            new File(directory.getPath() + "\\HypixelStats").mkdir();
        }
        nickFile = new File(directory.getPath() + "\\HypixelStats\\nicks.json");
        if (!nickFile.exists()) {
            nickFile.createNewFile();
            FileWriter fileWriter = new FileWriter(nickFile);
            fileWriter.write("{}");
            fileWriter.flush();
            fileWriter.close();
        }

        FileReader fileReader = new FileReader(nickFile);
        StringBuilder json = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            json.append(line);
        }
        Type type = new TypeToken<ConcurrentHashMap<String, ConcurrentHashMap<String, Long>>>() {
        }.getType();
        nickCache = JsonUtils.getGson().fromJson(json.toString(), type);
    }

    public void updateCache(String nick, String uuid) throws IOException {
        nickCache.computeIfAbsent(nick, k -> new ConcurrentHashMap<>());
        nickCache.get(nick).put(uuid, System.currentTimeMillis());
        nickCache.put(nick, nickCache.get(nick));
        FileWriter fileWriter = new FileWriter(nickFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        JsonUtils.getGson().toJson(nickCache, bufferedWriter);
        bufferedWriter.close();
    }

    public ConcurrentHashMap<String, Long> getCache(String nick) {
        for (String key : nickCache.keySet()) {
            if (key.equalsIgnoreCase(nick)) {
                return nickCache.get(key);
            }
        }
        return nickCache.get(nick);
    }

    public TreeMap<String, Long> sortByValue(ConcurrentHashMap<String, Long> map) {
        if (map == null) {
            return null;
        }
        Comparator<String> uuidDateComparator = Comparator.comparing(map::get);
        TreeMap<String, Long> sorted = new TreeMap<>(uuidDateComparator.reversed());
        sorted.putAll(map);
        return sorted;
    }
}
