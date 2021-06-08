package com.newwares.hypixelstats.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatUtils {

    public static void print(String msg) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(msg));
    }
}
