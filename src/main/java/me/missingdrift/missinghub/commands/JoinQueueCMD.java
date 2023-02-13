package me.missigdrift.missinghub.commands;

import me.missigdrift.missinghub.queue.Queue;
import me.missigdrift.missinghub.queue.QueueManager;
import me.missigdrift.missinghub.utils.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinQueueCMD implements CommandExecutor {

    public MissingHub MissingHub;
    public QueueManager queueManager;
    public Util util;

    public JoinQueueCMD(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
        this.queueManager = MissingHub.queueManager;
        this.util = MissingHub.util;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("joinqueue")){
            if(sender instanceof Player){
                if(args.length > 0) {
                    String server = args[0];
                    Player player = (Player) sender;
                    Queue queue = queueManager.getQueue(player);
                    if (queue == null) {
                        queueManager.addToQueue(queueManager.getQueue(server), player);
                    } else {
                        sender.sendMessage(ChatColor.RED + "You are already in a queue!");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Usage: /joinqueue {queue}");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
            }
        }
        return true;
    }
}
