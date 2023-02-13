package me.missigdrift.missinghub.commands;

import me.missigdrift.missinghub.configs.RanksConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCMD implements CommandExecutor {

    public MissingHub MissingHub;

    public ListCMD(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("list")){
            StringBuilder ranks = new StringBuilder();
            for(String group : MissingHub.perms.getGroups()){
                String upperGroup = group.toUpperCase();
                if(RanksConfig.getRankConfig().contains(upperGroup)){
                    ranks.append(ChatColor.translateAlternateColorCodes('&', RanksConfig.getRankConfig().getString(upperGroup))).append(group).append(ChatColor.GRAY).append(", ");
                } else {
                    ranks.append(ChatColor.WHITE).append(group).append(ChatColor.GRAY).append(", ");
                }
            }
            StringBuilder members = new StringBuilder();
            for(Player player : Bukkit.getOnlinePlayers()){
                String upperGroup = MissingHub.perms.getPrimaryGroup(player).toUpperCase();
                if(RanksConfig.getRankConfig().contains(upperGroup)){
                    members.append(ChatColor.translateAlternateColorCodes('&', RanksConfig.getRankConfig().getString(upperGroup))).append(player.getName()).append(ChatColor.WHITE).append(", ");
                } else {
                    members.append(ChatColor.WHITE).append(player.getName()).append(ChatColor.WHITE).append(", ");
                }
            }
            sender.sendMessage(ranks.toString());
            sender.sendMessage("(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ") [" + members.toString().substring(0, members.toString().length() - 2) + "]");
        }
        return true;
    }
}
