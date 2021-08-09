package me.TheJokerDev.futureholograms.utils;

import me.TheJokerDev.futureholograms.Main;
import me.TheJokerDev.other.DefaultFontInfo;
import me.TheJokerDev.other.FileConfigurationUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {

    //String utils
    public static String ct(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    public static List<String> ct (List<String> list){
        return list.stream().map(Utils::ct).collect(Collectors.toList());
    }
    public static String[] ct (String... list){
        return Arrays.stream(list).map(Utils::ct).toArray(String[]::new);
    }


    public static void sendMessage(CommandSender sender, List<String> list){
        list.forEach(s->sendMessage(sender, s));
    }
    public static void sendMessage(CommandSender sender, String... list){
        Arrays.stream(list).forEach(s->sendMessage(sender, s));
    }
    public static void sendMessage(CommandSender sender, String path){
        String msg = null;
        try {
            if (getConfig().get(path)==null){
                msg = path;
            }
            if (msg == null) {
                if (isStringList(getConfig(), path)) {
                    for(String s : getConfig().getStringList(path)) {
                        sendMessage(sender, s);
                    }
                    return;
                }
                if (!path.equals("")) {
                    msg = getConfig().getString(path);
                } else {
                    msg = "";
                }
            }
        } catch (Exception e) {
            msg = path;
        }
        msg = Utils.ct(msg);
        msg = msg.replace("\\n", "\n");
        if (msg.contains("\n")){
            String[] msg2 = msg.split("\n");
            sendMessage(sender, msg2);
            return;
        }
        boolean hasPrefix = msg.contains("{prefix}");
        boolean isCentered = msg.contains("{center}");
        boolean isBroadCast = msg.contains("{broadcast}");

        if (hasPrefix){
            msg = msg.replace("{prefix}", "");
            msg = getPrefix()+msg;
        }
        if (sender instanceof Player){
            msg = PlaceholderAPI.setPlaceholders((Player)sender, msg);
        } else {
            msg = PlaceholderAPI.setPlaceholders(null, msg);
        }
        if (isCentered){
            msg = msg.replace("{center}", "");
            msg = getCenteredMSG(msg);
        }
        if (isBroadCast){
            msg = msg.replace("{broadcast}", "");
            String finalMsg = msg;
            Bukkit.getOnlinePlayers().forEach(p -> Utils.sendMessage(p, finalMsg));
            sendMessage(Bukkit.getConsoleSender(), msg);
            return;
        }

        if (sender instanceof Player){
            sender.sendMessage(msg);
        } else {
            Bukkit.getConsoleSender().sendMessage(msg);
        }
    }

    public static boolean isNumeric(String var0) {
        try {
            Integer.parseInt(var0);
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }

    public static String getPrefix(){
        return Main.getPrefix();
    }

    public static FileConfigurationUtil getFile(String fileName){
        File file = new File(Main.getPlugin().getDataFolder(), fileName);

        if (!file.exists()){
            try {
                Main.getPlugin().saveResource(fileName, false);
            } catch (Exception e) {
                try {
                    file.createNewFile();
                } catch (IOException ignored) {
                }
            }
        }

        return new FileConfigurationUtil(file);
    }

    public static boolean isStringList(FileConfigurationUtil config, String path){
        boolean b = false;
        Object var1 = config.get(path);
        if (var1 instanceof List<?>){
            b = true;
        }
        return b;
    }

    public static FileConfigurationUtil getConfig(){
        return getFile("config.yml");
    }

    public static String getCenteredMSG(String message){
        if(message == null || message.equals("")){
            return null;
        }
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
            }else if(previousCode){
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = 154 - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb + message;
    }
}
