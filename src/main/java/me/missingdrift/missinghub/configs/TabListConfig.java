package me.missigdrift.missinghub.configs;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class TabListConfig {

    private static File tablistFile;
    private static FileConfiguration tablistConfig;

    public static FileConfiguration getTablistConfig() {
        return tablistConfig;
    }
    public static File getTablistFile() {
        return tablistFile;
    }

    public MissingHub MissingHub;

    public TabListConfig(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    public void createTabListFile() {
        tablistFile = new File(MissingHub.getDataFolder(), "tablist.yml");
        if (!tablistFile.exists()) {
            tablistFile.getParentFile().mkdirs();
            MissingHub.saveResource("tablist.yml", false);
        }

        tablistConfig = new YamlConfiguration();
        try {
            tablistConfig.load(tablistFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}