package com.newwares.hypixelstats.players.factories;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.players.GameMode;
import com.newwares.hypixelstats.players.Player;
import com.newwares.hypixelstats.config.PlayerCache;
import com.newwares.hypixelstats.utils.ChatColour;
import com.newwares.hypixelstats.utils.ChatUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public abstract class PlayerFactory {
    protected void setMainStats(JsonObject playerObject, Player player) {
        player.setTimeCreated(System.currentTimeMillis());
        if (playerObject.has("rank") && !playerObject.get("rank").getAsString().equals("NONE")) {
            player.setRank(playerObject.get("rank").getAsString());
        } else if (playerObject.has("monthlyPackageRank") && playerObject.get("monthlyPackageRank").getAsString().equals("SUPERSTAR")) {
            player.setRank(playerObject.get("monthlyPackageRank").getAsString());
        } else if (playerObject.has("newPackageRank")) {
            player.setRank(playerObject.get("newPackageRank").getAsString());
        }
    }

    protected boolean isValidPlayer(JsonObject jsonObject, String game, String uuid, String username) throws IllegalArgumentException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (Integer.parseInt(uuid.substring(12, 13)) == 1) {
            ChatUtils.print(ChatColour.RED + username + " is nicked!");
            return false;
        } else if (Integer.parseInt(uuid.substring(12, 13)) == 2) {
            return false;
        }
        JsonObject playerJsonObject = jsonObject.get("player").getAsJsonObject();
        GameMode type = GameMode.valueOf(game);
        if (!playerJsonObject.has("stats")) {
            PlayerCache.getInstance().updateCache(uuid, username, type);
            return false;
        }
        if (jsonObject.get("success").getAsString().equals("false") && jsonObject.get("cause").getAsString().replace("\"", "").equals("Invalid API key")) {
            throw new IllegalArgumentException(ChatColour.RED + "Invalid API key. Please run /players new to set it.");
        }
        return true;
    }

    public abstract Player createPlayer(JsonObject jsonObject, String uuid, String username) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;


}
