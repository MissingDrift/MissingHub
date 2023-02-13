package me.missigdrift.missinghub.listeners.player.inventory;

import me.missigdrift.missinghub.commands.BuildModeCMD;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryMoveListener implements Listener {

    public MissingHub MissingHub;

    public InventoryMoveListener(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {
        if(event != null) {
            if ((event.getInventory() != null && event.getCurrentItem() != null)) {
                Player player = (Player) event.getWhoClicked();
                if (!MissingHub.getConfig().getBoolean("INVENTORY_MOVE") && !BuildModeCMD.buildmode.contains(player)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
