package com.alexandre.maps.commands;

import com.alexandre.maps.core.ImageMap;
import com.alexandre.maps.core.ImageMapManager;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandGetMap implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("§cInsuffisent permissions.");
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command cannot be used from the console");
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage("§cSyntax error, try /getmap <id>");
            return false;
        }

        Player player = (Player) sender;
        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
            sender.sendMessage("§cInvalid ID, try /getmap <id>");
            return false;
        }

        ImageMap map = ImageMapManager.getMapByID(id);
        if (map == null || map.getPreviewID() < 0) {
            sender.sendMessage("§cCannot found map with id " + id);
            return false;
        }

        ItemStack item = new ItemStack(Material.MAP, 1, map.getPreviewID());
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Lists.newArrayList("ID: " + id, "Scale: " + map.getCol() + "x" + map.getRow()));
        item.setItemMeta(meta);
        player.getInventory().addItem(item);
        return true;
    }

}
