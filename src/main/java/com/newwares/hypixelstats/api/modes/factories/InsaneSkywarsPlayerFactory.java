package com.newwares.hypixelstats.api.modes.factories;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.newwares.hypixelstats.api.PlayerCache;
import com.newwares.hypixelstats.api.modes.InsaneSkywarsPlayer;
import com.newwares.hypixelstats.api.modes.Player;
import com.newwares.hypixelstats.utils.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsaneSkywarsPlayerFactory extends PlayerFactory {
    @Override
    public Player createPlayer(JsonObject jsonObject, String uuid, String username) {
        if (isValidPlayer(jsonObject, "INSANE_SKYWARS", uuid, username)) {
            if (PlayerCache.getInstance().getCache(uuid, InsaneSkywarsPlayer.class) == null) {
                JsonObject playerJsonObject = jsonObject.get("player").getAsJsonObject();
                JsonObject statJsonObject = playerJsonObject.get("stats").getAsJsonObject();
                InsaneSkywarsPlayer player = new InsaneSkywarsPlayer(new Player(uuid, playerJsonObject.get("displayname").getAsString()));

                if (statJsonObject.has("SkyWars")) {
                    JsonObject skywarsObject = statJsonObject.get("SkyWars").getAsJsonObject();
                    setMainStats(playerJsonObject, skywarsObject, player);
                    if (skywarsObject.has("activeKit_TEAMS"))
                        player.setCurrentKit(StringUtils.toTitleCase(skywarsObject.get("activeKit_TEAMS").getAsString().substring(skywarsObject.get("activeKit_TEAMS").getAsString().lastIndexOf("_") + 1).replace("_", " ")));
                    int highestTime = 0;
                    String mainKit = "none";
                    for (Map.Entry<String, JsonElement> element : skywarsObject.entrySet()) {
                        Matcher insaneKitMatcher = Pattern.compile("time_played_kit_.+_team_.+").matcher(element.getKey());
                        if (insaneKitMatcher.matches()) {
                            if (highestTime < element.getValue().getAsInt()) {
                                highestTime = element.getValue().getAsInt();
                                mainKit = element.getKey().substring(element.getKey().lastIndexOf("_") + 1).replace("_", " ");
                            }
                        }
                    }
                    player.setMostUsedKit(StringUtils.toTitleCase(mainKit));
                    if (skywarsObject.has("levelFormatted"))
                        player.setLevel(skywarsObject.get("levelFormatted").getAsString());
                } else {
                    player = new InsaneSkywarsPlayer(new Player(uuid, playerJsonObject.get("displayname").getAsString()));
                    PlayerCache.getInstance().updateCache(uuid, player);

                }
                PlayerCache.getInstance().updateCache(uuid, player);
            }
        }
        return PlayerCache.getInstance().getCache(uuid, InsaneSkywarsPlayer.class);
    }
}
