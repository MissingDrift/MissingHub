package me.missigdrift.missinghub.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ServerCMD implements CommandExecutor {

    public MissingHub MissingHub;

    public ServerCMD(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("server")){
            if(sender instanceof Player) {
                if (sender.hasPermission("MissingHub.server")) {
                    Player player = (Player) sender;
                    if(args.length > 0){
                        String server = args[0];
                        MissingHub.pluginMessageReceived.connect(player, server);
                    } else {
                        player.sendMessage(ChatColor.RED + "Usage: /server {server}");
                    }
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
