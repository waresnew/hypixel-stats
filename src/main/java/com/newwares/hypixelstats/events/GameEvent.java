package com.newwares.hypixelstats.events;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GameEvent {
    private World world;
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START || Minecraft.getMinecraft().theWorld != null && world != Minecraft.getMinecraft().theWorld) {
            this.world = Minecraft.getMinecraft().theWorld;
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/locraw");
        }
    }
}
