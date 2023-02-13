package me.missigdrift.missinghub.listeners.player.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {

    public MissingHub MissingHub;

    public PlayerDropItemListener(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    @EventHandler
    public void playerDropItemEvent(PlayerDropItemEvent event){
        if(!MissingHub.getConfig().getBoolean("ITEM_DROP")){
            event.setCancelled(true);
        }
    }
}
