package com.newwares.hypixelstats.config;

import com.google.gson.reflect.TypeToken;
import com.newwares.hypixelstats.utils.JsonUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

public class NickCache {
    private static NickCache instance;
    private HashMap<String, TreeMap<String, Long>> nickCache = new HashMap<>();
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
        if (!new File(directory.getPath() + "\\HypixelStats\\nicks.json").exists()) {
            new File(directory.getPath() + "\\HypixelStats\\nicks.json").createNewFile();
            nickFile = new File(directory.getPath() + "\\HypixelStats\\nicks.json");
            FileWriter fileWriter = new FileWriter(nickFile);
            fileWriter.write("{}");
            fileWriter.flush();
            fileWriter.close();
            FileReader fileReader = new FileReader(nickFile);
            StringBuilder json = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                json.append(line);
            }
            Type type = new TypeToken<HashMap<String, TreeMap<String, Long>>>() {
            }.getType();
            nickCache = JsonUtils.getGson().fromJson(json.toString(), type);
        }
        nickFile = new File(directory.getPath() + "\\HypixelStats\\nicks.json");
    }

    public void updateCache(String nick, String uuid) throws IOException {
        nickCache.computeIfAbsent(nick, k -> new TreeMap<>());
        nickCache.get(nick).put(uuid, System.currentTimeMillis());
        nickCache.put(nick, sortByValue(nickCache.get(nick)));
        FileWriter fileWriter = new FileWriter(nickFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        JsonUtils.getGson().toJson(nickCache, bufferedWriter);
        bufferedWriter.close();
    }

    public TreeMap<String, Long> getCache(String nick) {
        nickCache.computeIfAbsent(nick, k -> new TreeMap<>());
        for (String key : nickCache.keySet()) {
            if (key.equalsIgnoreCase(nick)) {
                return nickCache.get(key);
            }
        }
        return nickCache.get(nick);
    }

    private TreeMap<String, Long> sortByValue(TreeMap<String, Long> treeMap) {
        Comparator<String> uuidDateComparator = Comparator.comparing(treeMap::get);
        TreeMap<String, Long> sorted = new TreeMap<>(uuidDateComparator.reversed());
        sorted.putAll(treeMap);
        return sorted;
    }
}
