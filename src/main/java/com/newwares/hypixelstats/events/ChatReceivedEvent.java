package com.newwares.hypixelstats.events;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.api.HypixelApi;
import com.newwares.hypixelstats.api.MojangApi;
import com.newwares.hypixelstats.config.ConfigData;
import com.newwares.hypixelstats.utils.ChatColour;
import com.newwares.hypixelstats.utils.ChatUtils;
import com.newwares.hypixelstats.utils.JsonUtils;
import com.newwares.hypixelstats.utils.StatDisplayUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;

public class ChatReceivedEvent {
    String mode;
    String gametype;

    public void stat(String gametype, String mode, String uuid, String username, boolean join) throws IOException, InterruptedException {

        switch (gametype) {
            case "BEDWARS": {
                if (ConfigData.getInstance().isEnabledBedwars()) {
                    StatDisplayUtils.printStats(new HypixelApi("BEDWARS", uuid.replace("-", ""), username).setStats(), join);
                }

            }

            case "SPEED_UHC": {
                if (ConfigData.getInstance().isEnabledSpeedUhc()) {
                    StatDisplayUtils.printStats(new HypixelApi("SPEED_UHC", uuid.replace("-", ""), username).setStats(), join);
                }

            }

            case "SKYWARS": {
                switch (mode) {
                    case "ranked_normal": {
                        if (ConfigData.getInstance().isEnabledRankedSkywars()) {
                            StatDisplayUtils.printStats(new HypixelApi("RANKED_SKYWARS", uuid.replace("-", ""), username).setStats(), join);
                        }

                    }
                    case "teams_normal":
                    case "solo_normal": {
                        if (ConfigData.getInstance().isEnabledNormalSkywars()) {
                            StatDisplayUtils.printStats(new HypixelApi("NORMAL_SKYWARS", uuid.replace("-", ""), username).setStats(), join);
                        }

                    }
                    case "teams_insane":
                    case "solo_insane": {
                        if (ConfigData.getInstance().isEnabledInsaneSkywars()) {
                            StatDisplayUtils.printStats(new HypixelApi("INSANE_SKYWARS", uuid.replace("-", ""), username).setStats(), join);
                        }

                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        new Thread(() -> {
            JsonObject jsonObject;
            String msg = event.message.getUnformattedText();
            if (msg.startsWith("{\"server\":")) {
                //  event.setCanceled(true);
                jsonObject = JsonUtils.parseJson(msg).getAsJsonObject();
                if (jsonObject.get("mode") != null && jsonObject.get("map") != null) {
                    mode = jsonObject.get("mode").getAsString();
                    gametype = jsonObject.get("gametype").getAsString();
                    try {
                        for (NetworkPlayerInfo playerInfo : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
                            //TODO concurrentmodificationexception
                            stat(gametype, mode, playerInfo.getGameProfile().getId().toString(), playerInfo.getGameProfile().getName(), true);
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (msg.startsWith("Your new API key is")) {
                try {
                    ConfigData.getInstance().setApiKey(msg.replace("Your new API key is ", ""));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ChatUtils.print(ChatColour.GREEN.getColourCode() + "HypixelStats found and set api key");
            } else if (event.message.getFormattedText().contains("§r§e has quit!")) {
                if (mode != null && gametype != null) {
                    try {
                        String username = event.message.getUnformattedText().substring(0, event.message.getUnformattedText().indexOf(" has quit"));
                        stat(gametype, mode, MojangApi.usernameToUuid(username), username, false);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (event.message.getFormattedText().contains("§r§e has joined")) {
                if (mode != null && gametype != null) {
                    try {
                        String username = event.message.getUnformattedText().substring(0, event.message.getUnformattedText().indexOf(" has joined"));
                        stat(gametype, mode, MojangApi.usernameToUuid(username), username, true);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
