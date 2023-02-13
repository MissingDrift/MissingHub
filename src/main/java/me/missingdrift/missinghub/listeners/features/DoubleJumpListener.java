package me.missigdrift.missinghub.listeners.features;

import me.missigdrift.missinghub.commands.FlyCMD;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class DoubleJumpListener implements Listener {

    private final HashMap<Player, Boolean> cooldown = new HashMap<>();
    private final ArrayList<Player> smash = new ArrayList<>();
    public MissingHub MissingHub;

    public DoubleJumpListener(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (MissingHub.getConfig().getBoolean("DOUBLE_JUMP")) {
            if (!MissingHub.nodoublejumpplayers.contains(p)) {
                if (p.getGameMode() == GameMode.CREATIVE || FlyCMD.flymode.contains(p)) return;
                p.setAllowFlight(cooldown.get(p) != null && cooldown.get(p));
                if (p.isOnGround()) {
                    cooldown.put(p, true);
                }
            } else {
                p.setAllowFlight(false);
            }
        }
    }

    @EventHandler
    public void onFly(PlayerToggleFlightEvent e) {
        Player p = e.getPlayer();
        if (MissingHub.getConfig().getBoolean("DOUBLE_JUMP")) {
            if (!MissingHub.nodoublejumpplayers.contains(p)) {
                if (p.getGameMode() == GameMode.CREATIVE || FlyCMD.flymode.contains(p)) return;
                if (cooldown.get(p)) {
                    e.setCancelled(true);
                    cooldown.put(p, false);
                    p.setVelocity(p.getLocation().getDirection().multiply(1.6D).setY(1.0D));
                    p.setAllowFlight(false);
                }
            }
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        if (MissingHub.getConfig().getBoolean("DOUBLE_JUMP")) {
            if (!MissingHub.nodoublejumpplayers.contains(p)) {
                if (p.getGameMode() == GameMode.CREATIVE || FlyCMD.flymode.contains(p)) return;
                if (!p.isOnGround() && cooldown.get(p) != null && !cooldown.get(p)) {
                    p.setVelocity(new Vector(0, -5, 0));
                    smash.add(p);
                }
            }
        }
    }
}