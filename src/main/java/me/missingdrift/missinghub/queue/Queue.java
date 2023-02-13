package me.missigdrift.missinghub.queue;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class Queue {

    private String server;
    private boolean paused;
    private boolean working;
    private HashMap<Player, Integer> players;

    public Queue(String server, MissingHub MissingHub) {
        this.server = server;
        this.paused = MissingHub.getConfig().getBoolean("AUTO-PAUSE");
        this.working = false;
        this.players = new HashMap<>();
    }

    public String getServer() {
        return server;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public HashMap<Player, Integer> getPlayers() {
        return players;
    }
}
