package com.newwares.hypixelstats;

import com.newwares.hypixelstats.config.ConfigData;
import com.newwares.hypixelstats.events.ChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.IOException;

@Mod(modid = Main.MODID, version = Main.VERSION, acceptedMinecraftVersions = "1.8.9")
public class Main {
    public static final String MODID = "hypixelstats";
    public static final String VERSION = "1.0";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws IOException {
        ConfigData.getInstance().init(event.getModConfigurationDirectory().getParentFile());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ChatReceivedEvent());
    }
}
