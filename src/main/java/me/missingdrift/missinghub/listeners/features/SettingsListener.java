package me.missigdrift.missinghub.listeners.features;

import me.missigdrift.missinghub.configs.JoinItemsConfig;
import me.missigdrift.missinghub.queue.QueueManager;
import me.missigdrift.missinghub.utils.PluginMessageReceived;
import me.missigdrift.missinghub.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SettingsListener implements Listener {

    public MissingHub MissingHub;
    public Util util;
    public PluginMessageReceived pluginMessageReceived;
    public QueueManager queueManager;

    public SettingsListener(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
        this.util = MissingHub.util;
        this.pluginMessageReceived = MissingHub.pluginMessageReceived;
        this.queueManager = MissingHub.queueManager;
    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent event){
        if(JoinItemsConfig.getJoinitemsConfig().contains("INVENTORY.SETTINGS")) {
            if (event.getItem() != null && event.getItem().getType() == Material.valueOf(JoinItemsConfig.getJoinitemsConfig().getString("INVENTORY.SETTINGS.MATERIAL"))) {
                settings(event.getPlayer(), event.getItem(), event.getAction());
            }
        }
    }

    private void settings(Player player, ItemStack itemStack, Action action) {
        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
            if (itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null) {
                Inventory selector = Bukkit.createInventory(player, 27, ChatColor.RED + "Settings");
                glass(selector);
                itemStackImporterSelector(selector, player);
                player.openInventory(selector);
            }
        }
    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event){
        Inventory inventory = event.getClickedInventory();
        ItemStack itemStack = event.getCurrentItem();
        if(event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (inventory != null && itemStack != null) {
                if (inventory.getTitle().equalsIgnoreCase(ChatColor.RED + "Settings")) {
                    if (itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null) {
                        if (itemStack.getType() == Material.IRON_BOOTS) {
                            if (itemStack.getItemMeta().getLore().contains(ChatColor.GRAY + "» Enabled")) {
                                MissingHub.nodoublejumpplayers.remove(player);
                            } else {
                                MissingHub.nodoublejumpplayers.add(player);
                            }
                            itemStackImporterSelector(inventory, player);
                        }
                        if (itemStack.getType() == Material.ENDER_PEARL) {
                            if (itemStack.getItemMeta().getLore().contains(ChatColor.GRAY + "» Enabled")) {
                                MissingHub.noenderbuttplayers.remove(player);
                            } else {
                                MissingHub.noenderbuttplayers.add(player);
                            }
                            itemStackImporterSelector(inventory, player);
                        }
                        if (itemStack.getType() == Material.LEATHER_CHESTPLATE) {
                            if (itemStack.getItemMeta().getLore().contains(ChatColor.GRAY + "» Enabled")) {
                                MissingHub.noarmorplayers.remove(player);
                            } else {
                                MissingHub.noarmorplayers.add(player);
                            }
                            MissingHub.registerArmor(player);
                            itemStackImporterSelector(inventory, player);
                        }
                    }
                }
            }
        }
    }

    private void glass(Inventory inventory) {
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE);
        itemStack.setDurability((short) 15);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemStack.setItemMeta(itemMeta);
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, itemStack);
        }
    }

    private void itemStackImporterSelector(Inventory inventory, Player player) {
        ItemStack armor = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemMeta armorMeta = armor.getItemMeta();
        armorMeta.setDisplayName(ChatColor.YELLOW + "Color Armor");
        if(MissingHub.noarmorplayers.contains(player)) {
            armorMeta.setLore(Arrays.asList(ChatColor.GRAY + "» Enabled", ChatColor.RED + "» Disabled"));
        } else {
            armorMeta.setLore(Arrays.asList(ChatColor.GREEN + "» Enabled", ChatColor.GRAY + "» Disabled"));
        }
        armor.setItemMeta(armorMeta);
        inventory.setItem(16, armor);
        ItemStack enderbut = new ItemStack(Material.ENDER_PEARL);
        ItemMeta enderbutMeta = enderbut.getItemMeta();
        enderbutMeta.setDisplayName(ChatColor.YELLOW + "Enderbutt");
        if(MissingHub.noenderbuttplayers.contains(player)) {
            enderbutMeta.setLore(Arrays.asList(ChatColor.GRAY + "» Enabled", ChatColor.RED + "» Disabled"));
        } else {
            enderbutMeta.setLore(Arrays.asList(ChatColor.GREEN + "» Enabled", ChatColor.GRAY + "» Disabled"));
        }
        enderbut.setItemMeta(enderbutMeta);
        inventory.setItem(13, enderbut);
        ItemStack jumpboost = new ItemStack(Material.IRON_BOOTS);
        ItemMeta jumpboostMeta = jumpboost.getItemMeta();
        jumpboostMeta.setDisplayName(ChatColor.YELLOW + "Double Jump");
        if(MissingHub.nodoublejumpplayers.contains(player)) {
            jumpboostMeta.setLore(Arrays.asList(ChatColor.GRAY + "» Enabled", ChatColor.RED + "» Disabled"));
        } else {
            jumpboostMeta.setLore(Arrays.asList(ChatColor.GREEN + "» Enabled", ChatColor.GRAY + "» Disabled"));
        }
        jumpboost.setItemMeta(jumpboostMeta);
        inventory.setItem(10, jumpboost);
    }
}
