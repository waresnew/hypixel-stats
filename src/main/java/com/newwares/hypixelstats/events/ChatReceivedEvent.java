package com.newwares.hypixelstats.events;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.utils.ChatUtils;
import com.newwares.hypixelstats.utils.JsonUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatReceivedEvent {
    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();
        if (msg.startsWith("{\"server\":")) {
            event.setCanceled(true);
            JsonObject jsonObject = JsonUtils.parseJson(msg).getAsJsonObject();
            if (jsonObject.get("mode") != null && jsonObject.get("map") != null) {
                if (jsonObject.get("mode").getAsString().equals("ranked_normal")) {

                } else {
                    switch (jsonObject.get("gametype").getAsString()) {
                        case "SKYWARS": {

                        }

                        case "BEDWARS": {

                        }

                        case "SPEED_UHC": {

                        }
                    }
                }
                System.out.println(jsonObject);
                ChatUtils.print(jsonObject.get("gametype").getAsString());
            }
        }
    }
}
