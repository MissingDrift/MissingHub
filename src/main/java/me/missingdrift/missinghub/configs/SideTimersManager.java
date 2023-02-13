package me.missigdrift.missinghub.configs;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class SideTimersManager {

    private File sidetimerFile;
    private FileConfiguration sidetimerConfig;

    public FileConfiguration getSidetimerConfig() {
        return sidetimerConfig;
    }
    public File getSidetimerFile() {
        return sidetimerFile;
    }

    public MissingHub MissingHub;

    public SideTimersManager(MissingHub MissingHub) {
        this.MissingHub = MissingHub;
    }

    public void createSideTimer() {
        sidetimerFile = new File(MissingHub.getDataFolder(), "sidetimers.yml");
        if (!sidetimerFile.exists()) {
            sidetimerFile.getParentFile().mkdirs();
            MissingHub.saveResource("sidetimers.yml", false);
        }

        sidetimerConfig = new YamlConfiguration();
        try {
            sidetimerConfig.load(sidetimerFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig(MissingHub MissingHub) {
        if (sidetimerFile == null) {
            sidetimerFile = new File(MissingHub.getDataFolder(), "sidetimers.yml");
        }
        sidetimerConfig = YamlConfiguration.loadConfiguration(sidetimerFile);

        InputStream defConfigStream = MissingHub.getResource("sidetimers.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(sidetimerFile);
            sidetimerConfig.setDefaults(defConfig);
        }
    }
}