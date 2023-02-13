package me.missigdrift.missinghub.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MissingHubCMD implements CommandExecutor {

    public MissingHub MissingHub;

    public MissingHubCMD(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("MissingHub")) {
            sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------------");
            sender.sendMessage(ChatColor.YELLOW + MissingHub.getDescription().getFullName() + ":");
            sender.sendMessage(ChatColor.YELLOW + "");
            sender.sendMessage(ChatColor.YELLOW + "Name: " + ChatColor.GRAY + MissingHub.getDescription().getName());
            sender.sendMessage(ChatColor.YELLOW + "Version: " + ChatColor.GRAY + MissingHub.getDescription().getVersion());
            sender.sendMessage(ChatColor.YELLOW + "");
            sender.sendMessage(ChatColor.YELLOW + "Author: " + ChatColor.GRAY + MissingHub.getDescription().getAuthors().get(0));
            sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------------");
            if ((sender.hasPermission("MissingHub.*"))) {
                sender.sendMessage(ChatColor.YELLOW + "Customized License: " + ChatColor.GREEN + "Active" +
                        ChatColor.GRAY + " (" + ChatColor.YELLOW + MissingHub.getConfig().getString("LICENSE") + ChatColor.GRAY + ")");
                sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------------");
            }
        }
        return true;
    }
}
