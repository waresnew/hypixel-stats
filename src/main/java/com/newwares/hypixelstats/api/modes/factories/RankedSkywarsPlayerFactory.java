package com.newwares.hypixelstats.api.modes.factories;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.newwares.hypixelstats.api.PlayerCache;
import com.newwares.hypixelstats.api.modes.Player;
import com.newwares.hypixelstats.api.modes.RankedSkywarsPlayer;
import com.newwares.hypixelstats.utils.StringUtils;

import java.util.Map;

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
                    if (skywarsObject.has("activeKit_RANKED"))
                        player.setCurrentKit(StringUtils.toTitleCase(skywarsObject.get("activeKit_RANKED").getAsString().replace("kit_ranked_ranked_", "")));
                    int highestTime = 0;
                    String mainKit = "none";
                    for (Map.Entry<String, JsonElement> element : skywarsObject.entrySet()) {
                        if (element.getKey().startsWith("time_played_kit_ranked_ranked_")) {
                            if (highestTime < element.getValue().getAsInt()) {
                                highestTime = element.getValue().getAsInt();
                                mainKit = element.getKey().replace("time_played_kit_ranked_ranked_", "").replace("_", " ");
                            }
                        }
                    }
                    setMainStats(playerJsonObject, skywarsObject, player);
                    player.setMostUsedKit(StringUtils.toTitleCase(mainKit));
                    if (skywarsObject.has("levelFormatted"))
                        player.setLevel(skywarsObject.get("levelFormatted").getAsString());
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
