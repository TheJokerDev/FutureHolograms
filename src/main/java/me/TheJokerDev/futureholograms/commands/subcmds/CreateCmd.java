package me.TheJokerDev.futureholograms.commands.subcmds;

import me.TheJokerDev.futureholograms.commands.SubCommand;
import me.TheJokerDev.futureholograms.holo.FHologram;
import me.TheJokerDev.futureholograms.holo.HologramsManager;
import me.TheJokerDev.futureholograms.utils.LocationUtil;
import me.TheJokerDev.futureholograms.utils.Utils;
import me.TheJokerDev.other.FileConfigurationUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CreateCmd implements SubCommand {
    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)){
            Utils.sendMessage(sender, "messages.onlyPlayers");
            return true;
        }
        Player p = (Player)sender;
        if (args.length == 1){
            String var1 = args[0];
            FHologram var2 = HologramsManager.getHologram(var1);
            if (var2 != null){
                Utils.sendMessage(sender, "messages.commands.create.alreadyExist");
            }
            FileConfigurationUtil config = Utils.getFile("holograms.yml");
            config.add(var1+".location", LocationUtil.getString(p.getLocation(), true));
            config.add(var1+".default", "var1");
            config.add(var1+".var1.next", "var1");
            List<String> lines = new ArrayList<>();
            lines.add("&bChange this on holograms config.");
            config.add(var1+".var1.lines", lines);
            config.save();
            FHologram holo = new FHologram(var1);
            HologramsManager.hologramHashMap.put(var1, holo);
            Utils.sendMessage(sender, "messages.commands.create.success");
        } else {
            Utils.sendMessage(sender, help());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public String help() {
        return Utils.getConfig().getString("messages.commands.create.help").replace("%alias%", "fholo");
    }

    @Override
    public String getPermission() {
        return "futureholograms.admin.create";
    }

    @Override
    public boolean console() {
        return false;
    }
}
