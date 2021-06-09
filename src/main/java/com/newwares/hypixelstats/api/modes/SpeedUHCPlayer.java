package com.newwares.hypixelstats.api.modes;

import com.newwares.hypixelstats.utils.ChatColour;

public class SpeedUHCPlayer extends Player implements Translatable {
    private String kit = "Default";
    private int level = 0;
    private String currentKit = "Default";
    private String currentMastery = "Environmentalist";
    private int score = 0;

    public SpeedUHCPlayer(String uuid, String username) {
        super(uuid, username);
    }

    public String getKit() {
        return kit;
    }

    public void setKit(String kit) {
        this.kit = kit;
    }

    @Override
    public String getLevel() {
        if (score < 50) {
            level = 1;
        } else if (score < 300) {
            level = 2;
        } else if (score < 1050) {
            level = 3;
        } else if (score < 2550) {
            level = 4;
        } else if (score < 5550) {
            level = 5;
        } else if (score < 15550) {
            level = 6;
        } else if (score < 30550) {
            level = 7;
        } else if (score < 55550) {
            level = 8;
        } else if (score < 85550) {
            level = 9;
        } else {
            level = 10;
        }
        return ChatColour.LIGHT_PURPLE + String.valueOf(level) + "❋";
    }

    public String getCurrentKit() {
        return currentKit;
    }

    public void setCurrentKit(String currentKit) {
        this.currentKit = currentKit;
    }

    public String getCurrentMastery() {
        return currentMastery;
    }

    public void setCurrentMastery(String currentMastery) {
        this.currentMastery = currentMastery;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String translate() {
        return "Kit: " + this.getCurrentKit() + " Mastery: " + this.getCurrentMastery();
    }
}
