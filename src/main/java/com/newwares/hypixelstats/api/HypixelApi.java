package com.newwares.hypixelstats.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.newwares.hypixelstats.api.modes.*;
import com.newwares.hypixelstats.config.ConfigData;
import com.newwares.hypixelstats.utils.ChatColour;
import com.newwares.hypixelstats.utils.ChatUtils;
import com.newwares.hypixelstats.utils.JsonUtils;
import com.newwares.hypixelstats.utils.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public void setMainStats(JsonObject jsonObject, Player player) {

        if (jsonObject.has("rank")) {
            player.setRank(jsonObject.get("rank").getAsString());
        } else if (jsonObject.has("monthlyPackageRank")) {
            player.setRank(jsonObject.get("monthlyPackageRank").getAsString());
        } else if (jsonObject.has("newPackageRank")) {
            player.setRank(jsonObject.get("newPackageRank").getAsString());
        }

        if (jsonObject.has("win_streak")) player.setWs(jsonObject.get("win_streak").getAsInt());
        if (jsonObject.has("wins")) player.setWins(jsonObject.get("wins").getAsInt());
        if (jsonObject.has("losses")) player.setLosses(jsonObject.get("losses").getAsInt());
        if (jsonObject.has("kills")) player.setKills(jsonObject.get("kills").getAsInt());
        if (jsonObject.has("deaths")) player.setDeaths(jsonObject.get("deaths").getAsInt());
    }

    public Player setStats() throws IOException, InterruptedException {
        JsonObject wholejsonObject = JsonUtils.parseJson(new URL(String.format("https://api.hypixel.net/player?key=%s&uuid=%s", ConfigData.getInstance().getApiKey(), uuid))).getAsJsonObject();
        JsonObject jsonObject = wholejsonObject.get("player").getAsJsonObject();
        JsonObject statJsonObject = jsonObject.get("stats").getAsJsonObject();
        if (wholejsonObject.get("success").getAsString().equals("false") && wholejsonObject.get("cause").getAsString().replace("\"", "").equals("Invalid API key")) {
            ChatUtils.print(ChatColour.RED + "Invalid API key. Please run /api new to set it.");
        } else {
            switch (game) {
                case "BEDWARS": {
                    if (!PlayerCache.getInstance().getBedwarsPlayers().containsKey(uuid)) {
                        BedwarsPlayer player = new BedwarsPlayer(uuid, username);
                        if (Integer.parseInt(uuid.substring(12, 13)) == 1) {
                            player.setNicked(true);
                        } else if (Integer.parseInt(uuid.substring(12, 13)) == 2) {
                            player.setBot(true);
                        } else {
                            if (statJsonObject.has("SkyWars")) {
                                JsonObject bedwarsObject = statJsonObject.get("Bedwars").getAsJsonObject();
                                if (bedwarsObject.has("achievements")) {
                                    JsonObject achievements = jsonObject.get("achievements").getAsJsonObject();
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
                                player = new BedwarsPlayer(uuid, username);
                                PlayerCache.getInstance().updateBedwarsPlayers(uuid, player);
                            }
                        }

                        PlayerCache.getInstance().updateBedwarsPlayers(uuid, player);
                    }
                    return PlayerCache.getInstance().getBedwarsPlayers().get(uuid);

                }
                case "NORMAL_SKYWARS": {
                    if (!PlayerCache.getInstance().getNormalSkywarsPlayers().containsKey(uuid)) {
                        NormalSkywarsPlayer player = new NormalSkywarsPlayer(uuid, username);
                        if (Integer.parseInt(uuid.substring(12, 13)) == 1) {
                            player.setNicked(true);
                        } else if (Integer.parseInt(uuid.substring(12, 13)) == 2) {
                            player.setBot(true);
                        } else {
                            if (statJsonObject.has("SkyWars")) {
                                JsonObject skywarsObject = statJsonObject.get("SkyWars").getAsJsonObject();
                                setMainStats(skywarsObject, player);
                                if (skywarsObject.has("activeKit_SOLO"))
                                    player.setCurrentKit(StringUtils.toTitleCase(skywarsObject.get("activeKit_SOLO").getAsString().substring(skywarsObject.get("activeKit_SOLO").getAsString().lastIndexOf("_") + 1).replace("_", " ")));                                int highestTime = 0;
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
                                player = new NormalSkywarsPlayer(uuid, username);
                                PlayerCache.getInstance().updateNormalSkywarsPlayers(uuid, player);
                            }
                        }
                        PlayerCache.getInstance().updateNormalSkywarsPlayers(uuid, player);
                    }
                    return PlayerCache.getInstance().getNormalSkywarsPlayers().get(uuid);

                }
                case "INSANE_SKYWARS": {
                    if (!PlayerCache.getInstance().getInsaneSkywarsPlayers().containsKey(uuid)) {
                        InsaneSkywarsPlayer player = new InsaneSkywarsPlayer(uuid, username);
                        if (Integer.parseInt(uuid.substring(12, 13)) == 1) {
                            player.setNicked(true);
                        } else if (Integer.parseInt(uuid.substring(12, 13)) == 2) {
                            player.setBot(true);
                        } else {
                            if (statJsonObject.has("SkyWars")) {
                                JsonObject skywarsObject = statJsonObject.get("SkyWars").getAsJsonObject();
                                setMainStats(skywarsObject, player);
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
                                player = new InsaneSkywarsPlayer(uuid, username);
                                PlayerCache.getInstance().updateInsaneSkywarsPlayers(uuid, player);
                            }
                        }
                        PlayerCache.getInstance().updateInsaneSkywarsPlayers(uuid, player);
                    }
                    return PlayerCache.getInstance().getInsaneSkywarsPlayers().get(uuid);
                }
                case "RANKED_SKYWARS": {
                    if (!PlayerCache.getInstance().getRankedSkywarsPlayers().containsKey(uuid)) {
                        RankedSkywarsPlayer player = new RankedSkywarsPlayer(uuid, username);
                        if (Integer.parseInt(uuid.substring(12, 13)) == 1) {
                            player.setNicked(true);
                        } else if (Integer.parseInt(uuid.substring(12, 13)) == 2) {
                            player.setBot(true);
                        } else {
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
                                setMainStats(skywarsObject, player);
                                player.setMostUsedKit(StringUtils.toTitleCase(mainKit));
                                if (skywarsObject.has("levelFormatted"))
                                    player.setLevel(skywarsObject.get("levelFormatted").getAsString());
                            } else {
                                player = new RankedSkywarsPlayer(uuid, username);
                                PlayerCache.getInstance().updateRankedSkywarsPlayers(uuid, player);
                            }
                        }
                        PlayerCache.getInstance().updateRankedSkywarsPlayers(uuid, player);
                    }
                    return PlayerCache.getInstance().getRankedSkywarsPlayers().get(uuid);
                }
                case "SPEED_UHC": {
                    if (!PlayerCache.getInstance().getSpeedUHCPlayers().containsKey(uuid)) {
                        SpeedUHCPlayer player = new SpeedUHCPlayer(uuid, username);
                        if (Integer.parseInt(uuid.substring(12, 13)) == 1) {
                            player.setNicked(true);
                        } else if (Integer.parseInt(uuid.substring(12, 13)) == 2) {
                            player.setBot(true);
                        } else {
                            if (statJsonObject.has("SpeedUHC")) {
                                JsonObject speedUHCObject = statJsonObject.get("SpeedUHC").getAsJsonObject();
                                setMainStats(speedUHCObject, player);
                                if (speedUHCObject.has("activeMasterPerk"))
                                    player.setCurrentMastery(StringUtils.toTitleCase(speedUHCObject.get("activeMasterPerk").getAsString().replace("mastery_", "")));
                                if (speedUHCObject.has("activeKit_NORMAL"))
                                    player.setCurrentKit(StringUtils.toTitleCase(speedUHCObject.get("activeKit_NORMAL").getAsString().replace("kit_basic_normal_", "")));
                                if (speedUHCObject.has("score"))
                                    player.setScore(speedUHCObject.get("score").getAsInt());
                            } else {
                                player = new SpeedUHCPlayer(uuid, username);
                                PlayerCache.getInstance().updateSpeedUHCPlayers(uuid, player);
                            }
                        }
                        PlayerCache.getInstance().updateSpeedUHCPlayers(uuid, player);
                    }
                    return PlayerCache.getInstance().getSpeedUHCPlayers().get(uuid);
                }
            }
        }
        return null;
    }


}
