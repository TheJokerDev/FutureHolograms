package me.TheJokerDev.futureholograms.listeners;

import me.TheJokerDev.futureholograms.holo.FHologram;
import me.TheJokerDev.futureholograms.holo.HologramsManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldListeners implements Listener {

    @EventHandler
    public void onWorldChange (PlayerChangedWorldEvent e){
        Player p = e.getPlayer();
        World w = e.getPlayer().getWorld();

        for (FHologram holo : HologramsManager.getHolograms()){
            if (holo.getHologram(p).isDeleted()){
                return;
            }
            if (holo.getHologram(p) == null){
                return;
            }
            if (holo.getLocation().getWorld().getName().equals(w.getName())){
                holo.showTo(p, p);
            } else{
                holo.hideTo(p, p);
            }
        }
    }
}
