package me.TheJokerDev.futureholograms.holo;

import lombok.Getter;
import lombok.Setter;
import me.TheJokerDev.futureholograms.enums.HoloActions;
import me.TheJokerDev.futureholograms.utils.Utils;
import me.TheJokerDev.other.FileConfigurationUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class FHologram {
    private Player p;
    private String name;
    private Location location;
    private List<FHoloLine> lines;
    private Location lastLineLoc;

    public FHologram(String var1){
        name = var1;

        lines = new ArrayList<>();
    }

    private void loadConfig(){
        FileConfigurationUtil config = Utils.getFile("holograms");
        boolean exist = config.get(name)!=null;
        String lines = name+".lines";
        if (!exist){
            config.add(lines+".1.text", "&bChange this line in holograms.yml ("+name+", line: "+"1");
            config.add(lines+".1.weight", 2.0D);
            List<String> actions = new ArrayList<>(Arrays.stream(HoloActions.values()).map(HoloActions::name).collect(Collectors.toList()));
            config.add(lines+".1.actions", actions);
            List<String> execute = new ArrayList<>(Arrays.asList(
                    "[sound]ENTITY_CHICKEN_EGG,10.0,1.0",
                    "[msg]&aHologram line #1 clicked!"
            ));
            config.add(lines+".1.execute", execute);
        }
        if (config.get(lines)!=null){
            for (String key : config.getSection(lines).getKeys(false)){
                String id = lines+"."+key;
                FHoloLine line = new FHoloLine(p, this, config.getSection(id));
                this.lines.add(line);
            }
        }
        if (!this.lines.isEmpty()){
            return;
        }
        spawn();
    }

    public void spawn(){

    }


}
