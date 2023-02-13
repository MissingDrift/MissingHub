package me.missigdrift.missinghub;


import me.missigdrift.missinghub.listeners.blocks.BlockBreakListener;
import me.missigdrift.missinghub.listeners.blocks.BlockPlaceListener;

import me.missigdrift.missinghub.listeners.player.inventory.InventoryMoveListener;
import me.missigdrift.missinghub.listeners.world.CreatureSpawnListener;
import me.missigdrift.missinghub.listeners.world.WeatherListener;
import me.missigdrift.missinghub.listeners.player.PlayerJoinListener;
import me.missigdrift.missinghub.listeners.player.PlayerMoveListener;
import me.missigdrift.missinghub.listeners.player.PlayerQuitListener;
import me.missigdrift.missinghub.listeners.player.async.AsyncPlayerChatListener;
import me.missigdrift.missinghub.listeners.player.inventory.PlayerDropItemListener;
import me.missigdrift.missinghub.listeners.player.inventory.PlayerPickupItemListener;
import me.missigdrift.missinghub.listeners.player.stats.EntityDamageListener;
import me.missigdrift.missinghub.listeners.player.stats.FoodLevelChangeListener;
import me.missigdrift.missinghub.queue.Queue;
import me.missigdrift.missinghub.queue.QueueManager;
import me.missigdrift.missinghub.sidetimer.SideTimer;
import me.missigdrift.missinghub.tab.TabManager;
import me.missigdrift.missinghub.utils.ColourUtils;
import me.missigdrift.missinghub.utils.PluginMessageReceived;
import me.missigdrift.missinghub.utils.ScoreHelper;
import me.missigdrift.missinghub.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.PluginManager;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@DoNotRename
public final class MissingHub extends JavaPlugin {

    @DoNotRename
    public Permission perms = null;
    @DoNotRename
    public Chat chat = null;
    @DoNotRename
    public HashMap<String, Integer> playercount = new HashMap<>();
    @DoNotRename
    public List<String> servers = new ArrayList<>();
    @DoNotRename
    public List<Queue> queues = new ArrayList<>();
    @DoNotRename
    public List<Player> hideplayers = new ArrayList<>();
    @DoNotRename
    public List<Player> noarmorplayers = new ArrayList<>();
    @DoNotRename
    public List<Player> noenderbuttplayers = new ArrayList<>();
    @DoNotRename
    public List<Player> nodoublejumpplayers = new ArrayList<>();
    @DoNotRename
    public Util util;
    @DoNotRename
    public QueueManager queueManager;
    @DoNotRename
    public PluginMessageReceived pluginMessageReceived;
    @DoNotRename
    public List<String> scorelist;
    @DoNotRename
    public TabManager tabManager;
    @DoNotRename
    public List<SideTimer> sidetimers = new ArrayList<>();
    @DoNotRename
    public SideTimersManager sideTimersManager;


    @DoNotRename
    public HashMap<String, Integer> getPlayercount() {
        return playercount;
    }
    @DoNotRename
    public List<String> getServers() {
        return servers;
    }
    @DoNotRename
    public List<Queue> getQueues() {
        return queues;
    }

    @DoNotRename
    @ControlFlowObfuscation(ControlFlowObfuscation.DISABLE)
    @ExtensiveFlowObfuscation(ExtensiveFlowObfuscation.DISABLE)
    @Override
    public void onEnable() {
        registerLicense();
        saveDefaultConfig();
        registerConfigs();
        if (this.isEnabled()) {
            if (getConfig().getString("RANK_PLUGIN").equalsIgnoreCase("Vault")) {
                if(Bukkit.getPluginManager().getPlugin("Vault") != null) {
                    setupChat();
                    setupPermissions();
                }
            }
            util = new Util(this);
            tabManager = new TabManager(this);
            queueManager = new QueueManager(this);
            pluginMessageReceived = new PluginMessageReceived(this);
            registerBungeecord();
            registerEvents();
            registerCommands();
            registerInventory();
            registerElse();
        }
    }
    @DoNotRename
    @ControlFlowObfuscation(ControlFlowObfuscation.DISABLE)
    @ExtensiveFlowObfuscation(ExtensiveFlowObfuscation.DISABLE)
    @Override
    public void onDisable() {
        unRegisterScoreboardAll();
    }

