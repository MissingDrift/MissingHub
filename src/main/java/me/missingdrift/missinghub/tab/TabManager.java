package me.missigdrift.missinghub.tab;




import me.missigdrift.missinghub.configs.RanksConfig;
import me.missigdrift.missinghub.configs.TabListConfig;
import me.missigdrift.missinghub.utils.Util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TabManager implements ITablist {

    public MissingHub MissingHub;
    public Util util;
    private static HashMap<Player, Boolean> enable = new HashMap<>();
    public static HashMap<Player, Boolean> getEnable() {return enable;}

    public TabManager(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
        this.util = MissingHub.util;
    }

    @Override
    public List<TablistElement> getElements(Player player) {
        List<TablistElement> list = new ArrayList<>();
        for (int i = 0; i < 80; i++) {
            list.add(new TablistElement(ChatColor.translateAlternateColorCodes('&', replacer(TabListConfig.getTablistConfig().getString("TABLIST." + (i + 1)), player)), i + 1));
        }
        return list;
    }

    @Override
    public String getHeader(Player player) {
        return null;
    }

    @Override
    public String getFooter(Player player) {
        return null;
    }

    public String replacer(String string, Player player) {
        final String[] returner = {""};
        if(player != null && player.isOnline()) {
            returner[0] = string.replace("{Online}", MissingHub.getPlayercount().get("ALL") + "").replace("{Player}", player.getName());
            for (String server : MissingHub.getServers()) {
                server = server.replace(" ", "");
                returner[0] = returner[0].replace("{playercount_" + server + "}", MissingHub.getPlayercount().get(server) + "");
            }
            if (!setToOffline(returner[0]).equalsIgnoreCase("NoStringReturned")) {
                returner[0] = setToOffline(returner[0]);
            }
            if (MissingHub.getConfig().getString("RANK_PLUGIN").equalsIgnoreCase("Vault")) {
                if (MissingHub.perms != null) {
                    if(MissingHub.perms.getPrimaryGroup(player) != null) {
                        returner[0] = returner[0].replace("{Rank}", MissingHub.perms.getPrimaryGroup(player));
                        if (RanksConfig.getRankConfig().getString(MissingHub.perms.getPrimaryGroup(player).toUpperCase()) != null) {
                            returner[0] = returner[0].replace("{Rank_Color}", ChatColor.translateAlternateColorCodes('&', RanksConfig.getRankConfig().getString(MissingHub.perms.getPrimaryGroup(player).toUpperCase())));
                        } else {
                            returner[0] = returner[0].replace("{Rank_Color}", "");
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