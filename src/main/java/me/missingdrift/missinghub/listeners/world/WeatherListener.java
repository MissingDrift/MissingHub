package me.missigdrift.missinghub.listeners.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherListener implements Listener {

    public MissingHub MissingHub;

    public WeatherListener(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWeatherChangeEvent(WeatherChangeEvent e) {
        if (MissingHub.getConfig().getBoolean("NO_RAIN")) {
            if (e.toWeatherState()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onThunderChange(ThunderChangeEvent e) {
        if (MissingHub.getConfig().getBoolean("NO_RAIN")) {
            if (e.toThunderState()) {
                e.setCancelled(true);
            }
        }
    }
}
