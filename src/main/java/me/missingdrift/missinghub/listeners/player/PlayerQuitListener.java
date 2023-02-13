package me.missigdrift.missinghub.listeners.player;

import me.missigdrift.missinghub.queue.Queue;
import me.missigdrift.missinghub.queue.QueueManager;
import me.missigdrift.missinghub.tab.TabManager;
import me.missigdrift.missinghub.utils.ScoreHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    public MissingHub MissingHub;
    public QueueManager queueManager;

    public PlayerQuitListener(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
        this.queueManager = MissingHub.queueManager;
    }

    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null);
        ScoreHelper.removeScore(player);
        Queue queue = queueManager.getQueue(player);
        TabManager.getEnable().put(player, false);
        if (queue != null) {
            queueManager.leaveQueue(queue, player);
        }
        if (MissingHub.getConfig().getBoolean("QUIT_MESSAGE.ENABLED")) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', MissingHub.getConfig().getString("QUIT_MESSAGE.MESSAGE").replace("{Player}", player.getName())));
        }
    }
}
