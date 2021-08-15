package me.TheJokerDev.futureholograms;

import me.TheJokerDev.futureholograms.commands.FHologramsCmd;
import me.TheJokerDev.futureholograms.holo.FHologram;
import me.TheJokerDev.futureholograms.holo.HologramsManager;
import me.TheJokerDev.futureholograms.listeners.LoginListeners;
import me.TheJokerDev.futureholograms.listeners.WorldListeners;
import me.TheJokerDev.futureholograms.utils.Utils;
import me.TheJokerDev.other.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public final class Main extends JavaPlugin {
    private static Main plugin;
    private static boolean papiLoaded = true;
    private static boolean hasUpdate;
    private String newVersion = "";

    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;
        long ms = System.currentTimeMillis();
        log(0, "{prefix}&7Loading plugin...");
        PluginManager pm = getServer().getPluginManager();

        log(0, "{prefix}&7Checking dependencies...");
        if (!pm.isPluginEnabled("PlaceholderAPI")){
            log(1, "&cYou need to install PlaceholderAPI to work.");
            papiLoaded = false;
            pm.disablePlugin(this);
            return;
        } else if (!pm.isPluginEnabled("HolographicDisplays")){
            log(1, "&cYou need to force install HolographicDisplays to work.");
            pm.disablePlugin(this);
            return;
        }
        log(0, "{prefix}&aDependencies checked and hooked!");

        log(0, "{prefix}&7Loading commands...");
        getCommand("futureholograms").setExecutor(new FHologramsCmd());
        getCommand("futureholograms").setTabCompleter(new FHologramsCmd());
        log(0, "{prefix}&aCommands loaded!");

        log(0, "{prefix}&7Loading listeners...");
        listeners(new LoginListeners(), new WorldListeners());
        log(0, "{prefix}&aListeners loaded sucessfully!");

        ms = System.currentTimeMillis()-ms;
        Utils.sendMessage(Bukkit.getConsoleSender(),
                "{prefix}&aPlugin fully loaded and started!",
                "&b&l=========================================",
                "&fThanks to use my plugin. Plugin loaded in",
                "&f"+ms/1000+" seconds.",
                "",
                "&a    Made with love, by TheJokerDev &c<3",
                "&b&l========================================="
        );

        if (getConfig().getBoolean("update.checkUpdates")) {
            new UpdateChecker(this, 94642).getVersion(version -> {
                if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                    hasUpdate = false;
                    Main.log(0, "{prefix}&eYou have the latest version of &aFuture&2Holograms!");
                } else {
                    hasUpdate = true;
                    newVersion = version;
                    Main.log(0, "{prefix}&aThere is a new update available! &bVersion: " + version);
                    Main.log(0, "{prefix}&7Go to fix / improve this plugin.");
                    Main.log(0, "{prefix}&ehttps://www.spigotmc.org/resources/futureholograms.94642/");
                }
            });
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                log(0, "{prefix}&7Loading holograms...");
                HologramsManager.initHolograms();
                log(0, "{prefix}&aHolograms loaded!");
            }
        }.runTask(this);
    }

    public String getNewVersion() {
        return newVersion;
    }

    public static void listeners(Listener... list){
        Arrays.stream(list).forEach(l-> Bukkit.getPluginManager().registerEvents(l, getPlugin()));
    }

    public static boolean hasUpdate() {
        return hasUpdate;
    }

    public static void log(int mode, String msg){
        if (mode == 0){
            Utils.sendMessage(Bukkit.getConsoleSender(), msg);
        } else if (mode == 1){
            Utils.sendMessage(Bukkit.getConsoleSender(), "&c&lError: &7"+msg);
        } else if (mode == 2){
            if (getPlugin().getConfig().getBoolean("debug")) {
                Utils.sendMessage(Bukkit.getConsoleSender(), "&e&lDebug: &7" + msg);
            }
        }
    }

    public static boolean isPapiLoaded() {
        return papiLoaded;
    }

    public static Main getPlugin() {
        return plugin;
    }

    public static String getPrefix(){
        return Utils.getConfig().getString("prefix");
    }

    @Override
    public void onDisable() {
        log(0, "{prefix}&7Disabling holograms...");
        for (FHologram holo : HologramsManager.getHolograms()){
            holo.deleteAll();
            HologramsManager.hologramHashMap.remove(holo.getName());
        }
        log(0, "{prefix}&cHolograms disabled!");
        log(0, "{prefix}&cDisabling plugin...");
    }
}
