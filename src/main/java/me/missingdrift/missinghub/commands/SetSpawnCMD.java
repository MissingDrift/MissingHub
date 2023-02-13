package me.missigdrift.missinghub.commands;

import me.missigdrift.missinghub.utils.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCMD implements CommandExecutor {

    public MissingHub MissingHub;
    public Util util;

    public SetSpawnCMD(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
        this.util = MissingHub.util;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("setspawn")){
            if(sender instanceof Player) {
                if (sender.hasPermission("MissingHub.setspawn")) {
                    Player player = (Player) sender;
                    MissingHub.getConfig().set("SPAWN_LOCATION", util.locationToString(player.getLocation()));
                    MissingHub.saveConfig();
                    sender.sendMessage(ChatColor.GRAY + "Spawn have been set!");
                } else {
                    sender.sendMessage(ChatColor.RED + "No Permissions.");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
            }
        }
        return true;
    }
}
