package com.newwares.hypixelstats.api.modes.factories;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.api.PlayerCache;
import com.newwares.hypixelstats.api.modes.*;
import com.newwares.hypixelstats.utils.ChatColour;
import com.newwares.hypixelstats.utils.ChatUtils;

public abstract class PlayerFactory {
    public static Player switchModes(JsonObject playerObject, String game, String uuid, String username) {
        switch (game) {
            case "BEDWARS": {
                return new BedwarsPlayer(new Player(uuid, playerObject.get("displayname").getAsString()));
            }
            case "NORMAL_SKYWARS": {
                return new NormalSkywarsPlayer(new Player(uuid, playerObject.get("displayname").getAsString()));
            }
            case "INSANE_SKYWARS": {
                return new InsaneSkywarsPlayer(new Player(uuid, playerObject.get("displayname").getAsString()));
            }
            case "RANKED_SKYWARS": {
                return new RankedSkywarsPlayer(new Player(uuid, playerObject.get("displayname").getAsString()));
            }
            case "SPEED_UHC": {
                return new SpeedUHCPlayer(new Player(uuid, playerObject.get("displayname").getAsString()));
            }
        }
        return null;
    }

    public static void setMainStats(JsonObject playerObject, JsonObject gameObject, Player player) {
        if (playerObject.has("rank") && !playerObject.get("rank").getAsString().equals("NONE")) {
            player.setRank(playerObject.get("rank").getAsString());
        } else if (playerObject.has("monthlyPackageRank") && playerObject.get("monthlyPackageRank").getAsString().equals("SUPERSTAR")) {
            player.setRank(playerObject.get("monthlyPackageRank").getAsString());
        } else if (playerObject.has("newPackageRank")) {
            player.setRank(playerObject.get("newPackageRank").getAsString());
        }
    }

    public static boolean isValidPlayer(JsonObject jsonObject, String game, String uuid, String username) throws IllegalArgumentException {
        if (Integer.parseInt(uuid.substring(12, 13)) == 1) {
            ChatUtils.print(ChatColour.RED + username + " is nicked!");
            return false;
        } else if (Integer.parseInt(uuid.substring(12, 13)) == 2) {
            return false;
        }
        JsonObject playerJsonObject = jsonObject.get("player").getAsJsonObject();
        if (!playerJsonObject.has("stats")) {
            PlayerCache.getInstance().updateCache(uuid, switchModes(playerJsonObject, game, uuid, username));
            return false;
        }
        if (jsonObject.get("success").getAsString().equals("false") && jsonObject.get("cause").getAsString().replace("\"", "").equals("Invalid API key")) {
            throw new IllegalArgumentException(ChatColour.RED + "Invalid API key. Please run /api new to set it.");
        }
        return true;
    }

    public abstract Player createPlayer(JsonObject jsonObject, String uuid, String username);


}
