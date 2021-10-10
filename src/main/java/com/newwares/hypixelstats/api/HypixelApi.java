package com.newwares.hypixelstats.api;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.api.modes.*;
import com.newwares.hypixelstats.api.modes.factories.*;
import com.newwares.hypixelstats.config.ConfigData;
import com.newwares.hypixelstats.utils.JsonUtils;

import java.io.IOException;
import java.net.URL;

public class HypixelApi {
    private final String uuid;
    private final String username;
    private String game;

    public HypixelApi(String game, String uuid, String username) {
        for (Game mode : Game.values()) {
            if (mode.name().equals(game)) {
                this.game = mode.name();
                break;
            }
        }
        this.uuid = uuid;
        this.username = username;
    }


    public Player setStats() throws IOException {
        JsonObject jsonObject = JsonUtils.parseJson(new URL(String.format("https://api.hypixel.net/player?key=%s&uuid=%s", ConfigData.getInstance().getApiKey(), uuid))).getAsJsonObject();
        JsonObject player;
        try {
            player = jsonObject.get("player").getAsJsonObject();
        } catch (IllegalStateException ignored) {
            return null;
        }
        System.out.println("HypixelStats checking: " + uuid);
        Player testPlayer = PlayerFactory.switchModes(player, game, username, uuid);


        if (testPlayer instanceof BedwarsPlayer) {
            new InsaneSkywarsPlayerFactory().createPlayer(jsonObject, uuid, username);
            new NormalSkywarsPlayerFactory().createPlayer(jsonObject, uuid, username);
            new RankedSkywarsPlayerFactory().createPlayer(jsonObject, uuid, username);
            new SpeedUHCPlayerFactory().createPlayer(jsonObject, uuid, username);
            return new BedwarsPlayerFactory().createPlayer(jsonObject, uuid, username);
        } else if (testPlayer instanceof InsaneSkywarsPlayer) {
            new NormalSkywarsPlayerFactory().createPlayer(jsonObject, uuid, username);
            new RankedSkywarsPlayerFactory().createPlayer(jsonObject, uuid, username);
            new SpeedUHCPlayerFactory().createPlayer(jsonObject, uuid, username);
            new BedwarsPlayerFactory().createPlayer(jsonObject, uuid, username);
            return new InsaneSkywarsPlayerFactory().createPlayer(jsonObject, uuid, username);
        } else if (testPlayer instanceof NormalSkywarsPlayer) {
            new InsaneSkywarsPlayerFactory().createPlayer(jsonObject, uuid, username);
            new RankedSkywarsPlayerFactory().createPlayer(jsonObject, uuid, username);
            new BedwarsPlayerFactory().createPlayer(jsonObject, uuid, username);
            new SpeedUHCPlayerFactory().createPlayer(jsonObject, uuid, username);
            return new NormalSkywarsPlayerFactory().createPlayer(jsonObject, uuid, username);
        } else if (testPlayer instanceof RankedSkywarsPlayer) {
            new InsaneSkywarsPlayerFactory().createPlayer(jsonObject, uuid, username);
            new NormalSkywarsPlayerFactory().createPlayer(jsonObject, uuid, username);
            new BedwarsPlayerFactory().createPlayer(jsonObject, uuid, username);
            new SpeedUHCPlayerFactory().createPlayer(jsonObject, uuid, username);
            return new RankedSkywarsPlayerFactory().createPlayer(jsonObject, uuid, username);
        } else if (testPlayer instanceof SpeedUHCPlayer) {
            new InsaneSkywarsPlayerFactory().createPlayer(jsonObject, uuid, username);
            new NormalSkywarsPlayerFactory().createPlayer(jsonObject, uuid, username);
            new RankedSkywarsPlayerFactory().createPlayer(jsonObject, uuid, username);
            new BedwarsPlayerFactory().createPlayer(jsonObject, uuid, username);
            return new SpeedUHCPlayerFactory().createPlayer(jsonObject, uuid, username);
        } else {
            return null;
        }

    }


}
