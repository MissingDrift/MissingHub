package me.missigdrift.missinghub.listeners.player;

import me.missigdrift.missinghub.utils.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    public MissingHub MissingHub;
    public Util util;

    public PlayerMoveListener(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
        this.util = new Util(MissingHub);
    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(event.getTo().getBlockY() < 1){
            if(MissingHub.getConfig().getBoolean("VOID_TELEPORT")) {
                player.teleport(util.stringToLocation(MissingHub.getConfig().getString("SPAWN_LOCATION")));
            }
        }
        if(MissingHub.getConfig().getBoolean("BORDER.ENABLED")){
            if(isInBorder(event.getTo())){
                player.teleport(util.stringToLocation(MissingHub.getConfig().getString("SPAWN_LOCATION")));
                player.sendMessage(ChatColor.RED + "You've reached the border.");
            }
        }
    }
    public boolean isInBorder(Location location) {
        int x1 = Integer.parseInt(MissingHub.getConfig().getString("BORDER.POS1").split(",")[0]);
        int y1 = Integer.parseInt(MissingHub.getConfig().getString("BORDER.POS1").split(",")[1]);
        int z1 = Integer.parseInt(MissingHub.getConfig().getString("BORDER.POS1").split(",")[2]);
        int x2 = Integer.parseInt(MissingHub.getConfig().getString("BORDER.POS2").split(",")[0]);
        int y2 = Integer.parseInt(MissingHub.getConfig().getString("BORDER.POS2").split(",")[1]);
        int z2 = Integer.parseInt(MissingHub.getConfig().getString("BORDER.POS2").split(",")[2]);
        if ((location.getBlockX() <= x1 && location.getBlockX() >= x2) || (location.getBlockX() >= x1 && location.getBlockX() <= x2)) {
            if ((location.getBlockZ() <= z1 && location.getBlockZ() >= z2) || (location.getBlockZ() >= z1 && location.getBlockZ() <= z2)) {
                if ((location.getBlockY() <= y1 && location.getBlockY() >= y2) || (location.getBlockY() >= y1 && location.getBlockY() <= y1)) {
                    return false;
                }
            }
        }
        return true;
    }
}
