package com.alexandre.maps.events;

import com.google.common.collect.Lists;
import com.alexandre.maps.Main;
import com.alexandre.maps.core.ImageMap;
import com.alexandre.maps.core.ImageMapManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ClickEvent implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getPlayer().getInventory().getItemInHand() == null || event.getPlayer().getInventory().getItemInHand().getType() != Material.MAP) return;
        if (event.getBlockFace() == BlockFace.UP || event.getBlockFace() == BlockFace.DOWN) return;
        ItemStack map = event.getPlayer().getInventory().getItemInHand();

        if (!map.hasItemMeta()) return;

        int id = -1;
        int col = -1;
        int row = -1;
        
        for (String lore : map.getItemMeta().getLore()) {
            if (lore.startsWith("ID: ")) {
                try {
                    id = Integer.parseInt(lore.substring("ID: ".length()));
                } catch (Exception e) {
                    return;
                }
            }
            
            if (lore.startsWith("Scale: ")) {
                String[] scale = lore.substring("Scale: ".length()).split("x");
                if (scale.length < 2) return;
                
                try {
                    col = Integer.parseInt(scale[0]);
                } catch (Exception e2) {
                    return;
                }
                
                try {
                    row = Integer.parseInt(scale[1]);
                } catch (Exception e2) {
                    return;
                }
            }
        }
        
        if (id < 0 || row < 0 || col < 0) return;
        
        ImageMap imageMap = ImageMapManager.getMapByID(id);
        if (imageMap == null) return;
        if (imageMap.getIDs().size() != col * row) return;
        
        int direction = 0;
        if (event.getBlockFace() == BlockFace.WEST) {
            direction = 1;
        } else if (event.getBlockFace() == BlockFace.EAST) {
            direction = -1;
        } else if (event.getBlockFace() == BlockFace.NORTH) {
            direction = -1;
        } else if (event.getBlockFace() == BlockFace.SOUTH) {
            direction = 1;
        }
        
        int xSub = 0;
        if (event.getBlockFace() == BlockFace.WEST) {
            xSub = 1;
        } else if (event.getBlockFace() == BlockFace.EAST) {
            xSub = -1;
        }
        
        int zSub = 0;
        if (event.getBlockFace() == BlockFace.NORTH) {
            zSub = 1;
        } else if (event.getBlockFace() == BlockFace.SOUTH) {
            zSub = -1;
        }
        
        Location from = new Location(event.getPlayer().getWorld(), (double) (event.getClickedBlock().getX() - xSub), (double) event.getClickedBlock().getY(), (double) (event.getClickedBlock().getZ() - zSub));
        boolean isZ = event.getBlockFace() == BlockFace.EAST || event.getBlockFace() == BlockFace.WEST;
        int max = isZ ? from.getBlockZ() : from.getBlockX();
        max = ((direction > 0) ? (max + col) : (max - col));

        Location current;
        for (int y = from.getBlockY(); y > from.getBlockY() - row; --y) {
            int posFrom = isZ ? from.getBlockZ() : from.getBlockX();
            while ((direction <= 0 || posFrom < max) && posFrom > max) {
                current = new Location(from.getWorld(), isZ ? from.getBlockX() : posFrom, y, isZ ? posFrom : from.getBlockZ());
                if (from.getWorld().getBlockAt(current).getType() != Material.AIR) {
                    event.getPlayer().sendMessage("§cNot enought space! (" + col + "x" + row + ")");
                    return;
                }

                Location solidBlock = new Location(from.getWorld(), (isZ ? from.getBlockX() : posFrom) + xSub, y, (isZ ? posFrom : from.getBlockZ()) + zSub);
                if (!from.getWorld().getBlockAt(solidBlock).getType().isSolid()) {
                    event.getPlayer().sendMessage("§cNot enought space! (" + col + "x" + row + ")");
                    return;
                }

                posFrom += direction;
            }
        }

        int index = 0;
        for (int y2 = from.getBlockY(); y2 > from.getBlockY() - row; --y2) {
            int posFrom2 = isZ ? from.getBlockZ() : from.getBlockX();

            while ((direction <= 0 || posFrom2 < max) && posFrom2 > max) {
                current = new Location(from.getWorld(), isZ ? from.getBlockX() : posFrom2, y2, isZ ? posFrom2 : from.getBlockZ());

                ItemFrame itemframe = from.getWorld().spawn(current, ItemFrame.class);
                itemframe.setFacingDirection(event.getBlockFace());

                HangingPlaceEvent hEvent = new HangingPlaceEvent(itemframe, event.getPlayer(), event.getClickedBlock(), event.getBlockFace());
                Main.instance.getServer().getPluginManager().callEvent(hEvent);

                ItemStack item = new ItemStack(Material.MAP, 1, imageMap.getIDs().get(index));
                ItemMeta meta = item.getItemMeta();
                meta.setLore(Lists.newArrayList("ID: " + id, "Position: " + (index % col + 1) + " | " + (1 + index / col)));
                item.setItemMeta(meta);
                itemframe.setItem(item);
                ++index;
                posFrom2 += direction;
            }
        }
    }
}
