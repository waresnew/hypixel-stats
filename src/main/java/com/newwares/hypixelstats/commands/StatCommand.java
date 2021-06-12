package com.newwares.hypixelstats.commands;

import com.newwares.hypixelstats.api.MojangApi;
import com.newwares.hypixelstats.utils.ChatColour;
import com.newwares.hypixelstats.utils.ChatUtils;
import com.newwares.hypixelstats.utils.StatDisplayUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.io.IOException;

public class StatCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "stat";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/stat <bw/nsw/isw/rsw/uhc> <username>";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] args) {
        new Thread(() -> {
            if (args == null || args[0] == null || args[1] == null) return;
            try {
                String username = args[1];
                String uuid;
                uuid = MojangApi.usernameToUuid(username);
                if (uuid != null) {
                    switch (args[0]) {
                        case "bw": {
                            StatDisplayUtils.stat("BEDWARS", "a", uuid, username, true);
                            break;
                        }
                        case "nsw": {
                            StatDisplayUtils.stat("SKYWARS", "solo_normal", uuid, username, true);
                            break;
                        }
                        case "isw": {
                            StatDisplayUtils.stat("SKYWARS", "solo_insane", uuid, username, true);
                            break;
                        }
                        case "rsw": {
                            StatDisplayUtils.stat("SKYWARS", "ranked_normal", uuid, username, true);
                            break;
                        }
                        case "uhc": {
                            StatDisplayUtils.stat("SPEED_UHC", "a", uuid, username, true);
                            break;
                        }
                    }
                } else {
                    ChatUtils.print(ChatColour.RED.getColourCode() + username + " is nicked!");
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
