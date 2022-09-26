package com.alexandre.maps.core;

import java.util.*;

public class ImageMapManager {

    private static ArrayList<ImageMap> maps;

    static {
        ImageMapManager.maps = new ArrayList<>();
    }

    public static void addMap(ImageMap map) {
        if (getMapByID(map.getID()) != null) {
            ImageMapManager.maps.set(ImageMapManager.maps.indexOf(getMapByID(map.getID())), map);
        } else {
            ImageMapManager.maps.add(map);
        }
    }

    public static void removeMap(ImageMap map) {
        ImageMapManager.maps.remove(map);
    }

    public static ArrayList<ImageMap> getMaps() {
        return ImageMapManager.maps;
    }

    public static ImageMap getMapByID(int id) {
        for (ImageMap map : ImageMapManager.maps) {
            if (map.getID() == id) {
                return map;
            }
        }
        return null;
    }
}
