package me.missigdrift.missinghub.commands;

import me.missigdrift.missinghub.sidetimer.SideTimer;
import me.missigdrift.missinghub.utils.Time;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class SideTimerCMD implements CommandExecutor {

    public MissingHub MissingHub;

    public SideTimerCMD(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender.hasPermission("MissingHub.sidetimer")) {
            if (args.length > 0) {
                if(args[0].equalsIgnoreCase("create")){
                    if(args.length > 1) {
                        if (SideTimer.getSideTimer(args[1], MissingHub) == null) {
                            SideTimer sideTimer = new SideTimer(args[1]);
                            MissingHub.sidetimers.add(sideTimer);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.getConfig().getString("SIDETIMER-CREATED").replace("{timer}", args[1])));
                            saveConfig(sideTimer, MissingHub);
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.getConfig().getString("SIDETIMER-ALREADY-EXIST").replace("{timer}", args[1])));
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /sidetimer create {timer}");
                    }
                }else if(args[0].equalsIgnoreCase("remove")){
                    if(args.length > 1) {
                        SideTimer sideTimer = SideTimer.getSideTimer(args[1], MissingHub);
                        if (sideTimer != null) {
                            MissingHub.sideTimersManager.getSidetimerConfig().set(sideTimer.getName(), null);
                            try {
                                MissingHub.sideTimersManager.getSidetimerConfig().save(MissingHub.sideTimersManager.getSidetimerFile());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            MissingHub.sidetimers.remove(sideTimer);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.getConfig().getString("SIDETIMER-REMOVED").replace("{timer}", args[1])));
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.getConfig().getString("SIDETIMER-DOESNT-EXIST").replace("{timer}", args[1])));
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /sidetimer remove {timer}");
                    }
                } else if(args[0].equalsIgnoreCase("format")){
                    if(args.length > 2) {
                        SideTimer sideTimer = SideTimer.getSideTimer(args[1], MissingHub);
                        if (sideTimer != null) {
                            StringBuilder sb = new StringBuilder();
                            for(int i = 2; i < args.length; i++){
                                sb.append(args[i]).append(" ");
                            }
                            sideTimer.setFormat(sb.toString().substring(0, sb.toString().length() - 1));
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.getConfig().getString("SIDETIMER-FORMAT").replace("{timer}", args[1]).replace("{format}", sideTimer.getFormat())));
                            saveConfig(sideTimer, MissingHub);
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.getConfig().getString("SIDETIMER-DOESNT-EXIST").replace("{timer}", args[1])));
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /sidetimer format {timer} {format}");
                    }
                } else if(args[0].equalsIgnoreCase("pause")){
                    if(args.length > 1) {
                        SideTimer sideTimer = SideTimer.getSideTimer(args[1], MissingHub);
                        if (sideTimer != null) {
                            sideTimer.setPause(true);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.getConfig().getString("SIDETIMER-PAUSE").replace("{timer}", args[1]).replace("{pause}", "paused")));
                            saveConfig(sideTimer, MissingHub);
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.getConfig().getString("SIDETIMER-DOESNT-EXIST").replace("{timer}", args[1])));
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /sidetimer pause {timer}");
                    }
                }  else if(args[0].equalsIgnoreCase("unpause")){
                    if(args.length > 1) {
                        SideTimer sideTimer = SideTimer.getSideTimer(args[1], MissingHub);
                        if (sideTimer != null) {
                            sideTimer.setPause(false);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.getConfig().getString("SIDETIMER-PAUSE").replace("{timer}", args[1]).replace("{pause}", "unpaused")));
                            saveConfig(sideTimer, MissingHub);
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.getConfig().getString("SIDETIMER-DOESNT-EXIST").replace("{timer}", args[1])));
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /sidetimer unpause {timer}");
                    }
                } else if(args[0].equalsIgnoreCase("unshow")){
                    if(args.length > 1) {
                        SideTimer sideTimer = SideTimer.getSideTimer(args[1], MissingHub);
                        if (sideTimer != null) {
                            if (sideTimer.getTime() > 0) {
                                sideTimer.setTime(0, MissingHub);
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.getConfig().getString("SIDETIMER-UNSHOW").replace("{timer}", args[1])));
                                saveConfig(sideTimer, MissingHub);
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.getConfig().getString("SIDETIMER-NOT-SHOWN").replace("{timer}", args[1])));
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.getConfig().getString("SIDETIMER-DOESNT-EXIST").replace("{timer}", args[1])));
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /sidetimer unshow {timer}");
                    }
                } else if(args[0].equalsIgnoreCase("show")){
                    if(args.length > 2) {
                        SideTimer sideTimer = SideTimer.getSideTimer(args[1], MissingHub);
                        if (sideTimer != null) {
                            if (sideTimer.getFormat() != null) {
                                if (Time.check(args[2])) {
                                    Time time = Time.parseString(args[2]);
                                    long millis = time.toMilliseconds();
                                    sideTimer.setTime((int) millis / 1000, MissingHub);
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.getConfig().getString("SIDETIMER-SHOW").replace("{timer}", args[1]).replace("{time}", Time.toString(millis))));
                                    saveConfig(sideTimer, MissingHub);
                                } else {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.getConfig().getString("USAGE").replace("{usage}", "/sidetimer show {timer} {time}")));
                                }
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.getConfig().getString("SIDETIMER-NO-FORMAT").replace("{timer}", args[1])));
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.getConfig().getString("SIDETIMER-DOESNT-EXIST").replace("{timer}", args[1])));
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /sidetimer show {timer} {time}");
                    }
                }
            } else {
                for(String s : MissingHub.getConfig().getStringList("SIDETIMER-HELP")){
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
        }
        return true;
    }

    public static void saveConfig(SideTimer sideTimer, MissingHub MissingHub){
        MissingHub.sideTimersManager.getSidetimerConfig().set(sideTimer.getName() + ".TIME", sideTimer.getTime());
        MissingHub.sideTimersManager.getSidetimerConfig().set(sideTimer.getName() + ".FORMAT", sideTimer.getFormat());
        MissingHub.sideTimersManager.getSidetimerConfig().set(sideTimer.getName() + ".PAUSE", sideTimer.isPause());
        try {
            MissingHub.sideTimersManager.getSidetimerConfig().save(MissingHub.sideTimersManager.getSidetimerFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}