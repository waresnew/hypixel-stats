package com.newwares.hypixelstats.hypixel.factories;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.config.PlayerCache;
import com.newwares.hypixelstats.hypixel.GameMode;
import com.newwares.hypixelstats.hypixel.InsaneSkywarsPlayer;
import com.newwares.hypixelstats.hypixel.Player;
import com.newwares.hypixelstats.utils.StringUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class InsaneSkywarsPlayerFactory extends PlayerFactory {

    private static InsaneSkywarsPlayerFactory instance;

    private InsaneSkywarsPlayerFactory() {
    }

    public static InsaneSkywarsPlayerFactory getInstance() {
        if (instance == null) {
            instance = new InsaneSkywarsPlayerFactory();
        }
        return instance;
    }

    @Override
    public Player createPlayer(JsonObject jsonObject, String uuid, String username) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (isValidPlayer(jsonObject, "INSANE_SKYWARS", uuid, username)) {
            if (PlayerCache.getInstance().getCache(uuid, InsaneSkywarsPlayer.class) == null) {
                JsonObject playerJsonObject = jsonObject.get("player").getAsJsonObject();
                JsonObject statJsonObject = playerJsonObject.get("stats").getAsJsonObject();
                InsaneSkywarsPlayer player = new InsaneSkywarsPlayer(uuid, playerJsonObject.get("displayname").getAsString(), GameMode.INSANE_SKYWARS.name());

                if (statJsonObject.has("SkyWars")) {
                    JsonObject skywarsObject = statJsonObject.get("SkyWars").getAsJsonObject();
                    setMainStats(playerJsonObject, player);
                    if (skywarsObject.has("activeKit_TEAMS"))
                        player.setCurrentKit(StringUtils.toTitleCase(skywarsObject.get("activeKit_TEAMS").getAsString().substring(skywarsObject.get("activeKit_TEAMS").getAsString().lastIndexOf("_") + 1).replace("_", " ")));
                    if (skywarsObject.has("levelFormatted"))
                        player.setLevel(skywarsObject.get("levelFormatted").getAsString());
                    if (skywarsObject.has("deaths_solo_insane"))
                        player.setDeaths(skywarsObject.get("deaths_solo_insane").getAsInt());
                    if (skywarsObject.has("deaths_team_insane"))
                        player.setDeaths(player.getDeaths() + skywarsObject.get("deaths_team_insane").getAsInt());
                    if (skywarsObject.has("kills_solo_insane"))
                        player.setKills(skywarsObject.get("kills_solo_insane").getAsInt());
                    if (skywarsObject.has("kills_team_insane"))
                        player.setKills(player.getKills() + skywarsObject.get("kills_team_insane").getAsInt());
                    if (skywarsObject.has("wins_solo_insane"))
                        player.setWins(skywarsObject.get("wins_solo_insane").getAsInt());
                    if (skywarsObject.has("wins_team_insane"))
                        player.setWins(player.getWins() + skywarsObject.get("wins_team_insane").getAsInt());
                    if (skywarsObject.has("losses_solo_insane"))
                        player.setLosses(skywarsObject.get("losses_solo_insane").getAsInt());
                    if (skywarsObject.has("losses_team_insane"))
                        player.setLosses(player.getLosses() + skywarsObject.get("losses_team_insane").getAsInt());

                } else {
                    player = new InsaneSkywarsPlayer(uuid, playerJsonObject.get("displayname").getAsString(), GameMode.INSANE_SKYWARS.name());
                    PlayerCache.getInstance().updateCache(uuid, player);

                }
                PlayerCache.getInstance().updateCache(uuid, player);
            }
        }
        return PlayerCache.getInstance().getCache(uuid, InsaneSkywarsPlayer.class);
    }
}
