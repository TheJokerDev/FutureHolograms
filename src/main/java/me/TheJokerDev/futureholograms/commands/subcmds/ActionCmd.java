package me.TheJokerDev.futureholograms.commands.subcmds;

import me.TheJokerDev.futureholograms.commands.SubCommand;
import me.TheJokerDev.futureholograms.holo.FHologram;
import me.TheJokerDev.futureholograms.holo.HologramsManager;
import me.TheJokerDev.futureholograms.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ActionCmd implements SubCommand {
    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player){
            Utils.sendMessage(sender, "messages.onlyConsole");
            return true;
        }
        if (args.length == 2){
            String var1 = args[0];
            String var2 = args[1];

            Player p = Bukkit.getPlayer(var1);
            if (p == null){
                Utils.sendMessage(sender, "messages.commands.action.playerNotExist");
                return true;
            }
            FHologram hologram = HologramsManager.getHologram(var2);
            if (HologramsManager.getHologram(var2) == null){
                Utils.sendMessage(sender, "messages.commands.action.notExist");
                return true;
            }

            hologram.onClick(p);
            Utils.sendMessage(sender, "messages.commands.action.success");
        } else {
            Utils.sendMessage(sender, help());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1){
            StringUtil.copyPartialMatches(args[0], Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()), list);
            Collections.shuffle(list);
        }
        if (args.length == 2){
            StringUtil.copyPartialMatches(args[1], Arrays.stream(HologramsManager.getHolograms()).map(FHologram::getName).collect(Collectors.toList()), list);
            Collections.shuffle(list);
        }
        return list;
    }

    @Override
    public String help() {
        return Utils.getConfig().getString("messages.commands.action.help").replace("%alias%", "fholo");
    }

    @Override
    public String getPermission() {
        return "futureholograms.console";
    }

    @Override
    public boolean console() {
        return true;
    }
}
