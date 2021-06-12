package com.newwares.hypixelstats.api.modes.factories;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.api.PlayerCache;
import com.newwares.hypixelstats.api.modes.Player;
import com.newwares.hypixelstats.api.modes.SpeedUHCPlayer;
import com.newwares.hypixelstats.utils.StringUtils;

public class SpeedUHCPlayerFactory extends PlayerFactory {
    @Override
    public Player createPlayer(JsonObject jsonObject, String uuid, String username) {
        if (isValidPlayer(jsonObject, "SPEED_UHC", uuid, username)) {
            if (PlayerCache.getInstance().getCache(uuid, SpeedUHCPlayer.class) == null) {
                JsonObject playerJsonObject = jsonObject.get("player").getAsJsonObject();
                JsonObject statJsonObject = playerJsonObject.get("stats").getAsJsonObject();
                SpeedUHCPlayer player = new SpeedUHCPlayer(new Player(uuid, playerJsonObject.get("displayname").getAsString()));
                if (statJsonObject.has("SpeedUHC")) {
                    JsonObject speedUHCObject = statJsonObject.get("SpeedUHC").getAsJsonObject();
                    setMainStats(playerJsonObject, speedUHCObject, player);
                    if (speedUHCObject.has("activeMasterPerk"))
                        player.setCurrentMastery(StringUtils.toTitleCase(speedUHCObject.get("activeMasterPerk").getAsString().replace("mastery_", "").replace("_", " ")));
                    if (speedUHCObject.has("activeKit_NORMAL"))
                        player.setCurrentKit(StringUtils.toTitleCase(speedUHCObject.get("activeKit_NORMAL").getAsString().replace("kit_basic_normal_", "")));
                    if (speedUHCObject.has("score"))
                        player.setScore(speedUHCObject.get("score").getAsInt());
                } else {
                    player = new SpeedUHCPlayer(new Player(uuid, playerJsonObject.get("displayname").getAsString()));
                    PlayerCache.getInstance().updateCache(uuid, player);

                }
                PlayerCache.getInstance().updateCache(uuid, player);
            }
        }
        return PlayerCache.getInstance().getCache(uuid, SpeedUHCPlayer.class);
    }
}
