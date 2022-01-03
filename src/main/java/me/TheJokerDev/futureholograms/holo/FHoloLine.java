package me.TheJokerDev.futureholograms.holo;

import lombok.Getter;
import lombok.Setter;
import me.TheJokerDev.futureholograms.Main;
import me.TheJokerDev.futureholograms.enums.HoloActions;
import me.TheJokerDev.futureholograms.utils.Utils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FHoloLine {
    private FHologram hologram;
    private Player player;
    private String text;
    private double weight = 2.0D;
    private List<HoloActions> actions;
    private List<String> execute;
    private ArmorStand entity;


    public FHoloLine(Player p, FHologram holo, ConfigurationSection id) {
        player = p;
        hologram = holo;
        text = id.getString("text");
        weight = id.getDouble("weight");

        actions = new ArrayList<>();
        execute = new ArrayList<>();

        if (id.get("actions")!=null){
            for (String s : id.getStringList("actions")){
                HoloActions holoact = HoloActions.valueOf(s.toUpperCase());
                if (!actions.contains(holoact)) {
                    actions.add(holoact);
                }
            }
        }
        if (id.get("execute")!=null){
            execute.addAll(id.getStringList("execute"));
        }

        createEntity();
    }

    public String getText(){
        return PlaceholderAPI.setPlaceholders(player, Utils.ct(text));
    }

    private void createEntity(){
        if (hologram.getLastLineLoc()== null){
            hologram.setLastLineLoc(hologram.getLocation());
        }
        Location loc = hologram.getLastLineLoc();
        loc.add(0, weight, 0);
        if (!loc.isWorldLoaded() || loc.getWorld() == null){
            Main.log(2, "World on hologram line is not loaded or not exist");
            return;
        }
        entity = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);

        entity.setSmall(false);
        entity.setInvisible(true);
        entity.setInvulnerable(true);

        entity.setArms(false);
        entity.setBasePlate(false);

        entity.setCollidable(false);
        entity.setGravity(false);

        entity.setCustomName(getText());
        entity.setCustomNameVisible(true);

        hologram.setLastLineLoc(loc);

    }

    public void deleteEntity(){
        entity.remove();
    }


}
