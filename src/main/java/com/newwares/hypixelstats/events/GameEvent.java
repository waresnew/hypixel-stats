package com.newwares.hypixelstats.events;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.utils.JsonUtils;
import com.newwares.hypixelstats.utils.StatDisplayUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameEvent {
    private World world;
    boolean fromLimbo;
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START || Minecraft.getMinecraft().theWorld == null) {
            return;
        }
        if (world != Minecraft.getMinecraft().theWorld) {
            this.world = Minecraft.getMinecraft().theWorld;
            Executors.newSingleThreadScheduledExecutor().schedule(() -> Minecraft.getMinecraft().thePlayer.sendChatMessage("/locraw"), 500, TimeUnit.MILLISECONDS);
            StatDisplayUtils.clearPlayerList();
        }
    }

}
