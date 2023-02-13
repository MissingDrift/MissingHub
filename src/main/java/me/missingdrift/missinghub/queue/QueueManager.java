package me.missigdrift.missinghub.queue;

import me.missigdrift.missinghub.utils.PluginMessageReceived;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueueManager {

    public MissingHub MissingHub;

    public QueueManager(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }
    
    public me.missigdrift.missinghub.queue.Queue getQueue(String server) {
        for (Queue queue : MissingHub.getQueues()) {
            if (queue.getServer().equalsIgnoreCase(server)) {
                return queue;
            }
        }
        Queue queue = new Queue(server, MissingHub);
        MissingHub.getQueues().add(queue);
        return queue;
    }

    public Queue getQueue(Player player) {
        for (Queue queue : MissingHub.getQueues()) {
            if (queue.getPlayers().containsKey(player)) {
                return queue;
            }
        }
        return null;
    }

    public int getPriority(Player player) {
        for (String number : MissingHub.getConfig().getConfigurationSection("QUEUE_PRIORITY").getKeys(false)) {
            if (player.hasPermission(MissingHub.getConfig().getString("QUEUE_PRIORITY." + number + ".PERMISSION"))) {
                return Integer.parseInt(number);
            }
        }
        return 50;
    }

    public void addToQueue(Queue queue, Player player) {
        PluginMessageReceived pluginMessageReceived = new PluginMessageReceived(MissingHub);
        for(Queue queues : MissingHub.getQueues()){
            if(queues.getPlayers().containsKey(player)){
                player.sendMessage(ChatColor.RED + "You are already in a queue!");
                return;
            }
        }
        if (queue.getPlayers().size() == 0) {
            queue.setWorking(true);
            queue.getPlayers().put(player, 1);
        } else {
            int i = 1;
            for (Map.Entry<Player, Integer> hashmap : queue.getPlayers().entrySet()) {
                Player playerInQueue = hashmap.getKey();
                if (getPriority(playerInQueue) <= getPriority(player)) {
                    i++;
                }
            }
            for (Map.Entry<Player, Integer> hashmap : queue.getPlayers().entrySet()) {
                Player playerInQueue = hashmap.getKey();
                if (hashmap.getValue() >= i) {
                    queue.getPlayers().put(playerInQueue, hashmap.getValue() + 1);
                }
            }
            queue.getPlayers().put(player, i);
        }
        if(queue.isWorking()) {
            queue.setWorking(false);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!queue.isPaused()) {
                        if (queue.getPlayers().size() > 0) {
                            List<Player> goplayers = new ArrayList<>();
                            for(Player players : queue.getPlayers().keySet()){
                                if(goplayers.size() <= MissingHub.getConfig().getInt("QUEUE_JOINS")){
                                    goplayers.add(players);
                                }
                            }
                            for(Player players : goplayers) {
                                leaveQueue(queue, players);
                                pluginMessageReceived.connect(players, queue.getServer());
                            }
                        }
                    }
                }
            }.runTaskTimer(MissingHub, 0, MissingHub.getConfig().getInt("QUEUE_RUNNER") * 20);
        }
        new BukkitRunnable(){
            @Override
            public void run() {
                if(getQueue(player) != null) {
                    for (String string : MissingHub.getConfig().getStringList("QUEUE_UPDATER")) {
                        Queue queue = getQueue(player);
                        if (queue != null) {
                            string = string.replace("{queue_position}", queue.getPlayers().get(player) + "").replace("{queue_max}", queue.getPlayers().size() + "").replace("{queue_name}", queue.getServer());
                        }
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', string));
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(MissingHub, 0, MissingHub.getConfig().getInt("QUEUE_UPDATER_TIME") * 20);
    }

    public void leaveQueue(Queue queue, Player player) {
        int playerPlace = queue.getPlayers().get(player);
        queue.getPlayers().remove(player);
        for (Map.Entry<Player, Integer> hashmap : queue.getPlayers().entrySet()) {
            if (hashmap.getKey().equals(player)) {
                break;
            }
            Player playerInQueue = hashmap.getKey();
            int playerInQueuePlace = hashmap.getValue();
            if (playerPlace < playerInQueuePlace) {
                queue.getPlayers().put(playerInQueue, playerInQueuePlace - 1);
            }
        }
    }
}
