package me.TheJokerDev.futureholograms.commands.subcmds;

import me.TheJokerDev.futureholograms.commands.SubCommand;
import me.TheJokerDev.futureholograms.holo.FHologram;
import me.TheJokerDev.futureholograms.holo.HologramsManager;
import me.TheJokerDev.futureholograms.utils.Utils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ListCmd implements SubCommand {
    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length == 0){
            sendList(sender, "fholo");
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
        return Utils.getConfig().getString("messages.commands.list.help").replace("%alias%", "fholo");
    }

    @Override
    public String getPermission() {
        return "futureholograms.admin.list";
    }

    void sendList(CommandSender cmd, String label){

        List<String> help = Utils.getConfig().getStringList("messages.commands.list.format");
        String format = Utils.getConfig().getString("messages.commands.list.hologramFormat");
        List<String> var1 = new ArrayList<>();

        for (String s : help) {
            if (s.equalsIgnoreCase("{list}")) {
                for (FHologram subcmd : HologramsManager.getHolograms()){
                    var1.add(format.replaceAll("%name%", subcmd.getName())
                    .replaceAll("%location%", subcmd.getFormattedLocation()));
                }
            } else {
                var1.add(s);
            }
        }
        Utils.sendMessage(cmd, var1);
    }

    @Override
    public boolean console() {
        return true;
    }
}
