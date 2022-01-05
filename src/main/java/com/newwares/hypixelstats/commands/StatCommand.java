package com.newwares.hypixelstats.commands;

import com.newwares.hypixelstats.config.NickCache;
import com.newwares.hypixelstats.config.PlayerCache;
import com.newwares.hypixelstats.utils.ChatColour;
import com.newwares.hypixelstats.utils.ChatUtils;
import com.newwares.hypixelstats.utils.MojangApi;
import com.newwares.hypixelstats.utils.StatDisplayUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

public class StatCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "hypixelstats";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/hypixelstats <bw/nsw/isw/rsw/uhc> <username> [refresh] or /hypixelstats nicks <nick>";
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
            return getListOfStringsMatchingLastWord(args, Arrays.asList("bw", "nsw", "isw", "rsw", "uhc", "nicks"));
        } else if (args.length == 3) {
            return getListOfStringsMatchingLastWord(args, Collections.singletonList("refresh"));
        }
        return null;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] args) {
        new Thread(() -> {
            if (args == null || args[0] == null || args[1] == null) return;
            try {
                String username = args[1];
                if (args[0].equals("nicks")) {
                    TreeMap<String, Long> uuids = NickCache.getInstance().getCache(username);
                    StringBuilder uuidList = new StringBuilder();
                    if (uuids == null) {
                        ChatUtils.print(ChatColour.RED+"No usernames found.");
                        return;
                    }
                    if (!uuids.isEmpty()) {
                        for (Map.Entry<String, Long> uuid : uuids.entrySet()) {
                            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm");
                            uuidList.append(ChatColour.RED).append(MojangApi.uuidToUsername(uuid.getKey())).append(" ").append(format.format(new Date(uuid.getValue()))).append("\n");
                        }
                    }
                    ChatUtils.print(uuidList.toString());
                    return;
                }
                String uuid;
                uuid = MojangApi.usernameToUuid(username);
                if (uuid != null) {
                    if (args.length == 3 && args[2].equals("refresh")) {
                        PlayerCache.getInstance().removePlayer(uuid);
                    }
                    switch (args[0]) {
                        case "bw": {
                            StatDisplayUtils.printStats(StatDisplayUtils.stat("BEDWARS", "a", uuid, username));
                            break;
                        }
                        case "nsw": {
                            StatDisplayUtils.printStats(StatDisplayUtils.stat("SKYWARS", "solo_normal", uuid, username));
                            break;
                        }
                        case "isw": {
                            StatDisplayUtils.printStats(StatDisplayUtils.stat("SKYWARS", "solo_insane", uuid, username));
                            break;
                        }
                        case "rsw": {
                            StatDisplayUtils.printStats(StatDisplayUtils.stat("SKYWARS", "ranked_normal", uuid, username));
                            break;
                        }
                        case "uhc": {
                            StatDisplayUtils.printStats(StatDisplayUtils.stat("SPEED_UHC", "a", uuid, username));
                            break;
                        }
                    }
                } else {
                    ChatUtils.print(ChatColour.RED + username + " is nicked!");
                }
            } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
