package me.missigdrift.missinghub.listeners.blocks;

import me.missigdrift.missinghub.commands.BuildModeCMD;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockBreakListener implements Listener {

    public MissingHub MissingHub;

    public BlockBreakListener(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent event){
        if(!MissingHub.getConfig().getBoolean("BREAK_BLOCKS") && !BuildModeCMD.buildmode.contains(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFenceInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null) {
            Material m = e.getClickedBlock().getType();
            if (m == Material.FENCE_GATE || m == Material.WOOD_BUTTON || m == Material.STONE_BUTTON
                    || m == Material.TRAP_DOOR || m == Material.CHEST || m == Material.TRAPPED_CHEST || m == Material.ENDER_CHEST || m == Material.WOODEN_DOOR) {
                e.setCancelled(true);
            }
        }
    }
}
