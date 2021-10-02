package com.newwares.hypixelstats.api.modes.factories;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.api.PlayerCache;
import com.newwares.hypixelstats.api.modes.Player;
import com.newwares.hypixelstats.api.modes.RankedSkywarsPlayer;
import com.newwares.hypixelstats.utils.StringUtils;

public class RankedSkywarsPlayerFactory extends PlayerFactory {
    @Override
    public Player createPlayer(JsonObject jsonObject, String uuid, String username) {
        if (isValidPlayer(jsonObject, "RANKED_SKYWARS", uuid, username)) {
            if (PlayerCache.getInstance().getCache(uuid, RankedSkywarsPlayer.class) == null) {
                JsonObject playerJsonObject = jsonObject.get("player").getAsJsonObject();
                JsonObject statJsonObject = playerJsonObject.get("stats").getAsJsonObject();
                RankedSkywarsPlayer player = new RankedSkywarsPlayer(new Player(uuid, playerJsonObject.get("displayname").getAsString()));

                if (statJsonObject.has("SkyWars")) {
                    JsonObject skywarsObject = statJsonObject.get("SkyWars").getAsJsonObject();
                    setMainStats(playerJsonObject, skywarsObject, player);
                    if (skywarsObject.has("activeKit_RANKED"))
                        player.setCurrentKit(StringUtils.toTitleCase(skywarsObject.get("activeKit_RANKED").getAsString().replace("kit_ranked_ranked_", "")));
                    if (skywarsObject.has("levelFormatted"))
                        player.setLevel(skywarsObject.get("levelFormatted").getAsString());
                    if (skywarsObject.has("deaths_ranked"))
                        player.setDeaths(skywarsObject.get("deaths_ranked").getAsInt());
                    if (skywarsObject.has("kills_ranked"))
                        player.setKills(skywarsObject.get("kills_ranked").getAsInt());
                    if (skywarsObject.has("wins_ranked"))
                        player.setWins(skywarsObject.get("wins_ranked").getAsInt());
                    if (skywarsObject.has("losses_ranked"))
                        player.setLosses(skywarsObject.get("losses_ranked").getAsInt());
                } else {
                    player = new RankedSkywarsPlayer(new Player(uuid, playerJsonObject.get("displayname").getAsString()));
                    PlayerCache.getInstance().updateCache(uuid, player);

                }
                PlayerCache.getInstance().updateCache(uuid, player);
            }
        }
        return PlayerCache.getInstance().getCache(uuid, RankedSkywarsPlayer.class);
    }
}
