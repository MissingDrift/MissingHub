package me.missigdrift.missinghub.listeners.player;

import me.missigdrift.missinghub.configs.JoinItemsConfig;
import me.missigdrift.missinghub.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerJoinListener implements Listener {

    public MissingHub MissingHub;
    public Util util;

    public PlayerJoinListener(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
        this.util = MissingHub.util;
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        inventory(player);
        event.setJoinMessage(null);
        MissingHub.createScoreboard(player);
        MissingHub.registerArmor(player);
        MissingHub.registerHidedPlayer(player);
        if(MissingHub.getConfig().getBoolean("HIDE-ALL")) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                player.hidePlayer(players);
            }
        }
        if (MissingHub.getConfig().getBoolean("JOIN_SPAWN_LOCATION")) {
            player.teleport(util.stringToLocation(MissingHub.getConfig().getString("SPAWN_LOCATION")));
        }if (MissingHub.getConfig().getBoolean("JOIN_MESSAGE.ENABLED")) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.util.replacer(MissingHub.getConfig().getString("JOIN_MESSAGE.MESSAGE"), player)));
        }if (MissingHub.getConfig().getBoolean("PLAYER_MOTD.ENABLED")) {
            for (String string : MissingHub.getConfig().getStringList("PLAYER_MOTD.MESSAGE")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.util.replacer(string, player)));
            }
        }
    }

    public static void inventory(Player player){
        player.getInventory().clear();
        for(String item : JoinItemsConfig.getJoinitemsConfig().getConfigurationSection("INVENTORY").getKeys(false)){
            if(item.equalsIgnoreCase("PLAYER_HIDER")){
                break;
            }
            ItemStack itemStack = new ItemStack(Material.valueOf(JoinItemsConfig.getJoinitemsConfig().getString("INVENTORY." + item + ".MATERIAL")));
            itemStack.setDurability((short) JoinItemsConfig.getJoinitemsConfig().getInt("INVENTORY." + item + ".ID"));
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', JoinItemsConfig.getJoinitemsConfig().getString("INVENTORY." + item + ".NAME")));
            List<String> lore = new ArrayList<>();
            for(String loreString : JoinItemsConfig.getJoinitemsConfig().getStringList("INVENTORY." + item + ".LORE")){
                lore.add(ChatColor.translateAlternateColorCodes('&', loreString));
            }
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            player.getInventory().setItem(JoinItemsConfig.getJoinitemsConfig().getInt("INVENTORY." + item + ".SLOT"), itemStack);
        }
        if(JoinItemsConfig.getJoinitemsConfig().getBoolean("INVENTORY.PLAYER_HIDER.ENABLED")){
            player.getInventory().setItem(JoinItemsConfig.getJoinitemsConfig().getInt("INVENTORY.PLAYER_HIDER.SHOWN.SLOT"), shownItem());
        }
    }

    public static ItemStack hidedItem(){
        ItemStack itemStack = new ItemStack(Material.valueOf(JoinItemsConfig.getJoinitemsConfig().getString("INVENTORY.PLAYER_HIDER.HIDED.MATERIAL")));
        itemStack.setDurability((short) JoinItemsConfig.getJoinitemsConfig().getInt("INVENTORY.PLAYER_HIDER.HIDED.ID"));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', JoinItemsConfig.getJoinitemsConfig().getString("INVENTORY.PLAYER_HIDER.HIDED.NAME")));
        List<String> lore = new ArrayList<>();
        for(String loreString : JoinItemsConfig.getJoinitemsConfig().getStringList("INVENTORY.PLAYER_HIDER.HIDED.LORE")){
            lore.add(ChatColor.translateAlternateColorCodes('&', loreString));
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack shownItem(){
        ItemStack itemStack = new ItemStack(Material.valueOf(JoinItemsConfig.getJoinitemsConfig().getString("INVENTORY.PLAYER_HIDER.SHOWN.MATERIAL")));
        itemStack.setDurability((short) JoinItemsConfig.getJoinitemsConfig().getInt("INVENTORY.PLAYER_HIDER.SHOWN.ID"));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', JoinItemsConfig.getJoinitemsConfig().getString("INVENTORY.PLAYER_HIDER.SHOWN.NAME")));
        List<String> lore = new ArrayList<>();
        for(String loreString : JoinItemsConfig.getJoinitemsConfig().getStringList("INVENTORY.PLAYER_HIDER.SHOWN.LORE")){
            lore.add(ChatColor.translateAlternateColorCodes('&', loreString));
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
