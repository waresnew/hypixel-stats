package com.newwares.hypixelstats.utils;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JsonUtils {
    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().disableHtmlEscaping().create();
        }
        return gson;
    }


    public static JsonElement parseJson(URL url) throws IOException {
        JsonElement jsonElement;
        JsonParser parser = new JsonParser();
        InputStream inputStream = url.openStream();
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
