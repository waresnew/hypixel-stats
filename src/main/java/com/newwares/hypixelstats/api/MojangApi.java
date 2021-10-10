package com.newwares.hypixelstats.api;

import com.newwares.hypixelstats.utils.JsonUtils;

import java.net.URL;

public class MojangApi {
    public static String usernameToUuid(String username) {
        try {
            return JsonUtils.parseJson(new URL(String.format("https://api.ashcon.app/mojang/v2/user/%s", username))).getAsJsonObject().get("uuid").getAsString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String uuidToUsername(String uuid) {
        try {
            return JsonUtils.parseJson(new URL(String.format("https://api.ashcon.app/mojang/v2/user/%s", uuid))).getAsJsonObject().get("username").getAsString();
        } catch (Exception e) {
            return null;
        }
    }
}
