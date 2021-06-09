package com.newwares.hypixelstats.api.modes;

import com.newwares.hypixelstats.utils.ChatColour;

public abstract class Player implements Translatable {
    private int kills = 0;
    private int deaths = 0;
    private int wins = 0;
    private int losses = 0;
    private int level = 0;
    private int ws = 0;
    private ChatColour rankColour = ChatColour.GREY;
    private boolean nicked = false;
    private String uuid;
    private String username;
    private boolean isBot;

    public Player(String uuid, String username) {
        this.setUuid(uuid);
        this.setUsername(username);
    }

    public abstract String getLevel();

    public void setLevel(int level) {
        this.level = level;
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
            case "VIP":
            case "VIP_PLUS": {
                this.rankColour = ChatColour.GREEN;
            }

            case "MVP":
            case "MVP_PLUS": {
                this.rankColour = ChatColour.AQUA;
            }

            case "SUPERSTAR": {
                this.rankColour = ChatColour.GOLD;
            }

            case "YOUTUBER":
            case "NORMAL":
            case "ADMIN": {
                this.rankColour = ChatColour.RED;
            }

            case "MODERATOR":
            case "GAME_MASTER": {
                this.rankColour = ChatColour.DARK_GREEN;
            }
        }
    }

    public boolean isNicked() {
        return nicked;
    }

    public void setNicked(boolean nicked) {
        this.nicked = nicked;
    }

    public ChatColour getRankColour() {
        return rankColour;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        if (deaths == 0) return 1;
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        if (losses == 0) return 1;
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public String getWs() {
        ChatColour colour;
        if (ws < 1) {
            colour = ChatColour.DARK_GREY;
        } else if (ws < 50) {
            colour = ChatColour.GREY;
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

    public void setWs(int ws) {
        this.ws = ws;
    }

    public String getKdr() {
        ChatColour colour;
        double kdr = Double.parseDouble(String.format("%.2f", (double) this.getKills() / (double) this.getDeaths()));
        if (kdr < 1) {
            colour = ChatColour.DARK_GREY;
        } else if (kdr < 5) {
            colour = ChatColour.GREY;
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
        double wlr = Double.parseDouble(String.format("%.2f", (double) this.getWins() / (double) this.getLosses()));
        if (wlr < 1) {
            colour = ChatColour.DARK_GREY;
        } else if (wlr < 2) {
            colour = ChatColour.GREY;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isBot() {
        return isBot;
    }

    public void setBot(boolean bot) {
        isBot = bot;
    }
}
