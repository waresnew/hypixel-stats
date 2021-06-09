package com.newwares.hypixelstats.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.newwares.hypixelstats.api.modes.*;
import com.newwares.hypixelstats.config.ConfigData;
import com.newwares.hypixelstats.utils.ChatColour;
import com.newwares.hypixelstats.utils.ChatUtils;
import com.newwares.hypixelstats.utils.JsonUtils;
import com.newwares.hypixelstats.utils.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HypixelApi {
    private String game;
    private final String uuid;

    public HypixelApi(String game, String uuid) {
        for (Game mode : Game.values()) {
            if (mode.name().equals(game)) {
                this.game = mode.name();
                break;
            }
        }
        this.uuid = uuid;
    }

    public void setMainStats(JsonObject jsonObject, Player player) {
        player.setWs(jsonObject.get("win_streak").getAsInt());
        player.setWins(jsonObject.get("wins").getAsInt());
        player.setLosses(jsonObject.get("losses").getAsInt());
        player.setKills(jsonObject.get("kills").getAsInt());
        player.setDeaths(jsonObject.get("deaths").getAsInt());
    }

    public void setStats() {
        new Thread(() -> {
            JsonObject jsonObject = JsonUtils.parseJson(String.format("https://api.hypixel.net/player?key=%s&uuid=%s", ConfigData.getInstance().getApiKey(), uuid)).getAsJsonObject().get("player").getAsJsonObject();
            JsonObject statJsonObject = jsonObject.get("stats").getAsJsonObject();
            if (statJsonObject.get("success").getAsString().equals("false") && statJsonObject.get("cause").getAsString().replace("\"", "").equals("Invalid API key")) {
                ChatUtils.print(ChatColour.RED + "Invalid API key. Please run /api new to set it.");
            } else {
                switch (game) {
                    case "BEDWARS": {
                        if (!PlayerCache.getInstance().getBedwarsPlayers().containsKey(uuid)) {
                            BedwarsPlayer player = new BedwarsPlayer(uuid);
                            if (statJsonObject.get("success").getAsString().equals("true") && statJsonObject.get("player").getAsString().equals("null")) {
                                player.setNicked(true);
                            } else {
                                JsonObject bedwarsObject = statJsonObject.get("BEDWARS").getAsJsonObject();
                                player.setLevel(jsonObject.get("achievements").getAsJsonObject().get("bedwars_level").getAsInt());
                                player.setWs(bedwarsObject.get("winstreak").getAsInt());
                                player.setWins(bedwarsObject.get("wins_bedwars").getAsInt());
                                player.setLosses(bedwarsObject.get("losses_bedwars").getAsInt());
                                player.setKills(bedwarsObject.get("kills_bedwars").getAsInt());
                                player.setDeaths(bedwarsObject.get("deaths_bedwars").getAsInt());
                                player.setFinalKills(bedwarsObject.get("final_kills_bedwars").getAsInt());
                                player.setFinalDeaths(bedwarsObject.get("final_deaths_bedwars").getAsInt());
                                player.setBedBreaks(bedwarsObject.get("beds_broken_bedwars").getAsInt());
                                player.setBedLosses(bedwarsObject.get("beds_lost_bedwars").getAsInt());
                            }
                            PlayerCache.getInstance().updateBedwarsPlayers(uuid, player);
                        }
                    }
                    case "NORMAL_SKYWARS": {
                        if (!PlayerCache.getInstance().getNormalSkywarsPlayers().containsKey(uuid)) {
                            NormalSkywarsPlayer player = new NormalSkywarsPlayer(uuid);
                            if (statJsonObject.get("success").getAsString().equals("true") && statJsonObject.get("player").getAsString().equals("null")) {
                                player.setNicked(true);
                            } else {
                                JsonObject skywarsObject = statJsonObject.get("SKYWARS").getAsJsonObject();
                                setMainStats(skywarsObject, player);
                                player.setCurrentKit(StringUtils.toTitleCase(skywarsObject.get("activeKit_SOLO").getAsString().replace("kit_ranked_ranked_", "")));
                                long highestTime = 0;
                                String mainKit = "none";

                                for (Map.Entry<String, JsonElement> element : skywarsObject.entrySet()) {
                                    Matcher normalKitMatcher = Pattern.compile("time_played_kit_.+_solo_.+").matcher(element.getKey());
                                    if (normalKitMatcher.matches()) {
                                        if (highestTime < element.getValue().getAsLong()) {
                                            highestTime = element.getValue().getAsLong();
                                            mainKit = element.getKey().substring(element.getKey().lastIndexOf("_") + 1).replace("_", " ");
                                        }
                                    }
                                }
                                player.setMostUsedKit(StringUtils.toTitleCase(mainKit));
                                player.setLevel(skywarsObject.get("levelFormatted").getAsString());
                            }
                            PlayerCache.getInstance().updateNormalSkywarsPlayers(uuid, player);
                        }
                    }
                    case "INSANE_SKYWARS": {
                        if (!PlayerCache.getInstance().getInsaneSkywarsPlayers().containsKey(uuid)) {
                            InsaneSkywarsPlayer player = new InsaneSkywarsPlayer(uuid);
                            if (statJsonObject.get("success").getAsString().equals("true") && statJsonObject.get("player").getAsString().equals("null")) {
                                player.setNicked(true);
                            } else {
                                JsonObject skywarsObject = statJsonObject.get("SKYWARS").getAsJsonObject();
                                setMainStats(skywarsObject, player);
                                player.setCurrentKit(StringUtils.toTitleCase(skywarsObject.get("activeKit_TEAMS").getAsString().replace("kit_attacking_team_", "")));
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
                                player.setLevel(skywarsObject.get("levelFormatted").getAsString());
                            }
                            PlayerCache.getInstance().updateInsaneSkywarsPlayers(uuid, player);
                        }
                    }
                    case "RANKED_SKYWARS": {
                        if (!PlayerCache.getInstance().getRankedSkywarsPlayers().containsKey(uuid)) {
                            RankedSkywarsPlayer player = new RankedSkywarsPlayer(uuid);
                            if (statJsonObject.get("success").getAsString().equals("true") && statJsonObject.get("player").getAsString().equals("null")) {
                                player.setNicked(true);
                            } else {
                                JsonObject skywarsObject = statJsonObject.get("SKYWARS").getAsJsonObject();
                                player.setCurrentKit(StringUtils.toTitleCase(skywarsObject.get("activeKit_RANKED").getAsString().replace("kit_ranked_ranked_", "")));
                                long highestTime = 0;
                                String mainKit = "none";
                                for (Map.Entry<String, JsonElement> element : skywarsObject.entrySet()) {
                                    if (element.getKey().startsWith("time_played_kit_ranked_ranked_")) {
                                        if (highestTime < element.getValue().getAsLong()) {
                                            highestTime = element.getValue().getAsLong();
                                            mainKit = element.getKey().replace("time_played_kit_ranked_ranked_", "").replace("_", " ");
                                        }
                                    }

                                }
                                setMainStats(skywarsObject, player);
                                player.setMostUsedKit(StringUtils.toTitleCase(mainKit));
                                player.setLevel(skywarsObject.get("levelFormatted").getAsString());
                            }
                            PlayerCache.getInstance().updateRankedSkywarsPlayers(uuid, player);
                        }
                    }
                    case "SPEED_UHC": {
                        if (!PlayerCache.getInstance().getSpeedUHCPlayers().containsKey(uuid)) {
                            SpeedUHCPlayer player = new SpeedUHCPlayer(uuid);
                            if (statJsonObject.get("success").getAsString().equals("true") && statJsonObject.get("player").getAsString().equals("null")) {
                                player.setNicked(true);
                            } else {
                                JsonObject speedUHCObject = statJsonObject.get("SPEED_UHC").getAsJsonObject();
                                setMainStats(speedUHCObject, player);
                                player.setCurrentMastery(StringUtils.toTitleCase(speedUHCObject.get("activeMasterPerk").getAsString().replace("mastery_", "")));
                                player.setCurrentKit(StringUtils.toTitleCase(speedUHCObject.get("activeKit_NORMAL").getAsString().replace("kit_basic_normal_", "")));
                                player.setScore(speedUHCObject.get("score").getAsInt());
                            }
                            PlayerCache.getInstance().updateSpeedUHCPlayers(uuid, player);
                        }
                    }
                }
            }
        }).start();
    }


}
