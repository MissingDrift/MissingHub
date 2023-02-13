package me.missigdrift.missinghub.configs;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class RanksConfig {

    private static File rankFile;
    private static FileConfiguration rankConfig;

    public static FileConfiguration getRankConfig() {
        return rankConfig;
    }
    public static File getRankFile() {
        return rankFile;
    }

    public MissingHub MissingHub;

    public RanksConfig(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    public void createRankFile() {
        rankFile = new File(MissingHub.getDataFolder(), "ranks.yml");
        if (!rankFile.exists()) {
            rankFile.getParentFile().mkdirs();
            MissingHub.saveResource("ranks.yml", false);
        }

        rankConfig = new YamlConfiguration();
        try {
            rankConfig.load(rankFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}