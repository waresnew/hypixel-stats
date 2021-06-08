package com.newwares.hypixelstats.api.modes;

import com.newwares.hypixelstats.utils.ChatColour;

public class SpeedUHCStats extends Stats {
    private String kit;
    private int level;

    public void setKit(String kit) {
        this.kit = kit;
    }

    public String getKit() {
        return kit;
    }

    @Override
    public String getLevel() {
        return ChatColour.LIGHT_PURPLE + String.valueOf(level) + "❋";
    }
}
