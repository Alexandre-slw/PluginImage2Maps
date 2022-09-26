package com.alexandre.maps;

import com.alexandre.maps.commands.CommandGetMap;
import com.alexandre.maps.commands.CommandMap;
import com.alexandre.maps.core.DataLoader;
import com.alexandre.maps.events.ClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    public static File imageDir;
    public static File imageMapsDir;
    public static Main instance;

    public void onEnable() {
        Main.instance = this;
        (Main.imageDir = new File(this.getDataFolder(), "images")).mkdirs();
        (Main.imageMapsDir = new File(this.getDataFolder(), "maps")).mkdirs();
        this.getCommand("map").setExecutor(new CommandMap());
        this.getCommand("getmap").setExecutor(new CommandGetMap());
        this.getServer().getPluginManager().registerEvents(new ClickEvent(), this);
        DataLoader.loadMaps();
    }

    public void onDisable() {
    }
}
