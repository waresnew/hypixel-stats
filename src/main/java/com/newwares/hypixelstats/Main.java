package com.newwares.hypixelstats;

import com.newwares.hypixelstats.commands.StatCommand;
import com.newwares.hypixelstats.config.ModConfig;
import com.newwares.hypixelstats.config.NickCache;
import com.newwares.hypixelstats.config.PlayerCache;
import com.newwares.hypixelstats.handlers.GameEvent;
import com.newwares.hypixelstats.handlers.WorldSwitch;
import com.newwares.hypixelstats.hypixel.Player;
import com.newwares.hypixelstats.utils.JsonUtils;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mod(modid = Main.MODID, version = Main.VERSION, acceptedMinecraftVersions = "1.8.9", clientSideOnly = true)
public class Main {
    public static final String MODID = "hypixelstats";
    public static final String VERSION = "1.0";
    public static boolean helperLoaded = false;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws IOException {
        File minecraftFolder = event.getModConfigurationDirectory().getParentFile();
        ModConfig.getInstance().init(minecraftFolder);
        PlayerCache.getInstance().init(minecraftFolder);
        NickCache.getInstance().init(minecraftFolder);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) throws IOException {
        MinecraftForge.EVENT_BUS.register(new GameEvent());
        MinecraftForge.EVENT_BUS.register(new WorldSwitch());
        ClientCommandHandler.instance.registerCommand(new StatCommand());
        ConcurrentHashMap<String, ConcurrentHashMap<String, Player>> cache = PlayerCache.getInstance().getCache();
        ArrayList<String> toRemove = new ArrayList<>();
        for (Map.Entry<String, ConcurrentHashMap<String, Player>> entry : cache.entrySet()) {
            if (entry.getValue().isEmpty()) {
                toRemove.add(entry.getKey());
                continue;
            }
            for (Player player : entry.getValue().values()) {
                if (System.currentTimeMillis() - player.getTimeCreated() >= 4 * 24 * 60 * 60 * 1000) {
                    toRemove.add(entry.getKey());
                }
            }
        }
        toRemove.forEach(PlayerCache.getInstance().getCache()::remove);
        FileWriter fileWriter = new FileWriter(PlayerCache.getInstance().getCacheFile());
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        ConcurrentHashMap<String, ConcurrentHashMap<String, Player>> copy = new ConcurrentHashMap<>(PlayerCache.getInstance().getCache());
        JsonUtils.getGson().toJson(copy, bufferedWriter);
        bufferedWriter.close();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        helperLoaded = Loader.isModLoaded("helper");
    }
}
