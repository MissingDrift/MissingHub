package me.missigdrift.missinghub.listeners.player.async;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

    public MissingHub MissingHub;

    public AsyncPlayerChatListener(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }
    
    @EventHandler
    public void asyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (MissingHub.getConfig().getBoolean("PLAYER_CHAT.LOCKED")) {
            if (!player.hasPermission(MissingHub.getConfig().getString("PLAYER_CHAT.BYPASS_PERM"))) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.getConfig().getString("PLAYER_CHAT.LOCKED_MESSAGE")));
                event.setCancelled(true);
            }
        }
        if (MissingHub.getConfig().getBoolean("PLAYER_CHAT.FORMAT")) {
            String format;
            if (MissingHub.getConfig().getString("RANK_PLUGIN").equalsIgnoreCase("Vault")) {
                if(MissingHub.perms != null) {
                    format = MissingHub.getConfig().getString("PLAYER_CHAT.FORMAT_MESSAGE").replace("{Player}", player.getName()).replace("{Message}", event.getMessage()).replace("{Prefix}", MissingHub.chat.getGroupPrefix(player.getWorld(), MissingHub.perms.getPrimaryGroup(player)));
                } else {
                    format = MissingHub.getConfig().getString("PLAYER_CHAT.FORMAT_MESSAGE").replace("{Player}", player.getName()).replace("{Message}", event.getMessage());
                }
            } else if (MissingHub.getConfig().getString("RANK_PLUGIN").equalsIgnoreCase("AquaCore")) {
                format = MissingHub.getConfig().getString("PLAYER_CHAT.FORMAT_MESSAGE").replace("{Player}", player.getName()).replace("{Message}", event.getMessage()).replace("{Prefix}", AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getPrefix()).replace("{Suffix}", AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getSuffix()).replace("{Name_Color}", AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getColor() + "");
            } else if (MissingHub.getConfig().getString("RANK_PLUGIN").equalsIgnoreCase("nCore")) {
                format = MissingHub.getConfig().getString("PLAYER_CHAT.FORMAT_MESSAGE").replace("{Player}", player.getName()).replace("{Message}", event.getMessage()).replace("{Prefix}", NCoreAPI.instance.getPrefix(player.getUniqueId())).replace("{Suffix}", NCoreAPI.instance.getSuffix(player.getUniqueId())).replace("{Tag}", NCoreAPI.instance.getTag(player.getUniqueId())).replace("{Name_Color}", NCoreAPI.instance.getNameColor(player.getUniqueId()));
            } else if (MissingHub.getConfig().getString("RANK_PLUGIN").equalsIgnoreCase("HestiaCore")) {
                format = MissingHub.getConfig().getString("PLAYER_CHAT.FORMAT_MESSAGE").replace("{Player}", player.getName()).replace("{Message}", event.getMessage()).replace("{Prefix}", HestiaAPI.instance.getRankPrefix(player.getUniqueId())).replace("{Suffix}", HestiaAPI.instance.getRankSuffix(player.getUniqueId())).replace("{Tag}", HestiaAPI.instance.getTagPrefix(player.getUniqueId())).replace("{Name_Color}", HestiaAPI.instance.getRankColor(player.getUniqueId()) + "");
            } else if (MissingHub.getConfig().getString("RANK_PLUGIN").equalsIgnoreCase("mCore")) {
                CoreProfile coreProfile = CorePlugin.getInstance().getProfileHandler().getCoreProfile(player);
                format = MissingHub.getConfig().getString("PLAYER_CHAT.FORMAT_MESSAGE").replace("{Player}", player.getName()).replace("{Message}", event.getMessage()).replace("{Prefix}", coreProfile.getRank().getPrefix()).replace("{Suffix}", coreProfile.getRank().getSuffix()).replace("{Tag}", coreProfile.getTag().getPrefix()).replace("{Name_Color}", "");
            } else {
                format = MissingHub.getConfig().getString("PLAYER_CHAT.FORMAT_MESSAGE").replace("{Player}", player.getName()).replace("{Message}", event.getMessage());
            }
            event.setFormat(ChatColor.translateAlternateColorCodes('&', format));
        }
    }
}
