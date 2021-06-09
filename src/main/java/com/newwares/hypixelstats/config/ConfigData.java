package com.newwares.hypixelstats.config;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.utils.JsonUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigData {
    private static ConfigData configData;
    private String apiKey;
    private File configFile;
    JsonObject jsonObject;
    private boolean enabledSpeedUhc;
    private boolean enabledNormalSkywars;
    private boolean enabledRankedSkywars;
    private boolean enabledBedwars;

    public static ConfigData getInstance() {
        if (configData == null) {
            configData = new ConfigData();
        }
        return configData;
    }

    private ConfigData() {

    }

    public void init(File directory) throws IOException {
        if (!new File(directory.getPath() + "\\HypixelStats").exists()) {
            new File(directory.getPath() + "\\HypixelStats").mkdir();
        }
        if (!new File(directory.getPath() + "\\HypixelStats\\config.json").exists()) {
            new File(directory.getPath() + "\\HypixelStats\\config.json").createNewFile();
            configFile = new File(directory.getPath() + "\\HypixelStats\\config.json");
            FileWriter fileWriter = new FileWriter(configFile);
            fileWriter.write("{}");
            fileWriter.flush();
            fileWriter.close();
            jsonObject = JsonUtils.parseJson(configFile).getAsJsonObject();
        }
        configFile = new File(directory.getPath() + "\\HypixelStats\\config.json");
        jsonObject = JsonUtils.parseJson(configFile).getAsJsonObject();
        if (jsonObject.get("SpeedUHC") == null) {
            setEnabledSpeedUhc(true);
        }
        if (jsonObject.get("NormalSkywars") == null) {
            setEnabledNormalSkywars(true);
        }
        if (jsonObject.get("RankedSkywars") == null) {
            setEnabledRankedSkywars(true);
        }
        if (jsonObject.get("Bedwars") == null) {
            setEnabledBedwars(true);
        }
        if (apiKey == null || apiKey.equals("")) {
            setApiKey("defaultKey");
        }
    }


    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) throws IOException {
        this.apiKey = apiKey;
        jsonObject.addProperty("ApiKey", apiKey);
        JsonUtils.writeJson(jsonObject, configFile);
    }


    public boolean isEnabledSpeedUhc() {
        return enabledSpeedUhc;
    }

    public void setEnabledSpeedUhc(boolean enabledSpeedUhc) throws IOException {
        this.enabledSpeedUhc = enabledSpeedUhc;
        jsonObject.addProperty("SpeedUHC", enabledSpeedUhc);
        JsonUtils.writeJson(jsonObject, configFile);
    }

    public boolean isEnabledNormalSkywars() {
        return enabledNormalSkywars;
    }

    public void setEnabledNormalSkywars(boolean enabledNormalSkywars) throws IOException {
        this.enabledNormalSkywars = enabledNormalSkywars;
        jsonObject.addProperty("NormalSkywars", enabledSpeedUhc);
        JsonUtils.writeJson(jsonObject, configFile);
    }

    public boolean isEnabledRankedSkywars() {
        return enabledRankedSkywars;
    }

    public void setEnabledRankedSkywars(boolean enabledRankedSkywars) throws IOException {
        this.enabledRankedSkywars = enabledRankedSkywars;
        jsonObject.addProperty("RankedSkywars", enabledSpeedUhc);
        JsonUtils.writeJson(jsonObject, configFile);
    }

    public boolean isEnabledBedwars() {
        return enabledBedwars;
    }

    public void setEnabledBedwars(boolean enabledBedwars) throws IOException {
        this.enabledBedwars = enabledBedwars;
        jsonObject.addProperty("Bedwars", enabledSpeedUhc);
        JsonUtils.writeJson(jsonObject, configFile);
    }
}
