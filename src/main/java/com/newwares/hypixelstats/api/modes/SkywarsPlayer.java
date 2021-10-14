package com.newwares.hypixelstats.api.modes;

import com.newwares.hypixelstats.utils.ChatColour;

public abstract class SkywarsPlayer extends Player {
    private String skywarsLevel = ChatColour.GREY + "0☆";
    private String currentKit = "Default";

    public SkywarsPlayer(String uuid, String username) {
        super(uuid, username);
    }

    @Override
    public String getLevel() {
        return skywarsLevel;
    }


    public void setLevel(String level) {
        this.skywarsLevel = level;
    }

    public String getCurrentKit() {
        return currentKit;
    }

    public void setCurrentKit(String currentKit) {
        this.currentKit = currentKit;
    }

    @Override
    public String translate() {
        return super.translate() + ChatColour.RESET + "Kit: " + ChatColour.GREY + this.getCurrentKit() + ChatColour.RESET;
    }
}
