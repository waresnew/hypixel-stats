package com.newwares.hypixelstats.events;

import com.google.gson.JsonObject;
import com.newwares.hypixelstats.api.HypixelApi;
import com.newwares.hypixelstats.api.MojangApi;
import com.newwares.hypixelstats.api.modes.Player;
import com.newwares.hypixelstats.config.ConfigData;
import com.newwares.hypixelstats.utils.ChatColour;
import com.newwares.hypixelstats.utils.ChatUtils;
import com.newwares.hypixelstats.utils.JsonUtils;
import com.newwares.hypixelstats.utils.StatDisplayUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatReceivedEvent {
    String mode;
    String gametype;

    public void statAll(String gametype, String mode) throws IOException, InterruptedException {
        ArrayList<Player> players = new ArrayList<>();
        List<EntityPlayer> entityPlayers = Minecraft.getMinecraft().theWorld.playerEntities;
        ArrayList<EntityPlayer> allPlayers = new ArrayList<>(entityPlayers);
        for (EntityPlayer player : allPlayers) {
            switch (mode) {
                case "ranked_normal": {
                    if (ConfigData.getInstance().isEnabledRankedSkywars()) {
                        players.add(new HypixelApi("RANKED_SKYWARS", player.getUniqueID().toString().replace("-", ""), player.getName()).setStats());
                    }
                    break;
                }
                case "teams_normal":
                case "solo_normal": {
                    if (ConfigData.getInstance().isEnabledNormalSkywars()) {
                        players.add(new HypixelApi("NORMAL_SKYWARS", player.getUniqueID().toString().replace("-", ""), player.getName()).setStats());
                    }
                    break;
                }
                case "teams_insane":
                case "solo_insane": {
                    if (ConfigData.getInstance().isEnabledInsaneSkywars()) {
                        players.add(new HypixelApi("INSANE_SKYWARS", player.getUniqueID().toString().replace("-", ""), player.getName()).setStats());
                    }
                    break;
                }
            }
            switch (gametype) {
                case "BEDWARS": {
                    if (ConfigData.getInstance().isEnabledBedwars()) {
                        players.add(new HypixelApi("BEDWARS", player.getUniqueID().toString().replace("-", ""), player.getName()).setStats());
                    }
                    break;
                }

                case "SPEED_UHC": {
                    if (ConfigData.getInstance().isEnabledSpeedUhc()) {
                        players.add(new HypixelApi("SPEED_UHC", player.getUniqueID().toString().replace("-", ""), player.getName()).setStats());
                    }
                    break;
                }
            }
        }
        StatDisplayUtils.printStats(players);
    }

    public void stat(String gametype, String mode, String username, boolean join) throws IOException, InterruptedException {
        String uuid = MojangApi.usernameToUuid(username);
        switch (mode) {
            case "ranked_normal": {
                if (ConfigData.getInstance().isEnabledRankedSkywars()) {
                    StatDisplayUtils.printStats(new HypixelApi("RANKED_SKYWARS", uuid.replace("-", ""), username).setStats(), join);
                }
                break;
            }
            case "teams_normal":
            case "solo_normal": {
                if (ConfigData.getInstance().isEnabledNormalSkywars()) {
                    StatDisplayUtils.printStats(new HypixelApi("NORMAL_SKYWARS", uuid.replace("-", ""), username).setStats(), join);
                }
                break;
            }
            case "teams_insane":
            case "solo_insane": {
                if (ConfigData.getInstance().isEnabledInsaneSkywars()) {
                    StatDisplayUtils.printStats(new HypixelApi("INSANE_SKYWARS", uuid.replace("-", ""), username).setStats(), join);
                }
                break;
            }
        }
        switch (gametype) {
            case "BEDWARS": {
                if (ConfigData.getInstance().isEnabledBedwars()) {
                    StatDisplayUtils.printStats(new HypixelApi("BEDWARS", uuid.replace("-", ""), username).setStats(), join);
                }
                break;
            }

            case "SPEED_UHC": {
                if (ConfigData.getInstance().isEnabledSpeedUhc()) {
                    StatDisplayUtils.printStats(new HypixelApi("SPEED_UHC", uuid.replace("-", ""), username).setStats(), join);
                }
                break;
            }
        }
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        new Thread(() -> {
            JsonObject jsonObject;
            String msg = event.message.getUnformattedText();
            if (msg.startsWith("{\"server\":")) {
                event.setCanceled(true);
                jsonObject = JsonUtils.parseJson(msg).getAsJsonObject();
                if (jsonObject.get("mode") != null && jsonObject.get("map") != null) {
                    mode = jsonObject.get("mode").getAsString();
                    gametype = jsonObject.get("gametype").getAsString();
                    try {
                        statAll(gametype, mode);
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
                ChatUtils.print(ChatColour.GREEN + "HypixelStats found and set api key");
            } else if (event.message.getFormattedText().contains("§r§e has quit!")) {
                if (mode != null && gametype != null) {
                    try {
                        stat(gametype, mode, event.message.getUnformattedText().substring(0, event.message.getUnformattedText().indexOf(" has quit")), false);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (event.message.getFormattedText().contains("§r§e has joined")) {
                if (mode != null && gametype != null) {
                    try {
                        String username = event.message.getUnformattedText().substring(0, event.message.getUnformattedText().indexOf(" has joined"));
                        stat(gametype, mode, username, true);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
