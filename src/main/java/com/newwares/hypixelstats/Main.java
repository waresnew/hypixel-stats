package com.newwares.hypixelstats;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Main.MODID, version = Main.VERSION, acceptedMinecraftVersions = "1.8.9")
public class Main {
    public static final String MODID = "hypixelstats";
    public static final String VERSION = "1.0";

    @EventHandler
    public void init(FMLInitializationEvent event) {

    }
}
