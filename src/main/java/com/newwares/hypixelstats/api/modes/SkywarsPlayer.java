package com.newwares.hypixelstats.api.modes;

import com.newwares.hypixelstats.utils.ChatColour;

public abstract class SkywarsPlayer extends PlayerDecorator {
    private final Player player;
    private String level = ChatColour.GREY + "0☆";
    private String currentKit = "Default";

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

    @Override
    public String translate() {
        return super.translate() + ChatColour.RESET + "Kit: " + ChatColour.GREY + this.getCurrentKit() + ChatColour.RESET;
    }
}
