package me.TheJokerDev.futureholograms.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {
    boolean onCommand(CommandSender sender, String[] args);

    List<String> onTabComplete(CommandSender sender, String[] args);

    String help();

    String getPermission();

    boolean console();
}
