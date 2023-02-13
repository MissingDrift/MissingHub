package me.missigdrift.missinghub.utils;


import me.missigdrift.missinghub.configs.RanksConfig;
import me.missigdrift.missinghub.queue.Queue;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Util {

    public MissingHub MissingHub;

    public Util(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    public Location stringToLocation(String string) {
        String[] locationString = string.split(",");
        return new Location(Bukkit.getWorld(locationString[0]), Double.parseDouble(locationString[1]), Double.parseDouble(locationString[2]), Double.parseDouble(locationString[3]), Float.parseFloat(locationString[4]), Float.parseFloat(locationString[5]));
    }

    public String locationToString(Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + Math.round(location.getYaw()) + "," + Math.round(location.getPitch());
    }

    public String replacer(String string, Player player) {
        final String[] returner = {""};
        if (player != null && player.isOnline()) {
            returner[0] = string.replace("{Online}", MissingHub.getPlayerCount().getOrDefault("ALL", Bukkit.getOnlinePlayers().size()) + "").replace("{Player}", player.getName());
            if (MissingHub.getConfig().getString("RANK_PLUGIN").equalsIgnoreCase("Vault")) {
                if (MissingHub.perms != null && !MissingHub.perms.getName().equalsIgnoreCase("SuperPerms")) {
                    if (MissingHub.perms.getPrimaryGroup(player) != null) {
                        returner[0] = returner[0].replace("{Rank}", MissingHub.perms.getPrimaryGroup(player));
                        if (RanksConfig.getRankConfig().getString(MissingHub.perms.getPrimaryGroup(player).toUpperCase()) != null) {
                            returner[0] = returner[0].replace("{Rank_Color}", ChatColor.translateAlternateColorCodes('&', RanksConfig.getRankConfig().getString(MissingHub.perms.getPrimaryGroup(player).toUpperCase())));
                        }
                    }
                }
            } else if (MissingHub.getConfig().getString("RANK_PLUGIN").equalsIgnoreCase("AquaCore")) {
                returner[0] = returner[0].replace("{Rank}", AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getName());
                returner[0] = returner[0].replace("{Prefix}", AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getPrefix());
                returner[0] = returner[0].replace("{Suffix}", AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getSuffix());
                returner[0] = returner[0].replace("{Rank_Color}", ChatColor.translateAlternateColorCodes('&', AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getColor() + ""));
            } else if (MissingHub.getConfig().getString("RANK_PLUGIN").equalsIgnoreCase("nCore")) {
                    returner[0] = returner[0].replace("{Rank}", NCoreAPI.instance.getRank(player.getUniqueId()));
                    returner[0] = returner[0].replace("{Prefix}", NCoreAPI.instance.getPrefix(player.getUniqueId()));
                    returner[0] = returner[0].replace("{Suffix}", NCoreAPI.instance.getSuffix(player.getUniqueId()));
                    returner[0] = returner[0].replace("{Rank_Color}", ChatColor.translateAlternateColorCodes('&', NCoreAPI.instance.getNameColor(player.getUniqueId()) + ""));
            } else if (MissingHub.getConfig().getString("RANK_PLUGIN").equalsIgnoreCase("HestiaCore")) {
                    returner[0] = returner[0].replace("{Rank}", HestiaAPI.instance.getRank(player.getUniqueId()));
                    returner[0] = returner[0].replace("{Prefix}", HestiaAPI.instance.getRankPrefix(player.getUniqueId()));
                    returner[0] = returner[0].replace("{Suffix}", HestiaAPI.instance.getRankSuffix(player.getUniqueId()));
                    returner[0] = returner[0].replace("{Rank_Color}", ChatColor.translateAlternateColorCodes('&', HestiaAPI.instance.getRankColor(player.getUniqueId()) + ""));
            } else if (MissingHub.getConfig().getString("RANK_PLUGIN").equalsIgnoreCase("mCore")) {
                CoreProfile coreProfile = CorePlugin.getInstance().getProfileHandler().getCoreProfile(player);
                returner[0] = returner[0].replace("{Rank}", coreProfile.getRank() != null && coreProfile.getRank().getName() != null ? coreProfile.getRank().getName() : "");
                returner[0] = returner[0].replace("{Prefix}", coreProfile.getRank() != null && coreProfile.getRank().getPrefix() != null ? coreProfile.getRank().getPrefix() : "");
                returner[0] = returner[0].replace("{Suffix}", coreProfile.getRank() != null && coreProfile.getRank().getSuffix() != null ? coreProfile.getRank().getSuffix() : "");
                returner[0] = returner[0].replace("{Rank_Color}", "");
            }
            for (String server : MissingHub.getServers()) {
                final String[] s = {server};
                s[0] = s[0].replace(" ", "");
                returner[0] = returner[0].replace("{playercount_" + server + "}", MissingHub.getPlayercount().get(server) + "");
            }
            if (!setToOffline(returner[0]).equalsIgnoreCase("NoStringReturned")) {
                returner[0] = setToOffline(returner[0]);
            }
            if (MissingHub.getConfig().getString("QUEUE_PLUGIN").equalsIgnoreCase("MissingHub")) {
                Queue queue = MissingHub.queueManager.getQueue(player);
                if (queue != null) {
                    returner[0] = returner[0].replace("{queue_position}", queue.getPlayers().get(player) + "").replace("{queue_max}", queue.getPlayers().size() + "").replace("{queue_name}", queue.getServer());
                }
            }else {
                if (NQueueAPI.instance.getQueue(player) != null) {
                    returner[0] = returner[0].replace("{queue_position}", NQueueAPI.instance.getPosition(player) + "").replace("{queue_max}", NQueueAPI.instance.getMax(NQueueAPI.instance.getQueue(player)) + "").replace("{queue_name}", NQueueAPI.instance.getQueue(player));
                }
            }
        }
        return returner[0];
    }

    public String setToOffline(String s) {
        if(!MissingHub.getServers().contains(getUnexistedServer(s))){
            if(!getUnexistedServer(s).equalsIgnoreCase("NOSTRINGRETURN")) {
                return getUnexistedServer(s);
            }
        }
        return "NoStringReturned";
    }

    public String getUnexistedServer(String s) {
        if (s.contains("{playercount_")) {
            String[] splitted = s.split("");
            int startChar = 600;
            for (int i = 0; i < s.length(); i++) {
                if (splitted[i].equalsIgnoreCase("{")) {
                    if (startChar == 600) {
                        startChar = i;
                    }
                }
            }
            return getUnexistedServer(s, splitted, startChar);
        }
        return "NOSTRINGRETURN";
    }
    public String getUnexistedServer(String whole, String[] splitted, int startChar) {
        StringBuilder sb = new StringBuilder();
        for (int i = startChar; i < splitted.length; i++) {
            if (!splitted[i].equalsIgnoreCase("}")) {
                sb.append(splitted[i]);
            } else {
                sb.append(splitted[i]);
                return whole.replace(sb.toString(), ChatColor.translateAlternateColorCodes('&', MissingHub.getConfig().getString("SERVER_OFFLINE")));
            }
        }
        return "NOSTRINGRETURN";
    }
}