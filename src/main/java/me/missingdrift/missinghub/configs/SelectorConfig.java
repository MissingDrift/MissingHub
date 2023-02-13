package me.missigdrift.missinghub.configs;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SelectorConfig {

    private static File selectorFile;
    private static FileConfiguration selectorConfig;

    public static FileConfiguration getSelectorConfig() {
        return selectorConfig;
    }
    public static File getSelectorFile() {
        return selectorFile;
    }

    public MissingHub MissingHub;

    public SelectorConfig(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    public void createSelectorFile() {
        selectorFile = new File(MissingHub.getDataFolder(), "selector.yml");
        if (!selectorFile.exists()) {
            selectorFile.getParentFile().mkdirs();
            MissingHub.saveResource("selector.yml", false);
        }

        selectorConfig = new YamlConfiguration();
        try {
            selectorConfig.load(selectorFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
