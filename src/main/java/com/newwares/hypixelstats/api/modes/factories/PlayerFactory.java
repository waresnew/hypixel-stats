package com.newwares.hypixelstats.api.modes.factories;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.api.PlayerCache;
import com.newwares.hypixelstats.api.modes.*;
import com.newwares.hypixelstats.utils.ChatColour;
import com.newwares.hypixelstats.utils.ChatUtils;

import java.io.IOException;

public abstract class PlayerFactory {
    public static Player switchModes(String game, String uuid, String username) {
        switch (game) {
            case "BEDWARS": {
                return new BedwarsPlayer(new Player(uuid, username));
            }
            case "NORMAL_SKYWARS": {
                return new NormalSkywarsPlayer(new Player(uuid, username));
            }
            case "INSANE_SKYWARS": {
                return new InsaneSkywarsPlayer(new Player(uuid, username));
            }
            case "RANKED_SKYWARS": {
                return new RankedSkywarsPlayer(new Player(uuid, username));
            }
            case "SPEED_UHC": {
                return new SpeedUHCPlayer(new Player(uuid, username));
            }
        }
        return null;
    }

    public static void setMainStats(JsonObject playerObject, JsonObject gameObject, Player player) {
        if (playerObject.has("rank")) {
            player.setRank(playerObject.get("rank").getAsString());
        } else if (playerObject.has("monthlyPackageRank")) {
            player.setRank(playerObject.get("monthlyPackageRank").getAsString());
        } else if (playerObject.has("newPackageRank")) {
            player.setRank(playerObject.get("newPackageRank").getAsString());
        }
        if (gameObject.has("win_streak")) player.setWs(gameObject.get("win_streak").getAsInt());
        if (gameObject.has("wins")) player.setWins(gameObject.get("wins").getAsInt());
        if (gameObject.has("losses")) player.setLosses(gameObject.get("losses").getAsInt());
        if (gameObject.has("kills")) player.setKills(gameObject.get("kills").getAsInt());
        if (gameObject.has("deaths")) player.setDeaths(gameObject.get("deaths").getAsInt());
    }

    public static boolean isValidPlayer(JsonObject jsonObject, String game, String uuid, String username) throws IOException, InterruptedException, IllegalArgumentException {
        if (Integer.parseInt(uuid.substring(12, 13)) == 1) {
            ChatUtils.print(ChatColour.RED.getColourCode() + username + " is nicked!");
            return false;
        } else if (Integer.parseInt(uuid.substring(12, 13)) == 2) {
            return false;
        }
        JsonObject playerJsonObject = jsonObject.get("player").getAsJsonObject();
        if (!playerJsonObject.has("stats")) {
            PlayerCache.getInstance().updateCache(uuid, switchModes(game, uuid, username));
            return false;
        }
        if (jsonObject.get("success").getAsString().equals("false") && jsonObject.get("cause").getAsString().replace("\"", "").equals("Invalid API key")) {
            throw new IllegalArgumentException(ChatColour.RED.getColourCode() + "Invalid API key. Please run /api new to set it.");
        }
        return true;
    }

    public abstract Player createPlayer(JsonObject jsonObject, String uuid, String username);


}
