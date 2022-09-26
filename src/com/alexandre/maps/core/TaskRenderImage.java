package com.alexandre.maps.core;

import com.alexandre.maps.utils.ImageHelper;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TaskRenderImage extends BukkitRunnable {

    private final int id;
    private final Player player;
    private final String path;
    private final Color background;
    
    public TaskRenderImage(int id, Player player, String path, Color background) {
        this.id = id;
        this.player = player;
        this.path = path;
        this.background = background;
    }
    
    public void run() {
        try {
            ArrayList<Short> ids = new ArrayList<Short>();
            BufferedImage image = ImageHelper.getImage(this.path);

            int col = (int)Math.ceil(image.getWidth() / 128.0);
            int row = (int)Math.ceil(image.getHeight() / 128.0);

            BufferedImage img = ImageHelper.getImageWithBackground(this.background, image, col, row);
            BufferedImage previewImage = ImageHelper.getPreviewImage(img);

            ImageMap oldMap = ImageMapManager.getMapByID(this.id);
            int index = 0;
            short previewID;

            MapView preview;
            if (oldMap != null && oldMap.getPreviewID() > -1) {
                preview = Bukkit.getMap(oldMap.getPreviewID());
            } else {
                preview = Bukkit.createMap(this.player.getWorld());
            }

            for (MapRenderer renderer : preview.getRenderers()) {
                preview.removeRenderer(renderer);
            }

            preview.setScale(MapView.Scale.FARTHEST);
            preview.addRenderer(new ImageMapRenderer(previewImage));

            previewID = preview.getId();
            for (int y = 0; y < row; ++y) {
                for (int x = 0; x < col; ++x) {
                    MapView map;
                    if (oldMap != null && index < oldMap.getIDs().size()) {
                        map = Bukkit.getMap(oldMap.getIDs().get(index));
                    } else {
                        map = Bukkit.createMap(this.player.getWorld());
                    }

                    updateRenderers(img, y, x, map);
                    ids.add(map.getId());
                    ++index;
                }
            }

            ItemStack item = new ItemStack(Material.MAP, 1, previewID);
            ItemMeta meta = item.getItemMeta();
            meta.setLore(Lists.newArrayList("ID: " + this.id, "Scale: " + col + "x" + row));
            item.setItemMeta(meta);
            this.player.getInventory().addItem(item);

            ImageMap imageMap = new ImageMap(this.id, this.path, ids, previewID, this.background, col, row);
            ImageMapYML mapConfig = new ImageMapYML(imageMap.getID());
            mapConfig.write(imageMap);
            ImageMapManager.addMap(imageMap);

            this.player.sendMessage("§aMap rendering complete!");
        } catch (Exception e) {
            this.player.sendMessage("§cError while rendering.");
            e.printStackTrace();
        }
    }

    static void updateRenderers(BufferedImage img, int y, int x, MapView map) {
        for (MapRenderer renderer2 : map.getRenderers()) {
            map.removeRenderer(renderer2);
        }

        map.setScale(MapView.Scale.FARTHEST);
        int width = Math.min(img.getWidth() - x * 128, 128);
        int height = Math.min(img.getHeight() - y * 128, 128);
        map.addRenderer(new ImageMapRenderer(img.getSubimage(x * 128, y * 128, width, height)));
    }
}
