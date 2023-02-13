package me.missigdrift.missinghub.commands;

import me.missigdrift.missinghub.queue.Queue;
import me.missigdrift.missinghub.queue.QueueManager;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class QueueCMD implements CommandExecutor {

    public MissingHub MissingHub;
    public QueueManager queueManager;

    public QueueCMD(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
        this.queueManager = MissingHub.queueManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("queue")) {
            if (sender.hasPermission("MissingHub.queue")) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("unpause")) {
                        if (sender.hasPermission("MissingHub.unpause")) {
                            if (args.length > 1) {
                                Queue queue = queueManager.getQueue(args[1]);
                                if (queue != null) {
                                    if (queue.isPaused()) {
                                        if (MissingHub.getServers().contains(queue.getServer())) {
                                            queue.setPaused(false);
                                            sender.sendMessage(ChatColor.RED + "Queue has been unpaused.");
                                        } else {
                                            sender.sendMessage(ChatColor.RED + "There is no server called " + queue.getServer() + " connected to the bungee!");
                                        }
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "Queue is already unpaused!");
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Queue doesn't exist!");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Use: \"/queue unpause {Queue}\"");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "No permissions!");
                        }
                    } else if (args[0].equalsIgnoreCase("pause")) {
                        if (sender.hasPermission("MissingHub.pause")) {
                            if (args.length > 1) {
                                Queue queue = queueManager.getQueue(args[1]);
                                if (queue != null) {
                                    if (!queue.isPaused()) {
                                        queue.setPaused(true);
                                        sender.sendMessage(ChatColor.RED + "Queue has been paused.");
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "Queue is already paused!");
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Queue doesn't exist!");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Use: \"/queue pause {Queue}\"");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "No permissions!");
                        }
                    } else if (args[0].equalsIgnoreCase("settime")) {
                        if (sender.hasPermission("MissingHub.settime")) {
                            if (args.length > 1) {
                                if (NumberUtils.isNumber(args[1])) {
                                    int seconds = Integer.parseInt(args[1]);
                                    MissingHub.getConfig().set("QUEUE_RUNNER", seconds);
                                    MissingHub.saveConfig();
                                    sender.sendMessage(ChatColor.GREEN + "Queue seconds have been set to " + seconds + "!");
                                } else {
                                    sender.sendMessage(ChatColor.RED + "[Amount] have to be numbers!");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Use: \"/queue settime [Amount]\"");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "No permissions!");
                        }
                    } else if (args[0].equalsIgnoreCase("setplayers")) {
                        if (sender.hasPermission("MissingHub.setplayer")) {
                            if (args.length > 1) {
                                if (NumberUtils.isNumber(args[1])) {
                                    int players = Integer.parseInt(args[1]);
                                    MissingHub.getConfig().set("QUEUE_JOINS", players);
                                    MissingHub.saveConfig();
                                    sender.sendMessage(ChatColor.GREEN + "Queue players have been set to " + players + "!");
                                } else {
                                    sender.sendMessage(ChatColor.RED + "[Amount] have to be numbers!");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Use: \"/queue setplayers [Amount]\"");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "No permissions!");
                        }
                    } else {
                        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------");
                        sender.sendMessage(ChatColor.RED + "/queue pause [Queue]");
                        sender.sendMessage(ChatColor.RED + "/queue unpause [Queue]");
                        sender.sendMessage(ChatColor.RED + "/queue settime [Amount]");
                        sender.sendMessage(ChatColor.RED + "/queue setplayers [Amount]");
                        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------");
                    }
                } else {
                    sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------");
                    sender.sendMessage(ChatColor.RED + "/queue pause [Queue]");
                    sender.sendMessage(ChatColor.RED + "/queue unpause [Queue]");
                    sender.sendMessage(ChatColor.RED + "/queue settime [Amount]");
                    sender.sendMessage(ChatColor.RED + "/queue setplayers [Amount]");
                    sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "No permissions.");
            }
        }
        return true;
    }
}
