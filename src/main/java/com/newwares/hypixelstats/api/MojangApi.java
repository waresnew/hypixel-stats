package com.newwares.hypixelstats.api;

import com.newwares.hypixelstats.utils.JsonUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MojangApi {
    public static String usernameToUuid(String username) throws IOException, InterruptedException {
       return JsonUtils.parseJson(new URL(String.format("https://api.mojang.com/users/profiles/minecraft/%s", username))).getAsJsonObject().get("id").getAsString();
    }
}
