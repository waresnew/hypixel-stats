package com.newwares.hypixelstats.api.modes;

import com.newwares.hypixelstats.utils.ChatColour;

public abstract class SkywarsPlayer extends Player implements Translatable {
    private String level = ChatColour.GREY + "0☆";
    private String currentKit = "Default";
    private String mostUsedKit = "Default";

    public SkywarsPlayer(String uuid, String username) {
        super(uuid, username);
    }

    @Override
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCurrentKit() {
        return currentKit;
    }

    public void setCurrentKit(String currentKit) {
        this.currentKit = currentKit;
    }

    public String getMostUsedKit() {
        return mostUsedKit;
    }

    public void setMostUsedKit(String mostUsedKit) {
        this.mostUsedKit = mostUsedKit;
    }

    @Override
    public String translate() {
        return ChatColour.RESET.getColourCode() + "Kit: " + this.getCurrentKit() + ChatColour.RESET.getColourCode() + " Main kit: " + this.getMostUsedKit();
    }
}
