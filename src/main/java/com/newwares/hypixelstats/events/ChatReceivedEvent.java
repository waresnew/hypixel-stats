package com.newwares.hypixelstats.events;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.api.HypixelApi;
import com.newwares.hypixelstats.config.ConfigData;
import com.newwares.hypixelstats.utils.ChatColour;
import com.newwares.hypixelstats.utils.ChatUtils;
import com.newwares.hypixelstats.utils.JsonUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;

public class ChatReceivedEvent {
    public void stat(JsonObject jsonObject) {
        for (EntityPlayer player : Minecraft.getMinecraft().theWorld.playerEntities) {
            switch (jsonObject.get("mode").getAsString()) {
                case "ranked_normal": {
                    new HypixelApi("RANKED_SKYWARS", player.getUniqueID().toString().replace("-", "")).setStats();
                }

                case "teams_normal":
                case "solo_normal": {
                    new HypixelApi("NORMAL_SKYWARS", player.getUniqueID().toString().replace("-", "")).setStats();
                }

                case "teams_insane":
                case "solo_insane": {
                    new HypixelApi("INSANE_SKYWARS", player.getUniqueID().toString().replace("-", "")).setStats();
                }
            }
            switch (jsonObject.get("gametype").getAsString()) {
                case "BEDWARS": {
                    if (ConfigData.getInstance().isEnabledBedwars()) {
                        new HypixelApi("BEDWARS", player.getUniqueID().toString().replace("-", "")).setStats();
                    }
                }

                case "SPEED_UHC": {
                    if (ConfigData.getInstance().isEnabledSpeedUhc()) {
                        new HypixelApi("SPEED_UHC", player.getUniqueID().toString().replace("-", "")).setStats();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) throws IOException {
        String msg = event.message.getUnformattedText();
        if (msg.startsWith("{\"server\":")) {
            event.setCanceled(true);
            JsonObject jsonObject = JsonUtils.parseJson(msg).getAsJsonObject();
            if (jsonObject.get("mode") != null && jsonObject.get("map") != null) {
                stat(jsonObject);
            }
        } else if (msg.startsWith("Your new API key is")) {
            ConfigData.getInstance().setApiKey(msg.replace("Your new API key is ", ""));
            ChatUtils.print(ChatColour.GREEN + "HypixelStats found and set api key");
        } else if (event.message.getFormattedText().contains("§r§e has quit!")) {

        }
    }
}
