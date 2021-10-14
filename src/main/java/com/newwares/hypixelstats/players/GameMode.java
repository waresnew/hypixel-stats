package com.newwares.hypixelstats.players;

import com.newwares.hypixelstats.players.factories.*;

public enum GameMode {
    BEDWARS(BedwarsPlayer.class, BedwarsPlayerFactory.class),
    NORMAL_SKYWARS(NormalSkywarsPlayer.class, NormalSkywarsPlayerFactory.class),
    INSANE_SKYWARS(InsaneSkywarsPlayer.class, InsaneSkywarsPlayerFactory.class),
    SPEED_UHC(SpeedUHCPlayer.class, SpeedUHCPlayerFactory.class),
    RANKED_SKYWARS(RankedSkywarsPlayer.class, RankedSkywarsPlayerFactory.class);

    private final Class<? extends Player> type;
    private final Class<? extends PlayerFactory> factory;

    GameMode(Class<? extends Player> type, Class<? extends PlayerFactory> factory) {
        this.type = type;
        this.factory = factory;
    }

    /**
     * @return the Player class associated with this gamemode
     */
    public Class<? extends Player> getType() {
        return type;
    }

    /**
     * @return the PlayerFactory class associated with this gamemode
     */
    public Class<? extends PlayerFactory> getFactory() {
        return factory;
    }
}
