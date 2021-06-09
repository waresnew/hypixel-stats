package com.newwares.hypixelstats.api.modes;

public abstract class SkywarsPlayer extends Player {
    private String level;
    private String currentKit;
    private String mostUsedKit;
    public SkywarsPlayer(String uuid) {
        super(uuid);
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String getLevel() {
        return level;
    }
    public void setCurrentKit(String currentKit) {
        this.currentKit = currentKit;
    }

    public String getCurrentKit() {
        return currentKit;
    }

    public void setMostUsedKit(String mostUsedKit) {
        this.mostUsedKit = mostUsedKit;
    }

    public String getMostUsedKit() {
        return mostUsedKit;
    }
}
