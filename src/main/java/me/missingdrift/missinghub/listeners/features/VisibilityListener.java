package me.missigdrift.missinghub.listeners.features;

import me.missigdrift.missinghub.configs.JoinItemsConfig;
import me.missigdrift.missinghub.listeners.player.PlayerJoinListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class VisibilityListener implements Listener {

    public final HashMap<Player, Integer> cooldown = new HashMap<>();
    public MissingHub MissingHub;

    public VisibilityListener(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack itemStack = player.getItemInHand();
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null) {
                if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', JoinItemsConfig.getJoinitemsConfig().getString("INVENTORY.PLAYER_HIDER.HIDED.NAME")))) {
                    if (!cooldown.containsKey(player)) {
                        MissingHub.hideplayers.remove(player);
                        MissingHub.registerHidedPlayers(player);
                        player.getInventory().setItem(JoinItemsConfig.getJoinitemsConfig().getInt("INVENTORY.PLAYER_HIDER.SHOWN.SLOT"), PlayerJoinListener.shownItem());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', JoinItemsConfig.getJoinitemsConfig().getString("INVENTORY.PLAYER_HIDER.SHOW_MESSAGE")));
                        cooldown.put(player, 3);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                cooldown.put(player, cooldown.get(player) - 1);
                                if (cooldown.get(player) == 0) {
                                    cooldown.remove(player);
                                    cancel();
                                }
                            }
                        }.runTaskTimer(MissingHub, 0, 20);
                    }
                } else if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', JoinItemsConfig.getJoinitemsConfig().getString("INVENTORY.PLAYER_HIDER.SHOWN.NAME")))) {
                    if (!cooldown.containsKey(player)) {
                        MissingHub.hideplayers.add(player);
                        MissingHub.registerHidedPlayers(player);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', JoinItemsConfig.getJoinitemsConfig().getString("INVENTORY.PLAYER_HIDER.HIDE_MESSAGE")));
                        player.getInventory().setItem(JoinItemsConfig.getJoinitemsConfig().getInt("INVENTORY.PLAYER_HIDER.HIDED.SLOT"), PlayerJoinListener.hidedItem());
                        cooldown.put(player, 3);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                cooldown.put(player, cooldown.get(player) - 1);
                                if (cooldown.get(player) == 0) {
                                    cooldown.remove(player);
                                    cancel();
                                }
                            }
                        }.runTaskTimer(MissingHub, 0, 20);
                    }
                }
            }
        }
    }
}
