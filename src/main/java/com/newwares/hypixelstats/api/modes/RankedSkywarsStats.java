package com.newwares.hypixelstats.api.modes;

public class RankedSkywarsStats extends SkywarsStats {
    private String currentKit;
    private String mostUsedKit;
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
