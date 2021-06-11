package com.newwares.hypixelstats.api.modes.factories;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.newwares.hypixelstats.api.PlayerCache;
import com.newwares.hypixelstats.api.modes.NormalSkywarsPlayer;
import com.newwares.hypixelstats.api.modes.Player;
import com.newwares.hypixelstats.utils.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NormalSkywarsPlayerFactory extends PlayerFactory {
    @Override
    public Player createPlayer(JsonObject jsonObject, String uuid, String username) {
        if (PlayerCache.getInstance().getCache(uuid, NormalSkywarsPlayer.class) == null) {
            JsonObject playerJsonObject = jsonObject.get("player").getAsJsonObject();
            JsonObject statJsonObject = playerJsonObject.get("stats").getAsJsonObject();
            NormalSkywarsPlayer player = new NormalSkywarsPlayer(new Player(uuid, username));

            if (statJsonObject.has("SkyWars")) {
                JsonObject skywarsObject = statJsonObject.get("SkyWars").getAsJsonObject();
                setMainStats(playerJsonObject, skywarsObject, player);
                if (skywarsObject.has("activeKit_SOLO"))
                    player.setCurrentKit(StringUtils.toTitleCase(skywarsObject.get("activeKit_SOLO").getAsString().substring(skywarsObject.get("activeKit_SOLO").getAsString().lastIndexOf("_") + 1).replace("_", " ")));
                int highestTime = 0;
                String mainKit = "none";

                for (Map.Entry<String, JsonElement> element : skywarsObject.entrySet()) {
                    Matcher normalKitMatcher = Pattern.compile("time_played_kit_.+_solo_.+").matcher(element.getKey());
                    if (normalKitMatcher.matches()) {
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
                player = new NormalSkywarsPlayer(new Player(uuid, username));
                PlayerCache.getInstance().updateCache(uuid, player);

            }
            PlayerCache.getInstance().updateCache(uuid, player);
        }
        return PlayerCache.getInstance().getCache(uuid, NormalSkywarsPlayer.class);
    }
}
