package com.newwares.hypixelstats.utils;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.newwares.hypixelstats.config.ModConfig;
import com.newwares.hypixelstats.hypixel.GameMode;
import com.newwares.hypixelstats.hypixel.Player;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class JsonUtils {
    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            RuntimeTypeAdapterFactory<Player> adapter = RuntimeTypeAdapterFactory.of(Player.class, "mode", true);
            for (GameMode mode : GameMode.values()) {
                adapter.registerSubtype(mode.getType(), mode.name());
            }
            gson = new GsonBuilder().disableHtmlEscaping().registerTypeAdapterFactory(adapter).create();
        }
        return gson;
    }

    public static JsonElement parseJson(URL url) throws IOException {
        return parseJson(url, false);
    }
    public static JsonElement parseJson(URL url,boolean useApiKey ) throws IOException {
        JsonElement jsonElement;
        JsonParser parser = new JsonParser();
        URLConnection connection = url.openConnection();
        if (useApiKey) {
            connection.setRequestProperty("Api-Key", ModConfig.getInstance().getApiKey());
        }
        InputStream inputStream = connection.getInputStream();
        JsonElement result = parser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        inputStream.close();
        jsonElement = result;

        return jsonElement;
    }

    public static JsonElement parseJson(File file) throws IOException {
        JsonParser parser = new JsonParser();
        FileReader fileReader = new FileReader(file);
        StringBuilder json = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            json.append(line);
        }
        JsonElement result = parser.parse(json.toString());
        bufferedReader.close();
        return result;
    }

    public static JsonElement parseJson(String jsonString) {
        JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
        jsonReader.setLenient(true);
        return new JsonParser().parse(jsonReader);
    }

    public static void writeJson(JsonObject jsonObject, File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        getGson().toJson(jsonObject, bufferedWriter);
        bufferedWriter.close();
    }
}
