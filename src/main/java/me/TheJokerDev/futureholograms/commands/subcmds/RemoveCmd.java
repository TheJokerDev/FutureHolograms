package me.TheJokerDev.futureholograms.commands.subcmds;

import me.TheJokerDev.futureholograms.commands.SubCommand;
import me.TheJokerDev.futureholograms.holo.FHologram;
import me.TheJokerDev.futureholograms.holo.HologramsManager;
import me.TheJokerDev.futureholograms.utils.LocationUtil;
import me.TheJokerDev.futureholograms.utils.Utils;
import me.TheJokerDev.other.FileConfigurationUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveCmd implements SubCommand {
    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length == 1){
            String var1 = args[0];
            FHologram var2 = HologramsManager.getHologram(var1);
            if (var2 == null){
                Utils.sendMessage(sender, "messages.commands.remove.notExist");
                return true;
            }
            Utils.getFile("holograms.yml").set(var1, null);
            var2.deleteAll();
            HologramsManager.hologramHashMap.remove(var1);
            Utils.sendMessage(sender, "messages.commands.remove.success");
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
            List<String> list2 = new ArrayList<>();

            StringUtil.copyPartialMatches(var1, list1, list2);
            Collections.shuffle(list2);
            return list2;
        }
        return null;
    }

    @Override
    public String help() {
        return Utils.getConfig().getString("messages.commands.remove.help").replace("%alias%", "fholo");
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean console() {
        return false;
    }
}
