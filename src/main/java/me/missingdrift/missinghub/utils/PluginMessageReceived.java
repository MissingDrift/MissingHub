package me.missigdrift.missinghub.utils;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.List;

public class PluginMessageReceived implements PluginMessageListener {

    public MissingHub MissingHub;
    public List<Player> players = new ArrayList<>();

    public PluginMessageReceived(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    public void playerCount(String server) {
        if (server == null) {
            server = "ALL";
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF(server);

        Bukkit.getServer().sendPluginMessage(MissingHub, "BungeeCord", out.toByteArray());
    }

    public void getServers() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServers");
        Bukkit.getServer().sendPluginMessage(MissingHub, "BungeeCord", out.toByteArray());
    }

    public void connect(Player player, String server) {
        if(!players.contains(player)) {
            players.add(player);
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(MissingHub, "BungeeCord", out.toByteArray());
            Bukkit.getScheduler().scheduleSyncDelayedTask(MissingHub, () -> players.remove(player), 20);
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("PlayerCount")) {
            String server = in.readUTF();
            int playerCount = in.readInt();
            MissingHub.getPlayercount().put(server, playerCount);
        }
        if (subchannel.equals("GetServers")) {
            String[] serverList = in.readUTF().split(", ");
            for (String s : serverList) {
                if (!MissingHub.getServers().contains(s)) {
                    MissingHub.getServers().add(s);
                }
            }
        }
    }

}
