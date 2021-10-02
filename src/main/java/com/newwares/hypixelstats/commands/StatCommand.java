package com.newwares.hypixelstats.commands;

import com.newwares.hypixelstats.api.MojangApi;
import com.newwares.hypixelstats.utils.ChatColour;
import com.newwares.hypixelstats.utils.ChatUtils;
import com.newwares.hypixelstats.utils.StatDisplayUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "hypixelstats";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/hypixelstats <bw/nsw/isw/rsw/uhc> <username>";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("hs");
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            ArrayList<String> list = new ArrayList<>();
            list.add("bw");
            list.add("nsw");
            list.add("isw");
            list.add("rsw");
            list.add("uhc");
            Collections.sort(list);
            return getListOfStringsMatchingLastWord(args, list);
        }
        return null;
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
                    ChatUtils.print(ChatColour.RED + username + " is nicked!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
