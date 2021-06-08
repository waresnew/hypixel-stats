package com.newwares.hypixelstats.api.modes;

import com.newwares.hypixelstats.utils.ChatColour;

public abstract class SkywarsStats extends Stats {
    private int level;

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String getLevel() {
        ChatColour colour;
        if (level < 5) {
            colour = ChatColour.DARK_GRAY;
        } else if (level < 10) {
            colour = ChatColour.WHITE;
        } else if (level < 15) {
            colour = ChatColour.GOLD;
        } else if (level < 20) {
            colour = ChatColour.AQUA;
        } else if (level < 25) {
            colour = ChatColour.DARK_GREEN;
        } else if (level < 30) {
            colour = ChatColour.DARK_AQUA;
        } else if (level < 35) {
            colour = ChatColour.DARK_RED;
        } else if (level < 40) {
            colour = ChatColour.LIGHT_PURPLE;
        } else if (level < 45) {
            colour = ChatColour.DARK_BLUE;
        } else if (level < 50) {
            colour = ChatColour.DARK_PURPLE;
        } else {
            colour = ChatColour.RED;
        }
        return colour + String.valueOf(level) + "✫";
    }
}
