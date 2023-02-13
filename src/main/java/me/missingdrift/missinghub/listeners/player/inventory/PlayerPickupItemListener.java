package me.missigdrift.missinghub.listeners.player.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPickupItemListener implements Listener {

    public MissingHub MissingHub;

    public PlayerPickupItemListener(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    @EventHandler
    public void playerPickupItemEvent(PlayerPickupItemEvent event){
        if(!MissingHub.getConfig().getBoolean("ITEM_PICKUP")){
            event.setCancelled(true);
        }
    }
}
