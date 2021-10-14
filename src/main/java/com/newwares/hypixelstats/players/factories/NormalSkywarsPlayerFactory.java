package com.newwares.hypixelstats.players.factories;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.players.NormalSkywarsPlayer;
import com.newwares.hypixelstats.players.Player;
import com.newwares.hypixelstats.config.PlayerCache;
import com.newwares.hypixelstats.utils.StringUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class NormalSkywarsPlayerFactory extends PlayerFactory {

    private static NormalSkywarsPlayerFactory instance;

    private NormalSkywarsPlayerFactory() {
    }

    public static NormalSkywarsPlayerFactory getInstance() {
        if (instance == null) {
            instance = new NormalSkywarsPlayerFactory();
        }
        return instance;
    }

    @Override
    public Player createPlayer(JsonObject jsonObject, String uuid, String username) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (isValidPlayer(jsonObject, "NORMAL_SKYWARS", uuid, username)) {
            if (PlayerCache.getInstance().getCache(uuid, NormalSkywarsPlayer.class) == null) {
                JsonObject playerJsonObject = jsonObject.get("player").getAsJsonObject();
                JsonObject statJsonObject = playerJsonObject.get("stats").getAsJsonObject();
                NormalSkywarsPlayer player = new NormalSkywarsPlayer(uuid, playerJsonObject.get("displayname").getAsString());

                if (statJsonObject.has("SkyWars")) {
                    JsonObject skywarsObject = statJsonObject.get("SkyWars").getAsJsonObject();
                    setMainStats(playerJsonObject, player);
                    if (skywarsObject.has("activeKit_SOLO"))
                        player.setCurrentKit(StringUtils.toTitleCase(skywarsObject.get("activeKit_SOLO").getAsString().substring(skywarsObject.get("activeKit_SOLO").getAsString().lastIndexOf("_") + 1).replace("_", " ")));
                    if (skywarsObject.has("levelFormatted"))
                        player.setLevel(skywarsObject.get("levelFormatted").getAsString());
                    if (skywarsObject.has("deaths_solo_normal"))
                        player.setDeaths(skywarsObject.get("deaths_solo_normal").getAsInt());
                    if (skywarsObject.has("deaths_team_normal"))
                        player.setDeaths(player.getDeaths() + skywarsObject.get("deaths_team_normal").getAsInt());
                    if (skywarsObject.has("kills_solo_normal"))
                        player.setKills(skywarsObject.get("kills_solo_normal").getAsInt());
                    if (skywarsObject.has("kills_team_normal"))
                        player.setKills(player.getKills() + skywarsObject.get("kills_team_normal").getAsInt());
                    if (skywarsObject.has("wins_solo_normal"))
                        player.setWins(skywarsObject.get("wins_solo_normal").getAsInt());
                    if (skywarsObject.has("wins_team_normal"))
                        player.setWins(player.getWins() + skywarsObject.get("wins_team_normal").getAsInt());
                    if (skywarsObject.has("losses_solo_normal"))
                        player.setLosses(skywarsObject.get("losses_solo_normal").getAsInt());
                    if (skywarsObject.has("losses_team_normal"))
                        player.setLosses(player.getLosses() + skywarsObject.get("losses_team_normal").getAsInt());
                } else {
                    player = new NormalSkywarsPlayer(uuid, playerJsonObject.get("displayname").getAsString());
                    PlayerCache.getInstance().updateCache(uuid, player);

                }
                PlayerCache.getInstance().updateCache(uuid, player);
            }
        }
        return PlayerCache.getInstance().getCache(uuid, NormalSkywarsPlayer.class);
    }
}