    @DoNotRename
    @ControlFlowObfuscation(ControlFlowObfuscation.DISABLE)
    @ExtensiveFlowObfuscation(ExtensiveFlowObfuscation.DISABLE)
    private void registerLicense(){

    }

    private void registerConfigs() {
        JoinItemsConfig joinItemsConfig = new JoinItemsConfig(this);
        joinItemsConfig.createJoinItemsFile();
        RanksConfig ranksConfig = new RanksConfig(this);
        ranksConfig.createRankFile();
        SelectorConfig selectorConfig = new SelectorConfig(this);
        selectorConfig.createSelectorFile();
        TabListConfig tabListConfig = new TabListConfig(this);
        tabListConfig.createTabListFile();
        sideTimersManager = new SideTimersManager(this);
        sideTimersManager.createSideTimer();
        for(String sidetimer : sideTimersManager.getSidetimerConfig().getKeys(false)){
            SideTimer sideTimer = new SideTimer(sidetimer, sideTimersManager.getSidetimerConfig().getString(sidetimer + ".FORMAT"), sideTimersManager.getSidetimerConfig().getInt(sidetimer + ".TIME"), sideTimersManager.getSidetimerConfig().getBoolean(sidetimer + ".PAUSE"));
            sidetimers.add(sideTimer);
        }
        scorelist = getConfig().getStringList("SCOREBOARD");
    }

