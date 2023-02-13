package me.missigdrift.missinghub.listeners.blocks;

import me.missigdrift.missinghub.commands.BuildModeCMD;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    public MissingHub MissingHub;

    public BlockPlaceListener(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent event){
        if(!MissingHub.getConfig().getBoolean("PLACE_BLOCKS") && !BuildModeCMD.buildmode.contains(event.getPlayer())){
            event.setCancelled(true);
        }
    }
}
