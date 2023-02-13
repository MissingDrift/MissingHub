package me.missigdrift.missinghub.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BuildModeCMD implements CommandExecutor {

    public static List<Player> buildmode = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("buildmode")){
            if(sender.hasPermission("MissingHub.buildmode")){
                if(sender instanceof Player){
                    Player player = (Player) sender;
                    if(buildmode.contains(player)){
                        buildmode.remove(player);
                        player.sendMessage(ChatColor.RED + "Buildmode has been deactivated.");
                    } else {
                        buildmode.add(player);
                        player.sendMessage(ChatColor.GREEN + "Buildmode has been activated.");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "No permissions.");
            }
        }
        return true;
    }
}
