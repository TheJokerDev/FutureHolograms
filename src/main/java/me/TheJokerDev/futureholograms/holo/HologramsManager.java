package me.TheJokerDev.futureholograms.holo;

import me.TheJokerDev.futureholograms.utils.Utils;
import me.TheJokerDev.other.FileConfigurationUtil;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class HologramsManager {
    public static HashMap<String, FHologram> hologramHashMap = new HashMap<>();

    public static void initHolograms(){

        if (!hologramHashMap.isEmpty()){
            for (FHologram hologram : hologramHashMap.values()){
                hologram.deleteAll();
            }
        }

        hologramHashMap = new HashMap<>();

        FileConfigurationUtil config = Utils.getFile("holograms.yml");

        for (String key : config.getKeys(false)){
            FHologram hologram = new FHologram(key);

            hologramHashMap.put(key, hologram);
        }
    }

    public static FHologram getHologram(String s){
        return hologramHashMap.getOrDefault(s, null);
    }

    public static FHologram[] getHolograms() {
        return hologramHashMap.values().toArray(new FHologram[0]);
    }
}
