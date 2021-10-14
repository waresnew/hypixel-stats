package com.newwares.hypixelstats.api.modes.factories;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.api.modes.Player;
import com.newwares.hypixelstats.api.modes.SpeedUHCPlayer;
import com.newwares.hypixelstats.config.PlayerCache;
import com.newwares.hypixelstats.utils.StringUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class SpeedUHCPlayerFactory extends PlayerFactory {
    private static SpeedUHCPlayerFactory instance;

    private SpeedUHCPlayerFactory() {
    }

    public static SpeedUHCPlayerFactory getInstance() {
        if (instance == null) {
            instance = new SpeedUHCPlayerFactory();
        }
        return instance;
    }


    @Override
    public Player createPlayer(JsonObject jsonObject, String uuid, String username) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (isValidPlayer(jsonObject, "SPEED_UHC", uuid, username)) {
            if (PlayerCache.getInstance().getCache(uuid, SpeedUHCPlayer.class) == null) {
                JsonObject playerJsonObject = jsonObject.get("player").getAsJsonObject();
                JsonObject statJsonObject = playerJsonObject.get("stats").getAsJsonObject();
                SpeedUHCPlayer player = new SpeedUHCPlayer(uuid, playerJsonObject.get("displayname").getAsString());
                if (statJsonObject.has("SpeedUHC")) {
                    JsonObject speedUHCObject = statJsonObject.get("SpeedUHC").getAsJsonObject();
                    setMainStats(playerJsonObject, player);
                    if (speedUHCObject.has("activeMasterPerk"))
                        player.setCurrentMastery(StringUtils.toTitleCase(speedUHCObject.get("activeMasterPerk").getAsString().replace("mastery_", "").replace("_", " ")));
                    if (speedUHCObject.has("activeKit_NORMAL"))
                        player.setCurrentKit(StringUtils.toTitleCase(speedUHCObject.get("activeKit_NORMAL").getAsString().replace("kit_basic_normal_", "")));
                    if (speedUHCObject.has("score"))
                        player.setScore(speedUHCObject.get("score").getAsInt());
                    if (speedUHCObject.has("win_streak")) player.setWs(speedUHCObject.get("win_streak").getAsInt());
                    if (speedUHCObject.has("wins")) player.setWins(speedUHCObject.get("wins").getAsInt());
                    if (speedUHCObject.has("losses")) player.setLosses(speedUHCObject.get("losses").getAsInt());
                    if (speedUHCObject.has("kills")) player.setKills(speedUHCObject.get("kills").getAsInt());
                    if (speedUHCObject.has("deaths")) player.setDeaths(speedUHCObject.get("deaths").getAsInt());
                } else {
                    player = new SpeedUHCPlayer(uuid, playerJsonObject.get("displayname").getAsString());
                    PlayerCache.getInstance().updateCache(uuid, player);

                }
                PlayerCache.getInstance().updateCache(uuid, player);
            }
        }
        return PlayerCache.getInstance().getCache(uuid, SpeedUHCPlayer.class);
    }
}
