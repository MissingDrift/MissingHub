package me.missigdrift.missinghub.listeners.player.stats;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    public MissingHub MissingHub;

    public EntityDamageListener(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    @EventHandler
    public void entityDamageEvent(EntityDamageEvent event){
        if(MissingHub.getConfig().getBoolean("NO_DAMAGE")){
            event.setCancelled(true);
        }
    }
}
