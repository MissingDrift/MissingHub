package me.missigdrift.missinghub.listeners.features;

import me.missigdrift.missinghub.configs.JoinItemsConfig;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class EnderButtListener implements Listener {

    public MissingHub MissingHub;
    public List<Player> cooldown = new ArrayList<>();

    public EnderButtListener(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    @EventHandler
    public void onHit(PlayerInteractEvent e) {
        if (e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (!MissingHub.noenderbuttplayers.contains(e.getPlayer())) {
                    if (e.getAction() == Action.RIGHT_CLICK_AIR) {
                        Vector vec = e.getPlayer().getLocation().getDirection();
                        Location frontlocation = e.getPlayer().getEyeLocation().add(vec);
                        e.getPlayer().setVelocity(frontlocation.getDirection().multiply(4.0));
                    }
                }
                e.setCancelled(true);
                ItemStack itemStack = new ItemStack(Material.valueOf(JoinItemsConfig.getJoinitemsConfig().getString("INVENTORY.ENDERBUTT.MATERIAL")));
                itemStack.setDurability((short) JoinItemsConfig.getJoinitemsConfig().getInt("INVENTORY.ENDERBUTT.ID"));
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', JoinItemsConfig.getJoinitemsConfig().getString("INVENTORY.ENDERBUTT.NAME")));
                List<String> lore = new ArrayList<>();
                for (String loreString : JoinItemsConfig.getJoinitemsConfig().getStringList("INVENTORY.ENDERBUTT.LORE")) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', loreString));
                }
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
                e.getPlayer().getInventory().setItemInHand(itemStack);
                e.getPlayer().updateInventory();
            }
        }
    }
}
