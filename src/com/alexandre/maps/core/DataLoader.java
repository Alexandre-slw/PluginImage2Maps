package com.alexandre.maps.core;

import com.alexandre.maps.Main;

import java.io.File;

public class DataLoader {

    public static void loadMaps() {
        File imageMapsDir = Main.imageMapsDir;
        File[] files = imageMapsDir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (!(file.getName().endsWith(".yml"))) continue;

            int id;
            try {
                id = Integer.parseInt(file.getName().substring(0, file.getName().length() - ".yml".length()));
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            ImageMapYML config = new ImageMapYML(id);
            ImageMap map = config.read();
            ImageMapManager.addMap(map);
            new TaskUpdateImages(map).runTaskAsynchronously(Main.instance);
        }
    }
}
