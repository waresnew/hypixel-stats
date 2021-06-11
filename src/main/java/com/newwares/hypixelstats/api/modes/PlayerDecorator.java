package com.newwares.hypixelstats.api.modes;

public abstract class PlayerDecorator extends Player {
    public PlayerDecorator(String uuid, String username) {
        super(uuid, username);
    }

    public abstract String getLevel();
}
