package com.newwares.hypixelstats.api.modes.factories;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.api.PlayerCache;
import com.newwares.hypixelstats.api.modes.BedwarsPlayer;
import com.newwares.hypixelstats.api.modes.Player;

public class BedwarsPlayerFactory extends PlayerFactory {

    @Override
    public Player createPlayer(JsonObject jsonObject, String uuid, String username) {
        if (isValidPlayer(jsonObject, "BEDWARS", uuid, username)) {
            if (PlayerCache.getInstance().getCache(uuid, BedwarsPlayer.class) == null) {
                JsonObject playerJsonObject = jsonObject.get("player").getAsJsonObject();
                JsonObject statJsonObject = playerJsonObject.get("stats").getAsJsonObject();
                BedwarsPlayer player = new BedwarsPlayer(new Player(uuid, playerJsonObject.get("displayname").getAsString()));
                if (playerJsonObject.has("rank") && !playerJsonObject.get("rank").getAsString().equals("NONE")) {
                    player.setRank(playerJsonObject.get("rank").getAsString());
                } else if (playerJsonObject.has("monthlyPackageRank") && playerJsonObject.get("monthlyPackageRank").getAsString().equals("SUPERSTAR")) {
                    player.setRank(playerJsonObject.get("monthlyPackageRank").getAsString());
                } else if (playerJsonObject.has("newPackageRank")) {
                    player.setRank(playerJsonObject.get("newPackageRank").getAsString());
                }
                if (statJsonObject.has("Bedwars")) {
                    JsonObject bedwarsObject = statJsonObject.get("Bedwars").getAsJsonObject();
                    if (playerJsonObject.has("achievements")) {
                        JsonObject achievements = playerJsonObject.get("achievements").getAsJsonObject();
                        if (achievements.has("bedwars_level"))
                            player.setLevel(achievements.get("bedwars_level").getAsInt());
                    }
                    if (bedwarsObject.has("winstreak"))
                        player.setWs(bedwarsObject.get("winstreak").getAsInt());
                    if (bedwarsObject.has("wins_bedwars"))
                        player.setWins(bedwarsObject.get("wins_bedwars").getAsInt());
                    if (bedwarsObject.has("losses_bedwars"))
                        player.setLosses(bedwarsObject.get("losses_bedwars").getAsInt());
                    if (bedwarsObject.has("kills_bedwars"))
                        player.setKills(bedwarsObject.get("kills_bedwars").getAsInt());
                    if (bedwarsObject.has("deaths_bedwars"))
                        player.setDeaths(bedwarsObject.get("deaths_bedwars").getAsInt());
                    if (bedwarsObject.has("final_kills_bedwars"))
                        player.setFinalKills(bedwarsObject.get("final_kills_bedwars").getAsInt());
                    if (bedwarsObject.has("final_deaths_bedwars"))
                        player.setFinalDeaths(bedwarsObject.get("final_deaths_bedwars").getAsInt());
                    if (bedwarsObject.has("beds_broken_bedwars"))
                        player.setBedBreaks(bedwarsObject.get("beds_broken_bedwars").getAsInt());
                    if (bedwarsObject.has("beds_lost_bedwars"))
                        player.setBedLosses(bedwarsObject.get("beds_lost_bedwars").getAsInt());
                } else {
                    player = new BedwarsPlayer(new Player(uuid, playerJsonObject.get("displayname").getAsString()));
                    PlayerCache.getInstance().updateCache(uuid, player);
                }


                PlayerCache.getInstance().updateCache(uuid, player);
            }
        }
        return PlayerCache.getInstance().getCache(uuid, BedwarsPlayer.class);

    }
}
