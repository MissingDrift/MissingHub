package me.missigdrift.missinghub.listeners.world;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChangeListener implements Listener {

    public MissingHub MissingHub;

    public WeatherChangeListener(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    @EventHandler
    public void weatherEvent(WeatherChangeEvent event) {
        if (MissingHub.getConfig().getBoolean("NO_RAIN")) {
            for (World w : Bukkit.getWorlds()) {
                w.setStorm(false);
            }
        }
        if (event.getWorld().getEnvironment() == World.Environment.NORMAL) {
            if (MissingHub.getConfig().getBoolean("ALWAYS_DAY")) {
                event.getWorld().setTime(0);
            }
        }
    }
}
