package com.newwares.hypixelstats.api.modes;

import com.newwares.hypixelstats.utils.ChatColour;

public abstract class SkywarsPlayer extends PlayerDecorator implements Translatable {
    private final Player player;
    private String level = ChatColour.GREY + "0☆";
    private String currentKit = "Default";
    private String mostUsedKit = "Default";

    public SkywarsPlayer(Player player) {
        super(player.getUuid(), player.getUsername());
        this.player = player;
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
        return player.translate() + ChatColour.RESET.getColourCode() + "AK: " + ChatColour.GREY.getColourCode() + this.getCurrentKit() + ChatColour.RESET.getColourCode() + " MK: " + ChatColour.GREY.getColourCode() + this.getMostUsedKit() + ChatColour.RESET.getColourCode();
    }
}
