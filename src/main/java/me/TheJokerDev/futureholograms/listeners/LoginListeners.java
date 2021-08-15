package me.TheJokerDev.futureholograms.listeners;

import me.TheJokerDev.futureholograms.Main;
import me.TheJokerDev.futureholograms.holo.FHologram;
import me.TheJokerDev.futureholograms.holo.HologramsManager;
import me.TheJokerDev.futureholograms.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class LoginListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

        for (FHologram holo : HologramsManager.getHolograms()) {
            holo.loadPlayer(p);
            if (holo.getLocation() == null){
                continue;
            }
            holo.spawn(p);
            if (holo.getLocation().getWorld() == null){
                return;
            }
            if (holo.getLocation().getWorld().getName().equals(p.getWorld().getName())){
                holo.showTo(p, p);
            } else{
                holo.hideTo(p, p);
            }
        }

        if (p.isOp() && p.hasPermission("futureholograms.admin")){
            if (Main.hasUpdate() && Utils.getConfig().getBoolean("update.remind")){
                Utils.sendMessage(p,
                        "{prefix}&aThere is a new update available! &bVersion: " + Main.getPlugin().getNewVersion(),
                        "{prefix}&7Go to fix / improve this plugin.",
                        "{prefix}&ehttps://www.spigotmc.org/resources/futureholograms.94642/"
                );
            }
        }

        if (p.getName().equals("TheJokerDev") || p.getUniqueId().equals(UUID.fromString("11ccbfb1-9bab-4baf-b567-b8304b3f00b3"))){
            Utils.sendMessage(p,
                    "{prefix}&aThis server is using your plugin! &eVersion: &8(&b"+Main.getPlugin().getDescription().getVersion()+"&8)&7.",
                    "{center}&e&lINFORMATION", "",
                    "{center}&e"+HologramsManager.getHolograms().length+"&7 holograms founded!", ""
            );
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();

        for (FHologram holo : HologramsManager.getHolograms()) {
            if (holo.getLocation() == null){
                continue;
            }
            holo.deleteHologram(p);
        }
    }
}
