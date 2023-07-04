package com.newwares.hypixelstats.utils;

import java.net.URL;

public class MojangApi {
    public static String usernameToUuid(String username) {
        try {
            return JsonUtils.parseJson(new URL(String.format("https://api.mojang.com/users/profiles/minecraft/%s", username))).getAsJsonObject().get("id").getAsString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String uuidToUsername(String uuid) {
        try {
            return JsonUtils.parseJson(new URL(String.format("https://api.mojang.com/user/profile/%s", uuid))).getAsJsonObject().get("name").getAsString();
        } catch (Exception e) {
            return null;
        }
    }
}
