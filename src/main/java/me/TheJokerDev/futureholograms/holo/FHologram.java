package me.TheJokerDev.futureholograms.holo;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.gmail.filoghost.holographicdisplays.api.line.TouchableLine;
import me.TheJokerDev.futureholograms.Main;
import me.TheJokerDev.futureholograms.utils.LocationUtil;
import me.TheJokerDev.futureholograms.utils.Utils;
import me.TheJokerDev.other.XSound;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang.UnhandledException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FHologram {
    private final String name;
    private String defaultHologram;
    private BukkitTask updateTask;
    private boolean refresh = false;
    private int refreshRate = 20;
    private Location location = null;
    public HashMap<Player, Hologram> holograms;
    public List<String> cooldown = new ArrayList<>();

    public void setLocation(Location location) {
        this.location = location;
        Utils.getFile("holograms.yml").set(getName()+".location", LocationUtil.getString(location, true));
        Utils.getFile("holograms.yml").save();
    }

    public void setExactlyLocation(Location location) {
        this.location = location;
        Utils.getFile("holograms.yml").set(getName()+".location", LocationUtil.getString(location, false));
        Utils.getFile("holograms.yml").save();
    }

    public FHologram(String name){
        this.name = name;
        holograms = new HashMap<>();
        defaultHologram = getSection().getString("default");
        if (getSection().get("location") != null){
            location = LocationUtil.getLocation(getSection().getString("location"));
        } else {
            Utils.sendMessage(Bukkit.getConsoleSender(), "{prefix}&cThe hologram "+name+" doesn't have any location to spawn!");
            return;
        }
        if (getSection().get("refresh") != null && getSection().getBoolean("refresh")){
            refresh = true;
        }
        if (getSection().get("refreshRate") != null && Utils.isNumeric(getSection().getString("refreshRate"))){
            refreshRate = getSection().getInt("refreshRate");
        }
        for (Player p : Bukkit.getOnlinePlayers()){
            spawn(p);
        }

        if (refresh){
            updateTask = new BukkitRunnable() {
                @Override
                public void run() {
                    if (!new File(Main.getPlugin().getDataFolder(), "holograms.yml").exists()){
                        cancel();
                        HologramsManager.hologramHashMap.remove(name);
                        deleteAll();
                        return;
                    }
                    onRefresh();
                }
            }.runTaskTimer(Main.getPlugin(), 0L, refreshRate);
        }
    }

    public void reload(){
        deleteAll();
        holograms.keySet().forEach(l->deleteHologram(l));
        holograms = new HashMap<>();
        defaultHologram = getSection().getString("default");
        if (getSection().get("location") != null){
            location = LocationUtil.getLocation(getSection().getString("location"));
        } else {
            Utils.sendMessage(Bukkit.getConsoleSender(), "{prefix}&cThe hologram "+name+" doesn't have any location to spawn!");
        }
        if (getSection().get("refresh") != null && getSection().getBoolean("refresh")){
            refresh = true;
        }
        if (getSection().get("refreshRate") != null && Utils.isNumeric(getSection().getString("refreshRate"))){
            refreshRate = getSection().getInt("refreshRate");
        }
        for (Player p : Bukkit.getOnlinePlayers()){
            spawn(p);
        }

        if (refresh){
            updateTask = new BukkitRunnable() {
                @Override
                public void run() {
                    if (!new File(Main.getPlugin().getDataFolder(), "holograms.yml").exists()){
                        cancel();
                        HologramsManager.hologramHashMap.remove(name);
                        deleteAll();
                        return;
                    }
                    onRefresh();
                }
            }.runTaskTimerAsynchronously(Main.getPlugin(), 0L, refreshRate);
        }
    }

    public ConfigurationSection getSection(){
        return Utils.getFile("holograms.yml").getSection(name);
    }

    public Hologram getHologram(Player p) {
        return holograms.get(p);
    }

    public void showTo(Player p, Player t){
        holograms.get(p).getVisibilityManager().showTo(t);
    }

    public boolean hasCooldown(){
        return getSection().get("cooldown")!=null;
    }

    public int getCooldown(){
        return getSection().getInt("cooldown");
    }

    public void hideTo(Player p, Player t){
        holograms.get(p).getVisibilityManager().showTo(t);
    }

    public void deleteHologram(Player p){
        holograms.get(p).clearLines();
        holograms.get(p).delete();
    }

    public Location getLocation() {
        return location;
    }

    public String getFormattedLocation() {
        return location != null ? "X:"+location.getBlockX()+"; Y:"+location.getBlockY()+"; Z:"+location.getBlockZ() : Utils.getConfig().getString("messages.commands.list.noLocation");
    }

    public String getSelection(Player p){
        return Utils.getFile("data.yml").getString(getName()+"."+p.getName(), defaultHologram);
    }

    public void loadPlayer(Player p){
        Utils.getFile("data.yml").add(getName()+"."+p.getName(), defaultHologram);
    }

    public String getName() {
        return name;
    }

    public void spawn(Player p){
        if (location == null){
            return;
        }
        if (location.getWorld() == null){
            Main.log(1, "&cCan't spawn a hologram in a null world location!");
            return;
        }
        Hologram holo = HologramsAPI.createHologram(Main.getPlugin(), location);
        for (String s : getLines(p)){
            holo.appendTextLine(Utils.ct(PlaceholderAPI.setPlaceholders(p, s)));
        }
        if (getTouchLine(p, holo, getLines(p)) == 999){
            for (int i = 0; i<holo.size();i++){
                TouchableLine lastLine = (TouchableLine) holo.getLine(i);
                TouchHandler touchHandler = player -> onClick(p);
                lastLine.setTouchHandler(touchHandler);
            }
        } else {
            TouchableLine lastLine = (TouchableLine) holo.getLine(getTouchLine(p, holo, getLines(p)));
            TouchHandler touchHandler = player -> onClick(p);
            lastLine.setTouchHandler(touchHandler);
        }
        holo.setAllowPlaceholders(true);
        VisibilityManager var7 = holo.getVisibilityManager();
        var7.setVisibleByDefault(false);
        var7.showTo(p);
        holograms.put(p, holo);
    }

    public void onRefresh(){
        if (holograms.isEmpty()){
            return;
        }
        for (Player p : getPlayers()) {
            refreshPlayer(p);
        }
    }

    List<Player> getPlayers(){
        List<Player> p = new ArrayList<>();
        for (Player p2 : holograms.keySet()){
            if (p2!=null){
                p.add(p2);
            } else {
                holograms.remove(null);
            }
        }

        return p;
    }

    public void updateTask(){
        updateTask.cancel();
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!new File(Main.getPlugin().getDataFolder(), "holograms.yml").exists()){
                    cancel();
                    HologramsManager.hologramHashMap.remove(name);
                    deleteAll();
                    return;
                }
                onRefresh();
            }
        }.runTaskTimerAsynchronously(Main.getPlugin(), 0L, refreshRate);
    }

    public void refreshPlayer(Player p){
        Hologram holo = holograms.get(p);
        if (p == null || !p.isOnline()){
            holo.delete();
            holograms.remove(p);
            return;
        }
        if (holo == null){
            holograms.remove(p);
            updateTask();
        }
        if (holo.isDeleted()){
            holograms.remove(p);
            updateTask();
        }
        final List<String> var4 = getLines(p);
        if (holo.size() > var4.size()) {
            int i = holo.size() - var4.size();
            for (int i2 = 0; i2 < i; i2++) {
                holo.removeLine((holo.size() - 1) - 1);
            }
        }
        if (var4.size() < holo.size()) {
            int i = holo.size() - var4.size();
            for (int i2 = 0; i2 < i; i2++) {
                holo.removeLine((holo.size() - 1) - 1);
            }
        } else {
            for (int i = 0; i < var4.size(); i++) {
                if (i >= holo.size()) {
                    if (holo.isDeleted()){
                        updateTask();
                        break;
                    }
                    holo.insertTextLine(i, Utils.ct(PlaceholderAPI.setPlaceholders(p, var4.get(i))));
                    continue;
                }
                TextLine textLine = (TextLine) holo.getLine(i);
                textLine.setText(Utils.ct(PlaceholderAPI.setPlaceholders(p, var4.get(i))));
            }
        }
        for (int i = 0; i<holo.size();i++){
            TouchableLine lastLine = (TouchableLine) holo.getLine(i);
            lastLine.setTouchHandler(null);
        }
        if (getTouchLine(p, holo, getLines(p)) == 999){
            for (int i = 0; i<holo.size();i++){
                TouchableLine lastLine = (TouchableLine) holo.getLine(i);
                TouchHandler touchHandler = player -> onClick(p);
                lastLine.setTouchHandler(touchHandler);
            }
        } else {
            TouchableLine lastLine = (TouchableLine) holo.getLine(getTouchLine(p, holo, getLines(p)));
            TouchHandler touchHandler = player -> onClick(p);
            lastLine.setTouchHandler(touchHandler);
        }
    }

    public void onClick(Player p){
        if (hasCooldown()) {
            if (cooldown.contains(p.getName())) {
                return;
            } else {
                cooldown.add(p.getName());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        cooldown.remove(p.getName());
                    }
                }.runTaskLater(Main.getPlugin(), getCooldown());
            }
        }
        executeActions(p);
        if (p.isSneaking() && hasBack(p)){
            setBack(p);
        } else {
            setNext(p);
        }
        refreshPlayer(p);
    }

    private boolean hasBack(Player p){
        return getSection().get(getSelection(p)+".back")!=null;
    }

    private boolean hasTouchLine(Player p){
        return getSection().get(getSelection(p)+".touchLine")!=null;
    }

    private int getTouchLine(Player p, Hologram holo, List<String> lines){
        int difference = lines.size()-holo.size();
        int lastLine = lines.size()-difference-1;
        if (hasTouchLine(p)){
            String var1 = getSection().getString(getSelection(p)+".touchLine");
            boolean isInt = Utils.isNumeric(var1);
            if (isInt){
                int i = Integer.parseInt(var1)-1;
                if (i > lines.size() || i<0){
                    return lastLine;
                }
                return i;
            } else {
                switch (var1.toLowerCase()){
                    case "top":{
                        return 0;
                    }
                    case "middle":{
                        if (lines.size() == 0){
                            return 0;
                        }
                        return lines.size() /2;
                    }
                    case "bottom":{
                        return lastLine;
                    }
                    case "all":{
                        return 999;
                    }
                }
            }
        } else {
            return lastLine;
        }
        return lastLine;
    }

    public void executeActions(Player p){
        for (String s : getActions(p)){
            boolean isCommand = s.startsWith("[command]") || s.startsWith("[console]");
            boolean isSound = s.startsWith("[sound]");
            boolean isMessage = s.startsWith("[message]");

            if (isCommand){
                boolean isConsole = s.startsWith("[console]");
                s = s.replace("[command]", "").replace("[console]", "");
                s = PlaceholderAPI.setPlaceholders(p, s);
                if (isConsole){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
                } else {
                    p.chat("/"+s);
                }
            } else if (isMessage){
                s = s.replace("[message]", "");
                s = PlaceholderAPI.setPlaceholders(p, s);
                Utils.sendMessage(p, s);
            } else if (isSound){
                s = s.replace("[sound]", "");
                XSound sound;
                float volume = 1.0F;
                float pitch = 1.0F;
                if (s.contains(",")){
                    String[] var1 = s.split(",");
                    sound = XSound.valueOf(var1[0]);
                    volume = Float.parseFloat(var1[1]);
                    pitch = Float.parseFloat(var1[2]);
                } else {
                    sound = XSound.valueOf(s.toUpperCase());
                }
                sound.play(p, volume, pitch);
            }
        }
    }

    public void deleteAll(){
        if (holograms.isEmpty()){
            return;
        }
        getPlayers().forEach(this::deleteHologram);
        if (updateTask != null){
            updateTask.cancel();
        }
        holograms.clear();
    }

    public List<String> getLines(Player p){
        String selection = getSelection(p);
        List<String> list = new ArrayList<>();
        if (getSection().get(selection+".lines") != null){
            list.addAll(getSection().getStringList(selection+".lines"));
        }
        return list;
    }

    public List<String> getActions(Player p){
        String selection = getSelection(p);
        List<String> list = new ArrayList<>();
        if (getSection().get(selection+".actions") != null){
            list.addAll(getSection().getStringList(selection+".actions"));
        }
        return list;
    }

    public void setNext(Player p){
        Utils.getFile("data.yml").set(getName()+"."+p.getName(), getNext(p));
    }

    public String getNext(Player p){
        return getSection().getString(getSelection(p)+".next", defaultHologram);
    }

    public void setBack(Player p){
        Utils.getFile("data.yml").set(getName()+"."+p.getName(), getBack(p));
    }

    public String getBack(Player p){
        return getSection().getString(getSelection(p)+".back", defaultHologram);
    }
}