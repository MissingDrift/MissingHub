package me.missigdrift.missinghub.configs;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class JoinItemsConfig {

    private static File joinitemsFile;
    private static FileConfiguration joinitemsConfig;

    public static FileConfiguration getJoinitemsConfig() {
        return joinitemsConfig;
    }
    public static File getJoinitemsFile() {
        return joinitemsFile;
    }

    public MissingHub MissingHub;

    public JoinItemsConfig(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    public void createJoinItemsFile() {
        joinitemsFile = new File(MissingHub.getDataFolder(), "joinitems.yml");
        if (!joinitemsFile.exists()) {
            joinitemsFile.getParentFile().mkdirs();
            MissingHub.saveResource("joinitems.yml", false);
        }

        joinitemsConfig = new YamlConfiguration();
        try {
            joinitemsConfig.load(joinitemsFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}