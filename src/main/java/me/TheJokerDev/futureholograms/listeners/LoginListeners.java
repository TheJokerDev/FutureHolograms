package me.TheJokerDev.futureholograms.listeners;

import me.TheJokerDev.futureholograms.holo.FHologram;
import me.TheJokerDev.futureholograms.holo.HologramsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
            if (holo.getLocation().getWorld().getName().equals(p.getWorld().getName())){
                holo.showTo(p, p);
            } else{
                holo.hideTo(p, p);
            }
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
