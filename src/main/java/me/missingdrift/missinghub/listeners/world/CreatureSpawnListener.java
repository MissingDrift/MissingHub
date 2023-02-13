package me.missigdrift.missinghub.listeners.world;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawnListener implements Listener {

    public MissingHub MissingHub;

    public CreatureSpawnListener(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if(!MissingHub.getConfig().getBoolean("MOB_SPAWNING")) {
            if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
                for (World world : Bukkit.getWorlds()) {
                    world.setDifficulty(Difficulty.PEACEFUL);
                }
                event.setCancelled(true);
            }
        }
    }
}
