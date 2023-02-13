package me.missigdrift.missinghub.listeners.features;

import me.missigdrift.missinghub.configs.JoinItemsConfig;
import me.missigdrift.missinghub.configs.SelectorConfig;
import me.missigdrift.missinghub.queue.Queue;
import me.missigdrift.missinghub.queue.QueueManager;
import me.missigdrift.missinghub.utils.PluginMessageReceived;
import me.missigdrift.missinghub.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CompassListener implements Listener {

    public MissingHub MissingHub;
    public Util util;
    public PluginMessageReceived pluginMessageReceived;
    public QueueManager queueManager;

    public CompassListener(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
        this.util = MissingHub.util;
        this.pluginMessageReceived = MissingHub.pluginMessageReceived;
        this.queueManager = MissingHub.queueManager;
    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent event){
        if(JoinItemsConfig.getJoinitemsConfig().contains("INVENTORY.COMPASS")) {
            compass(event.getPlayer(), event.getItem(), event.getAction());
        }
    }

    private void compass(Player player, ItemStack itemStack, Action action) {
        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
            if (itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null) {
                if (itemStack.getType() == Material.valueOf(JoinItemsConfig.getJoinitemsConfig().getString("INVENTORY.COMPASS.MATERIAL"))) {
                    String configName = ChatColor.translateAlternateColorCodes('&', JoinItemsConfig.getJoinitemsConfig().getString("INVENTORY.COMPASS.NAME"));
                    if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(configName)) {
                        Inventory selector = Bukkit.createInventory(player, SelectorConfig.getSelectorConfig().getInt("SELECTOR.GUI_SLOTS"),
                                ChatColor.translateAlternateColorCodes('&', SelectorConfig.getSelectorConfig().getString("SELECTOR.GUI_NAME")));
                        glass(selector);
                        itemStackImporterSelector(selector, player);

                        player.openInventory(selector);
                    }
                }
            }
        }
    }

    private void glass(Inventory inventory){
        if(SelectorConfig.getSelectorConfig().getBoolean("SELECTOR.GLASS")){
            ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE);
            itemStack.setDurability((short) 15);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(" ");
            itemStack.setItemMeta(itemMeta);
            for(int i = 0; i < inventory.getSize(); i++){
                inventory.setItem(i, itemStack);
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
                if (itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null) {
                    String invName = inventory.getTitle();
                    compass(invName, itemStack, player, event);
                    subMenus(itemStack, player, event);
                }
            }
        }
    }

    private String findConfigSelector(ItemStack itemStack){
        for(String s : SelectorConfig.getSelectorConfig().getConfigurationSection("SELECTOR.GUI_ITEMS").getKeys(false)){
            if(Material.valueOf(SelectorConfig.getSelectorConfig().getString("SELECTOR.GUI_ITEMS." + s + ".MATERIAL")) == itemStack.getType()){
                if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', SelectorConfig.getSelectorConfig().getString("SELECTOR.GUI_ITEMS." + s + ".NAME")))){
                    return s;
                }
            }
        }
        return null;
    }

    private String findConfigSubMenu(ItemStack itemStack, String submenu){
        for(String s : SelectorConfig.getSelectorConfig().getConfigurationSection("SUBMENU." + submenu + ".GUI_ITEMS").getKeys(false)){
            if(Material.valueOf(SelectorConfig.getSelectorConfig().getString("SUBMENU." + submenu + ".GUI_ITEMS." + s + ".MATERIAL")) == itemStack.getType()){
                if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', SelectorConfig.getSelectorConfig().getString("SUBMENU." + submenu + ".GUI_ITEMS." + s + ".NAME")))){
                    return s;
                }
            }
        }
        return null;
    }

    private void glass(Inventory inventory, String s){
        if(SelectorConfig.getSelectorConfig().getBoolean("SUBMENU." + s + ".GLASS")){
            ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE);
            itemStack.setDurability((short) 15);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(" ");
            itemStack.setItemMeta(itemMeta);
            for(int i = 0; i < inventory.getSize(); i++){
                inventory.setItem(i, itemStack);
            }
        }
    }

    private void itemStackImporterSelector(Inventory inventory, Player player){
        ConfigurationSection config = SelectorConfig.getSelectorConfig().getConfigurationSection("SELECTOR.GUI_ITEMS");
        for(String gamemodes : config.getKeys(false)) {
            ItemStack itemStack = new ItemStack(Material.valueOf(config.getString(gamemodes + ".MATERIAL")));
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString(gamemodes + ".NAME")));
            List<String> list = new ArrayList<>();
            for (String s : config.getStringList(gamemodes + ".LORE")) {
                list.add(ChatColor.translateAlternateColorCodes('&', util.replacer(s, player)));
            }
            if (config.contains(gamemodes + ".DATA")) {
                itemStack.setDurability((short) config.getInt(gamemodes + ".DATA"));
            }
            itemMeta.setLore(list);
            if(config.contains(gamemodes + ".GLOW") && config.getBoolean(gamemodes + ".GLOW")){
                Glow glow = new Glow(70);
                itemMeta.addEnchant(glow, 1, true);
            }
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(config.getInt(gamemodes + ".SLOT"), itemStack);
        }
    }

    private void itemStackImporterSubMenu(Inventory inventory, String subMenu, Player player){
        ConfigurationSection config = SelectorConfig.getSelectorConfig().getConfigurationSection("SUBMENU." + subMenu + ".GUI_ITEMS");
        for(String gamemodes : config.getKeys(false)) {
            ItemStack itemStack = new ItemStack(Material.valueOf(config.getString(gamemodes + ".MATERIAL")));
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString(gamemodes + ".NAME")));
            List<String> list = new ArrayList<>();
            for (String s : config.getStringList(gamemodes + ".LORE")) {
                list.add(ChatColor.translateAlternateColorCodes('&', util.replacer(s, player)));
            }
            if (config.contains(gamemodes + ".DATA")) {
                itemStack.setDurability((short) config.getInt(gamemodes + ".DATA"));
            }
            itemMeta.setLore(list);
            if(config.contains(gamemodes + ".GLOW") && config.getBoolean(gamemodes + ".GLOW")){
                Glow glow = new Glow(70);
                itemMeta.addEnchant(glow, 1, true);
            }
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(config.getInt(gamemodes + ".SLOT"), itemStack);
        }
    }

    private void compass(String invName, ItemStack itemStack, Player player, InventoryClickEvent event){
        FileConfiguration config = SelectorConfig.getSelectorConfig();
        if (invName.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', config.getString("SELECTOR.GUI_NAME")))) {
            String clickedServer = findConfigSelector(itemStack);
            if (clickedServer != null && !clickedServer.equalsIgnoreCase("")) {
                if(config.getString("SELECTOR.GUI_ITEMS." + clickedServer + ".TRAVEL").startsWith("Queue")){
                    player.closeInventory();
                    String queueName = config.getString("SELECTOR.GUI_ITEMS." + clickedServer + ".TRAVEL").replace("Queue:", "");
                    if(MissingHub.getConfig().getString("QUEUE_PLUGIN").equalsIgnoreCase("MissingHub")) {
                        Queue queue = queueManager.getQueue(queueName);
                        queueManager.addToQueue(queue, player);
                    } else {
                        NQueueAPI.instance.joinQueue(player, queueName);
                    }
                } else if(config.getString("SELECTOR.GUI_ITEMS." + clickedServer + ".TRAVEL").startsWith("SubMenu")) {
                    player.closeInventory();
                    String subMenuName = config.getString("SELECTOR.GUI_ITEMS." + clickedServer + ".TRAVEL").replace("SubMenu:", "");
                    Inventory subMenu = Bukkit.createInventory(player, config.getInt("SUBMENU." + subMenuName + ".GUI_SLOTS"), config.getString("SUBMENU." + subMenuName + ".GUI_NAME"));
                    glass(subMenu, subMenuName);
                    itemStackImporterSubMenu(subMenu, subMenuName, player);
                    player.openInventory(subMenu);
                } else if(config.getString("SELECTOR.GUI_ITEMS." + clickedServer + ".TRAVEL").startsWith("Server")) {
                    player.closeInventory();
                    String serverName = config.getString("SELECTOR.GUI_ITEMS." + clickedServer + ".TRAVEL").replace("Server:", "");
                    pluginMessageReceived.connect(player, serverName);
                }
            }
            event.setCancelled(true);
        }
    }

    private void subMenus(ItemStack itemStack, Player player, InventoryClickEvent event){
        FileConfiguration config = SelectorConfig.getSelectorConfig();
        for(String menus : config.getConfigurationSection("SUBMENU").getKeys(false)) {
            String invName = config.getString("SUBMENU." + menus + ".GUI_NAME");
            if (event.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', invName))) {
                String clickedServer = findConfigSubMenu(itemStack, menus);
                if (clickedServer != null && !clickedServer.equalsIgnoreCase("")) {
                    if (config.getString("SUBMENU." + menus + ".GUI_ITEMS." + clickedServer + ".TRAVEL").startsWith("Queue")) {
                        player.closeInventory();
                        String queueName = config.getString("SUBMENU." + menus + ".GUI_ITEMS." + clickedServer + ".TRAVEL").replace("Queue:", "");
                        if(MissingHub.getConfig().getString("QUEUE_PLUGIN").equalsIgnoreCase("MissingHub")) {
                            Queue queue = queueManager.getQueue(queueName);
                            queueManager.addToQueue(queue, player);
                        } else {
                            NQueueAPI.instance.joinQueue(player, queueName);
                        }
                    } else if (config.getString("SUBMENU." + menus + ".GUI_ITEMS." + clickedServer + ".TRAVEL").startsWith("SubMenu")) {
                        player.closeInventory();
                        String subMenuName = config.getString("SUBMENU." + menus + ".GUI_ITEMS." + clickedServer + ".TRAVEL").replace("SubMenu:", "");
                        Inventory subMenu = Bukkit.createInventory(player, config.getInt("SUBMENU." + subMenuName + ".GUI_SLOTS"), config.getString("SUBMENU." + subMenuName + ".GUI_NAME"));
                        glass(subMenu, subMenuName);
                        itemStackImporterSubMenu(subMenu, subMenuName, player);
                        player.openInventory(subMenu);
                    } else if (config.getString("SUBMENU." + menus + ".GUI_ITEMS." + clickedServer + ".TRAVEL").startsWith("Server")) {
                        player.closeInventory();
                        String serverName = config.getString("SUBMENU." + menus + ".GUI_ITEMS." + clickedServer + ".TRAVEL").replace("Server:", "");
                        pluginMessageReceived.connect(player, serverName);
                    }
                }
                event.setCancelled(true);
            }
        }
    }

    public void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Glow glow = new Glow(70);
            Enchantment.registerEnchantment(glow);
        }
        catch (IllegalArgumentException ignored){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
