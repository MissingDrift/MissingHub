package me.missigdrift.missinghub.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FlyCMD implements CommandExecutor {

    public static List<Player> flymode = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("fly")){
            if(sender.hasPermission("MissingHub.fly")){
                if(sender instanceof Player){
                    Player player = (Player) sender;
                    if(flymode.contains(player)){
                        flymode.remove(player);
                        player.sendMessage(ChatColor.RED + "Fly has been deactivated.");
                        player.setAllowFlight(false);
                    } else {
                        flymode.add(player);
                        player.sendMessage(ChatColor.GREEN + "Fly has been activated.");
                        player.setAllowFlight(true);
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
