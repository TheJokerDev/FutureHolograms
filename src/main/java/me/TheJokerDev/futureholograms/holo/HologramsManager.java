package me.TheJokerDev.futureholograms.holo;

import me.TheJokerDev.futureholograms.enums.HoloActions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class HologramsManager {
    public HashMap<Player, List<FHologram>> manager;


    public void init(){
        manager = new HashMap<>();
    }


    public List<FHologram> getHolograms(Player p){
        return manager.getOrDefault(p, null);
    }

    public void onClick (Player p, int id, HoloActions action){

    }

}
