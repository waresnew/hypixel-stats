package com.newwares.hypixelstats.api.modes;

import com.newwares.hypixelstats.utils.ChatColour;

public abstract class Player {
    private int kills;
    private int deaths;
    private int wins;
    private int losses;
    private int level;
    private int ws;
    private ChatColour rankColour;
    private boolean nicked = false;
    private String uuid;

    public Player(String uuid) {
        this.setUuid(uuid);
    }

    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return this.uuid.equals(object);
    }

    public void setRank(String rank) {
        switch (rank) {
            case "vip":
            case "vip+": {
                this.rankColour = ChatColour.GREEN;
            }

            case "mvp":
            case "mvp+": {
                this.rankColour = ChatColour.AQUA;
            }

            case "mvp++": {
                this.rankColour = ChatColour.GOLD;
            }

            case "youtube":
            case "admin": {
                this.rankColour = ChatColour.RED;
            }


            case "moderator":
            case "gamemaster": {
                this.rankColour = ChatColour.DARK_GREEN;
            }
        }
    }

    public void setNicked(boolean nicked) {
        this.nicked = nicked;
    }

    public boolean isNicked() {
        return nicked;
    }

    public ChatColour getRankColour() {
        return rankColour;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getKills() {
        return kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getWins() {
        return wins;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getLosses() {
        return losses;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public abstract String getLevel();

    public void setWs(int ws) {
        this.ws = ws;
    }

    public String getWs() {
        ChatColour colour;
        if (ws < 1) {
            colour = ChatColour.DARK_GRAY;
        } else if (ws < 50) {
            colour = ChatColour.GRAY;
        } else if (ws < 200) {
            colour = ChatColour.WHITE;
        } else if (ws < 350) {
            colour = ChatColour.GOLD;
        } else if (ws < 500) {
            colour = ChatColour.DARK_GREEN;
        } else if (ws < 650) {
            colour = ChatColour.RED;
        } else if (ws < 800) {
            colour = ChatColour.DARK_RED;
        } else if (ws < 1000) {
            colour = ChatColour.LIGHT_PURPLE;
        } else {
            colour = ChatColour.DARK_PURPLE;
        }
        return colour + String.valueOf(ws);
    }
    
    public String getKdr() {
        ChatColour colour;
        double kdr = Double.parseDouble(String.format("%.2f", (double) kills / (double) deaths));
        if (kdr < 1) {
            colour = ChatColour.DARK_GRAY;
        } else if (kdr < 5) {
            colour = ChatColour.GRAY;
        } else if (kdr < 10) {
            colour = ChatColour.WHITE;
        } else if (kdr < 20) {
            colour = ChatColour.GOLD;
        } else if (kdr < 35) {
            colour = ChatColour.DARK_GREEN;
        } else if (kdr < 60) {
            colour = ChatColour.RED;
        } else if (kdr < 100) {
            colour = ChatColour.DARK_RED;
        } else if (kdr < 500) {
            colour = ChatColour.LIGHT_PURPLE;
        } else {
            colour = ChatColour.DARK_PURPLE;
        }
        return colour + String.valueOf(kdr);
    }

    public String getWlr() {
        ChatColour colour;
        double wlr = Double.parseDouble(String.format("%.2f", (double) wins / (double) losses));
        if (wlr < 1) {
            colour = ChatColour.DARK_GRAY;
        } else if (wlr < 2) {
            colour = ChatColour.GRAY;
        } else if (wlr < 4) {
            colour = ChatColour.WHITE;
        } else if (wlr < 6) {
            colour = ChatColour.GOLD;
        } else if (wlr < 7) {
            colour = ChatColour.DARK_GREEN;
        } else if (wlr < 10) {
            colour = ChatColour.RED;
        } else if (wlr < 15) {
            colour = ChatColour.DARK_RED;
        } else if (wlr < 50) {
            colour = ChatColour.LIGHT_PURPLE;
        } else {
            colour = ChatColour.DARK_PURPLE;
        }
        return colour + String.valueOf(wlr);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
