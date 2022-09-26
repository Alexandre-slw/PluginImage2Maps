package com.alexandre.maps.core;

import com.alexandre.maps.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageMapYML {

    private final int id;
    private final File configFile;
    private final YamlConfiguration config;
    
    public ImageMapYML(int id) {
        this.id = id;
        this.configFile = new File(Main.imageMapsDir, this.id + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }
    
    public void write(ImageMap map) {
        ConfigurationSection section = this.config.createSection("image");
        section.set("id", this.id);
        section.set("path", map.getPath());
        section.set("background", map.getBackground().getRGB());
        section.set("preview_id", map.getPreviewID());
        section.set("ids", map.getIDs());
        section.set("col", map.getCol());
        section.set("row", map.getRow());
        this.save();
    }
    
    public ImageMap read() {
        ConfigurationSection section = this.config.getConfigurationSection("image");
        int id = section.getInt("id");
        String path = section.getString("path");
        Color background = new Color(section.getInt("background", Color.BLACK.getRGB()));
        short previewID = (short)section.getInt("id", -1);
        ArrayList<Short> ids = (ArrayList<Short>)section.getShortList("ids");
        int col = section.getInt("col", 1);
        int row = section.getInt("row", 1);
        return new ImageMap(id, path, ids, previewID, background, col, row);
    }
    
    private void save() {
        try {
            this.config.save(this.configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
