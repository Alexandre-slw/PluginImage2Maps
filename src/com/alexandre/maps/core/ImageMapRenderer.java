package com.alexandre.maps.core;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;

public class ImageMapRenderer extends MapRenderer {

    private boolean shouldRender;
    private BufferedImage image;

    public ImageMapRenderer(BufferedImage image) {
        this.shouldRender = true;
        this.image = image;
    }

    public void render(MapView map, MapCanvas canvas, Player player) {
        if (!this.shouldRender) return;

        this.shouldRender = false;
        canvas.drawImage(0, 0, this.image);
    }

    public void updateImage(BufferedImage image) {
        this.image = image;
        this.shouldRender = true;
    }
}
