package com.alexandre.maps.core;

import com.alexandre.maps.utils.ImageHelper;
import org.bukkit.Bukkit;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TaskUpdateImages extends BukkitRunnable {

    private final ImageMap map;
    
    public TaskUpdateImages(ImageMap map) {
        this.map = map;
    }
    
    public void run() {
        try {
            BufferedImage image = ImageHelper.getImage(this.map.getPath());
            int col = (int)Math.ceil(image.getWidth() / 128.0);
            int row = (int)Math.ceil(image.getHeight() / 128.0);

            BufferedImage img = ImageHelper.getImageWithBackground(this.map.getBackground(), image, col, row);
            BufferedImage previewImage = ImageHelper.getPreviewImage(img);

            if (this.map.getPreviewID() > -1) {
                MapView preview = Bukkit.getMap(this.map.getPreviewID());
                for (MapRenderer renderer : preview.getRenderers()) {
                    preview.removeRenderer(renderer);
                }
                preview.setScale(MapView.Scale.FARTHEST);
                preview.addRenderer(new ImageMapRenderer(previewImage));
            }

            int index = 0;
            for (int y = 0; y < row; ++y) {
                for (int x = 0; x < col && index < this.map.getIDs().size(); ++index, ++x) {
                    MapView map = Bukkit.getMap(this.map.getIDs().get(index));
                    TaskRenderImage.updateRenderers(img, y, x, map);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
