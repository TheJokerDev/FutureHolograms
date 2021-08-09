package me.TheJokerDev.futureholograms.commands.subcmds;

import me.TheJokerDev.futureholograms.Main;
import me.TheJokerDev.futureholograms.commands.SubCommand;
import me.TheJokerDev.futureholograms.holo.FHologram;
import me.TheJokerDev.futureholograms.holo.HologramsManager;
import me.TheJokerDev.futureholograms.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ReloadCmd implements SubCommand {
    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length == 0){
            if (HologramsManager.getHolograms().length == 0) {
                Utils.sendMessage(sender, "messages.commands.reload.noHolograms");
                return true;
            }
            HologramsManager.initHolograms();
            Utils.sendMessage(sender, "messages.commands.reload.success");
        } else if (args.length == 1){
            String var1 = args[0];
            if (var1.equalsIgnoreCase("config")){
                Main.getPlugin().saveDefaultConfig();
                Main.getPlugin().saveConfig();
                Main.getPlugin().reloadConfig();
                Utils.sendMessage(sender, "messages.commands.reload.config");
                return true;
            }
            FHologram holo = HologramsManager.getHologram(var1);
            if (holo == null){
                Utils.sendMessage(sender, "messages.commands.reload.notExist");
                return true;
            }
            holo.reload();
            Utils.sendMessage(sender, "messages.commands.reload.success2");
        } else {
            Utils.sendMessage(sender, help());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1){
            String var1 = args[0];
            List<String> list1 = Arrays.stream(HologramsManager.getHolograms()).map(FHologram::getName).collect(Collectors.toList());
            list1.add("config");
            List<String> list2 = new ArrayList<>();

            StringUtil.copyPartialMatches(var1, list1, list2);
            Collections.shuffle(list2);
            return list2;
        }
        return null;
    }

    @Override
    public String help() {
        return Utils.getConfig().getString("messages.commands.reload.help").replace("%alias%", "fholo");
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean console() {
        return true;
    }
}
