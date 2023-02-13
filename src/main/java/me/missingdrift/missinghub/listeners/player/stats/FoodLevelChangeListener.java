package me.missigdrift.missinghub.listeners.player.stats;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeListener implements Listener {

    public MissingHub MissingHub;

    public FoodLevelChangeListener(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    @EventHandler
    public void foodLevelChangeEvent(FoodLevelChangeEvent event){
        if(MissingHub.getConfig().getBoolean("NO_HUNGER")) {
            event.setFoodLevel(20);
        }
    }
}
