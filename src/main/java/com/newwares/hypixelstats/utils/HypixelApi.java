package com.newwares.hypixelstats.utils;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.hypixel.GameMode;
import com.newwares.hypixelstats.hypixel.Player;
import com.newwares.hypixelstats.hypixel.factories.PlayerFactory;
import com.newwares.hypixelstats.config.ModConfig;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

public class HypixelApi {


    public static Player setStats(GameMode game, String uuid, String username) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        JsonObject jsonObject = JsonUtils.parseJson(new URL(String.format("https://api.hypixel.net/player?key=%s&uuid=%s", ModConfig.getInstance().getApiKey(), uuid))).getAsJsonObject();
        System.out.println("HypixelStats checking: " + uuid);
        Player toReturn = null;
        for (GameMode gameMode : GameMode.values()) {
            Class<? extends PlayerFactory> factory = gameMode.getFactory();
            Method createPlayerMethod = factory.getMethod("createPlayer", JsonObject.class, String.class, String.class);
            Method getInstanceMethod = factory.getMethod("getInstance");
            Player player = (Player) createPlayerMethod.invoke(getInstanceMethod.invoke(null), jsonObject, uuid, username);
            if (game.getType().isInstance(player)) {
                toReturn = player;
            }
        }

        return toReturn;


    }


}
