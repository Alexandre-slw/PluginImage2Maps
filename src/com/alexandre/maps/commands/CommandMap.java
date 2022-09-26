package com.alexandre.maps.commands;

import com.alexandre.maps.Main;
import com.alexandre.maps.core.TaskRenderImage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.awt.*;

public class CommandMap implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("§cInsuffisent permissions.");
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command cannot be used from the console");
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage("§cSyntax error, try /map <id> <url/path> [background color]");
            return false;
        }

        Player player = (Player) sender;
        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
            sender.sendMessage("§cInvalid ID, try /map <id> <url/path> [background color]");
            return false;
        }

        String path = args[1];
        Color color = Color.BLACK;

        if (args.length > 2) {
            color = getColorByName(args[2]);
            if (color == null) color = Color.decode(args[2].startsWith("#") ? args[2] : ("#" + args[2]));
        }

        new TaskRenderImage(id, player, path, color).runTaskAsynchronously((Plugin) Main.instance);
        return true;
    }
    
    public static Color getColorByName(String name) {
        try {
            return (Color)Color.class.getField(name.toUpperCase()).get(null);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            return null;
        }
    }
}
