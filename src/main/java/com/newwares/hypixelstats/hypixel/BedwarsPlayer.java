package com.newwares.hypixelstats.hypixel;

import com.newwares.hypixelstats.utils.ChatColour;

public class BedwarsPlayer extends Player {
    private int finalKills = 0;
    private int finalDeaths = 0;
    private int bedBreaks = 0;
    private int bedLosses = 0;

    public BedwarsPlayer(String uuid, String username, GameMode mode) {
        super(uuid, username, mode);
    }


    @Override
    public String translate() {
        return super.translate() + "FKDR: " + this.getFkdr() + ChatColour.RESET + " BBLR: " + this.getBblr() + ChatColour.RESET;
    }

    @Override
    public String getLevel() {
        ChatColour colour;
        if (level < 100) {
            colour = ChatColour.DARK_GREY;
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
        double bblr = Double.parseDouble(String.format("%.2f", (double) this.getBedBreaks() / (double) this.getBedLosses()));
        if (bblr < 1) {
            colour = ChatColour.DARK_GREY;
        } else if (bblr < 2) {
            colour = ChatColour.GREY;
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

    public int getFinalKills() {
        return finalKills;
    }

    public void setFinalKills(int finalKills) {
        this.finalKills = finalKills;
    }

    public int getFinalDeaths() {
        if (finalDeaths == 0) return 1;
        return finalDeaths;
    }

    public void setFinalDeaths(int finalDeaths) {
        this.finalDeaths = finalDeaths;
    }

    public int getBedBreaks() {
        return bedBreaks;
    }

    public void setBedBreaks(int bedBreaks) {
        this.bedBreaks = bedBreaks;
    }

    public int getBedLosses() {
        if (bedLosses == 0) return 1;
        return bedLosses;
    }

    public void setBedLosses(int bedLosses) {
        this.bedLosses = bedLosses;
    }

    public String getFkdr() {
        ChatColour colour;
        double fkdr = Double.parseDouble(String.format("%.2f", (double) this.getFinalKills() / (double) this.getFinalDeaths()));
        if (fkdr < 1) {
            colour = ChatColour.DARK_GREY;
        } else if (fkdr < 5) {
            colour = ChatColour.GREY;
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