    private void registerBungeecord() {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessageReceived(this));
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new AsyncPlayerChatListener(this), this);
        pm.registerEvents(new BlockBreakListener(this), this);
        pm.registerEvents(new BlockPlaceListener(this), this);
        pm.registerEvents(new CompassListener(this), this);
        pm.registerEvents(new CreatureSpawnListener(this), this);
        pm.registerEvents(new DoubleJumpListener(this), this);
        pm.registerEvents(new EnderButtListener(this), this);
        pm.registerEvents(new EntityDamageListener(this), this);
        pm.registerEvents(new FoodLevelChangeListener(this), this);
        pm.registerEvents(new PlayerDropItemListener(this), this);
        pm.registerEvents(new VisibilityListener(this), this);
        pm.registerEvents(new PlayerJoinListener(this), this);
        pm.registerEvents(new PlayerMoveListener(this), this);
        pm.registerEvents(new PlayerPickupItemListener(this), this);
        pm.registerEvents(new InventoryMoveListener(this), this);
        pm.registerEvents(new PlayerQuitListener(this), this);
        pm.registerEvents(new WeatherListener(this), this);
        pm.registerEvents(new SettingsListener(this), this);
    }

    private void registerCommands() {
        getCommand("server").setExecutor(new ServerCMD(this));
        getCommand("setspawn").setExecutor(new SetSpawnCMD(this));
        if(getConfig().getString("QUEUE_PLUGIN").equalsIgnoreCase("MissingHub")) {
            getCommand("queue").setExecutor(new QueueCMD(this));
            getCommand("leavequeue").setExecutor(new LeaveQueueCMD(this));
            getCommand("joinqueue").setExecutor(new JoinQueueCMD(this));
        }
        getCommand("fly").setExecutor(new FlyCMD());
        getCommand("MissingHub").setExecutor(new MissingHubCMD(this));
        getCommand("buildmode").setExecutor(new BuildModeCMD());
        getCommand("sidetimer").setExecutor(new SideTimerCMD(this));
        if (getConfig().getBoolean("LIST_COMMAND")) {
            getCommand("list").setExecutor(new ListCMD(this));
        }
        if (getConfig().getBoolean("ARMOR_RANK")) {
            getCommand("rainbow").setExecutor(new RainbowCMD(this));
        }
    }

    public void registerHidedPlayers(Player player) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (hideplayers.contains(player) || getConfig().getBoolean("HIDE-ALL")) {
                player.hidePlayer(players);
            } else {
                player.showPlayer(players);
            }
        }
    }

    public void registerHidedPlayer(Player player) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (hideplayers.contains(players) || getConfig().getBoolean("HIDE-ALL")) {
                players.hidePlayer(player);
            } else {
                players.showPlayer(player);
            }
        }
    }

    private void registerInventory() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerJoinListener.inventory(player);
        }
    }

    private void registerScoreboardAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            createScoreboard(player);
        }
    }

    private void unRegisterScoreboardAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (ScoreHelper.hasScore(player)) {
                ScoreHelper.removeScore(player);
            }
        }
    }

    private void setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
    }

    private void setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp != null) {
            chat = rsp.getProvider();
        }
    }

    public void createScoreboard(Player player) {
        if(getConfig().getBoolean("SCOREBOARD-STATE")) {
            if (!ScoreHelper.hasScore(player)) {
                ScoreHelper helper = ScoreHelper.createScore(player);
                helper.setTitle(getConfig().getString("SCOREBOARD_TITLE"));
                List<String> list = new ArrayList<>(getConfig().getStringList("SCOREBOARD"));
                helper.setSlotsFromList(list);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.isOnline()) {
                            if (ScoreHelper.hasScore(player)) {
                                List<String> list = new ArrayList<>();
                                for (String s : getConfig().getStringList("SCOREBOARD")) {
                                    String s1 = util.replacer(s, player);
                                    if (s.contains("{timer-space}")) {
                                        for (SideTimer sideTimer : sidetimers) {
                                            if (sideTimer.getTime() > 0) {
                                                list.add(" ");
                                            }
                                        }
                                    } else if (s.contains("{timer}")) {
                                        for (SideTimer sideTimer : sidetimers) {
                                            if (sideTimer.getTime() > 0) {
                                                int x = ((sideTimer.getTime() * 1000) / 1000);
                                                int seconds = x % 60;
                                                x /= 60;
                                                int minutes = x % 60;
                                                list.add(s.replace("{timer-format}", sideTimer.getFormat()).replace("{timer}", ((minutes + "").length() == 1 ? "0" + minutes : minutes + "") + ":" + ((seconds + "").length() == 1 ? "0" + seconds : seconds + "")));
                                            }
                                        }
                                    } else if (s1.contains("{If<queue_enabled>}")) {
                                        if (getConfig().getString("QUEUE_PLUGIN").equalsIgnoreCase("MissingHub")) {
                                            if (queueManager.getQueue(player) != null) {
                                                list.add(s1.replace("{If<queue_enabled>}", ""));
                                            }
                                        } else {
                                            if (NQueueAPI.instance.getQueue(player) != null) {
                                                list.add(s1.replace("{If<queue_enabled>}", ""));
                                            }
                                        }
                                    } else {
                                        list.add(s1);
                                    }
                                }
                                helper.setSlotsFromList(list);
                            } else {
                                cancel();
                            }
                        } else {
                            cancel();
                        }
                    }
                }.runTaskTimer(this, 5, getConfig().getInt("SCOREBOARD-UPDATE") * 20);
            }
        }
    }

    public void registerTimers(){
        new BukkitRunnable(){
            @Override
            public void run() {
                for(SideTimer sideTimer : sidetimers) {
                    if (sideTimer.getTime() > 0 && !sideTimer.isPause()) {
                        sideTimer.setTime(sideTimer.getTime() - 1, MissingHub.this);
                    }
                }
            }
        }.runTaskTimer(this, 0, 20);
        new BukkitRunnable(){
            @Override
            public void run() {
                pluginMessageReceived.getServers();
                pluginMessageReceived.playerCount(null);
            }
        }.runTaskTimer(this,0, 150);
    }

    public void registerElse() {
        registerScoreboardAll();
        registerTimers();
        if (getConfig().getBoolean("TABLIST")) {
            MissingHub MissingHub = this;
            new OutlastTab(MissingHub, new TabManager(MissingHub), getConfig().getInt("TABLIST-UPDATE") * 20);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            registerArmor(player);
        }
        if (getConfig().getBoolean("HIDE-ALL")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    player.hidePlayer(players);
                }
            }
        }
    }

    public void registerArmor(Player player) {
        if (getConfig().getBoolean("ARMOR_RANK") && !noarmorplayers.contains(player)) {
            if (player.hasPermission("MissingHub.rankarmor") || player.hasPermission("MissingHub.rainbow")) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
                    ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
                    ItemStack legging = new ItemStack(Material.LEATHER_LEGGINGS);
                    ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
                    LeatherArmorMeta itemMeta = (LeatherArmorMeta) chestplate.getItemMeta();
                    if (getConfig().getString("RANK_PLUGIN").equalsIgnoreCase("Vault")) {
                        if (perms != null) {
                            if (perms.getPrimaryGroup(player) != null) {
                                itemMeta.setDisplayName(ChatColor.GRAY.toString() + ChatColor.BOLD + perms.getPrimaryGroup(player));
                                if (RanksConfig.getRankConfig().getString(perms.getPrimaryGroup(player).toUpperCase()) != null) {
                                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', RanksConfig.getRankConfig().getString(perms.getPrimaryGroup(player).toUpperCase()) + ChatColor.BOLD + perms.getPrimaryGroup(player)));
                                    itemMeta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', ChatColor.GRAY + "You are currenly rank " + RanksConfig.getRankConfig().getString(perms.getPrimaryGroup(player).toUpperCase()) + perms.getPrimaryGroup(player)) + ChatColor.GRAY + "."));
                                    itemMeta.setColor(ColourUtils.getDyeColor(ColourUtils.format(RanksConfig.getRankConfig().getString(perms.getPrimaryGroup(player).toUpperCase()))));
                                }
                            }
                        }
                    } else if (getConfig().getString("RANK_PLUGIN").equalsIgnoreCase("AquaCore")) {
                        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getColor() + "") + ChatColor.BOLD + AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getName());
                        itemMeta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', ChatColor.GRAY + "You are currenly rank " + ChatColor.translateAlternateColorCodes('&', AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getColor() + "") + AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getName() + ChatColor.GRAY + ".")));
                        itemMeta.setColor(ColourUtils.getDyeColor(AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getColor()));
                    } else if (getConfig().getString("RANK_PLUGIN").equalsIgnoreCase("nCore")) {
                        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', NCoreAPI.instance.getNameColor(player.getUniqueId()) + "" + ChatColor.BOLD + NCoreAPI.instance.getRank(player.getUniqueId())));
                        itemMeta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', ChatColor.GRAY + "You are currenly rank " + NCoreAPI.instance.getNameColor(player.getUniqueId()) + NCoreAPI.instance.getRank(player.getUniqueId()) + ChatColor.GRAY + ".")));
                        itemMeta.setColor(ColourUtils.getDyeColor(ColourUtils.format(unTranslateAlternateColorCodes(NCoreAPI.instance.getNameColor(player.getUniqueId()) + ""))));
                    } else if (getConfig().getString("RANK_PLUGIN").equalsIgnoreCase("HestiaCore")) {
                        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', HestiaAPI.instance.getRankColor(player.getUniqueId()) + "" + ChatColor.BOLD + HestiaAPI.instance.getRank(player.getUniqueId())));
                        itemMeta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', ChatColor.GRAY + "You are currenly rank " + HestiaAPI.instance.getRankColor(player.getUniqueId()) + HestiaAPI.instance.getRank(player.getUniqueId()) + ChatColor.GRAY + ".")));
                        itemMeta.setColor(ColourUtils.getDyeColor(ColourUtils.format(unTranslateAlternateColorCodes(HestiaAPI.instance.getRankColor(player.getUniqueId()) + ""))));
                    } else if (getConfig().getString("RANK_PLUGIN").equalsIgnoreCase("mCore")) {
                        CoreProfile coreProfile = CorePlugin.getInstance().getProfileHandler().getCoreProfile(player);
                        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', coreProfile.getRank().getName()));
                        itemMeta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', ChatColor.GRAY + "You are currenly rank " + coreProfile.getRank().getName() + ChatColor.GRAY + ".")));
                    }
                    chestplate.setItemMeta(itemMeta);
                    legging.setItemMeta(itemMeta);
                    boots.setItemMeta(itemMeta);
                    player.getInventory().setHelmet(null);
                    player.getInventory().setChestplate(chestplate);
                    player.getInventory().setLeggings(legging);
                    player.getInventory().setBoots(boots);
                }, 6);
            }
        } else {
            player.getInventory().setHelmet(null);
            player.getInventory().setChestplate(null);
            player.getInventory().setLeggings(null);
            player.getInventory().setBoots(null);
        }
    }

    public String unTranslateAlternateColorCodes(String text) {
        char[] array = text.toCharArray();
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] == ChatColor.COLOR_CHAR && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(array[i + 1]) != -1) {
                array[i] = '&';
                array[i + 1] = Character.toLowerCase(array[i + 1]);
            }
        }
        return new String(array);
    }
}