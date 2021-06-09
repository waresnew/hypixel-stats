package com.newwares.hypixelstats.api.modes;

import com.newwares.hypixelstats.utils.ChatColour;

public class BedwarsPlayer extends Player {
    private int finalKills;
    private int finalDeaths;
    private int bedBreaks;
    private int bedLosses;
    private int level;

    public BedwarsPlayer(String uuid) {
        super(uuid);
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String getLevel() {
        ChatColour colour;
        if (level < 100) {
            colour = ChatColour.DARK_GRAY;
        } else if (level < 200) {
            colour = ChatColour.WHITE;
        } else if (level < 300) {
            colour = ChatColour.GOLD;
        } else if (level < 400) {
            colour = ChatColour.AQUA;
        } else if (level < 500) {
            colour = ChatColour.DARK_GREEN;
        } else if (level < 600) {
            colour = ChatColour.DARK_AQUA;
        } else if (level < 700) {
            colour = ChatColour.DARK_RED;
        } else if (level < 800) {
            colour = ChatColour.LIGHT_PURPLE;
        } else if (level < 900) {
            colour = ChatColour.BLUE;
        } else if (level < 1000) {
            colour = ChatColour.DARK_PURPLE;
        } else {
            colour = ChatColour.RED;
        }
        return colour + String.valueOf(level) + "✫";
    }

    public String getBblr() {
        ChatColour colour;
        double bblr = Double.parseDouble(String.format("%.2f", (double) bedBreaks / (double) bedLosses));
        if (bblr < 1) {
            colour = ChatColour.DARK_GRAY;
        } else if (bblr < 2) {
            colour = ChatColour.GRAY;
        } else if (bblr < 4) {
            colour = ChatColour.WHITE;
        } else if (bblr < 6) {
            colour = ChatColour.GOLD;
        } else if (bblr < 7) {
            colour = ChatColour.DARK_GREEN;
        } else if (bblr < 10) {
            colour = ChatColour.RED;
        } else if (bblr < 15) {
            colour = ChatColour.DARK_RED;
        } else if (bblr < 50) {
            colour = ChatColour.LIGHT_PURPLE;
        } else {
            colour = ChatColour.DARK_PURPLE;
        }
        return colour + String.valueOf(bblr);
    }

    public void setFinalKills(int finalKills) {
        this.finalKills = finalKills;
    }

    public int getFinalKills() {
        return finalKills;
    }

    public void setFinalDeaths(int finalDeaths) {
        this.finalDeaths = finalDeaths;
    }

    public int getFinalDeaths() {
        return finalDeaths;
    }

    public void setBedBreaks(int bedBreaks) {
        this.bedBreaks = bedBreaks;
    }

    public int getBedBreaks() {
        return bedBreaks;
    }

    public void setBedLosses(int bedLosses) {
        this.bedLosses = bedLosses;
    }

    public int getBedLosses() {
        return bedLosses;
    }

    public String getFkdr() {
        ChatColour colour;
        double fkdr = Double.parseDouble(String.format("%.2f", (double) finalKills / (double) finalDeaths));
        if (fkdr < 1) {
            colour = ChatColour.DARK_GRAY;
        } else if (fkdr < 5) {
            colour = ChatColour.GRAY;
        } else if (fkdr < 10) {
            colour = ChatColour.WHITE;
        } else if (fkdr < 20) {
            colour = ChatColour.GOLD;
        } else if (fkdr < 35) {
            colour = ChatColour.DARK_GREEN;
        } else if (fkdr < 60) {
            colour = ChatColour.RED;
        } else if (fkdr < 100) {
            colour = ChatColour.DARK_RED;
        } else if (fkdr < 500) {
            colour = ChatColour.LIGHT_PURPLE;
        } else {
            colour = ChatColour.DARK_PURPLE;
        }
        return colour + String.valueOf(fkdr);
    }
}
