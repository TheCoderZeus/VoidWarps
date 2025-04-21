package com.voidplugins.voidwarps.config;

import com.voidplugins.voidwarps.WarpPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class WarpsConfig {
    private final WarpPlugin plugin;
    private File configFile;
    private FileConfiguration config;

    public WarpsConfig(WarpPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "warps.yml");
        }

        if (!configFile.exists()) {
            plugin.saveResource("warps.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfig() {
        if (config == null || configFile == null) return;
        
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Não foi possível salvar warps.yml: " + e.getMessage());
        }
    }

    public void reloadConfig() {
        loadConfig();
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            loadConfig();
        }
        return config;
    }

    public void setWarpLocation(String warpName, org.bukkit.Location location) {
        String path = "warps." + warpName;
        config.set(path + ".world", location.getWorld().getName());
        config.set(path + ".x", location.getX());
        config.set(path + ".y", location.getY());
        config.set(path + ".z", location.getZ());
        config.set(path + ".yaw", location.getYaw());
        config.set(path + ".pitch", location.getPitch());
        saveConfig();
    }

    public org.bukkit.Location getWarpLocation(String warpName) {
        String path = "warps." + warpName;
        if (!config.contains(path)) {
            return null;
        }

        String worldName = config.getString(path + ".world");
        if (worldName == null) return null;

        org.bukkit.World world = org.bukkit.Bukkit.getWorld(worldName);
        if (world == null) return null;

        double x = config.getDouble(path + ".x");
        double y = config.getDouble(path + ".y");
        double z = config.getDouble(path + ".z");
        float yaw = (float) config.getDouble(path + ".yaw");
        float pitch = (float) config.getDouble(path + ".pitch");

        return new org.bukkit.Location(world, x, y, z, yaw, pitch);
    }

    public void removeWarp(String warpName) {
        config.set("warps." + warpName, null);
        saveConfig();
    }

    public boolean warpExists(String warpName) {
        return config.contains("warps." + warpName);
    }

    public java.util.Set<String> getWarpNames() {
        org.bukkit.configuration.ConfigurationSection warpsSection = config.getConfigurationSection("warps");
        if (warpsSection == null) {
            return new java.util.HashSet<>();
        }
        return warpsSection.getKeys(false);
    }
}