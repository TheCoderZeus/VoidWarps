package com.voidplugins.voidwarps.config;

import com.voidplugins.voidwarps.WarpPlugin;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WarpConfig {
    private final WarpPlugin plugin;
    private final FileConfiguration config;

    public WarpConfig(WarpPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        loadDefaults();
    }

    private void loadDefaults() {
        if (plugin.getConfig().getConfigurationSection("inventory") == null) {
            plugin.getConfig().set("inventory.size", 27);
            plugin.getConfig().set("inventory.title", "Warps");
            plugin.getConfig().set("inventory.slots.examplewarp", 11); 
            plugin.getConfig().set("inventory.items.examplewarp.name", "§eExample Warp");
            plugin.getConfig().set("inventory.items.examplewarp.lore", Collections.singletonList("§7Clique para teleportar"));
            plugin.saveConfig();
        }
    }

    public int getInventorySize() {
        int size = config.getInt("inventory.size", 27);
        if (size % 9 != 0 || size < 9 || size > 54) {
            return 27;
        }
        return size;
    }

    public String getInventoryTitle() {
        return config.getString("inventory.title", "Warps");
    }

    public int getSlot(String warpName) {
        return config.getInt("inventory.slots." + warpName.toLowerCase(), -1);
    }

    public String getItemName(String warpName) {
        return config.getString("inventory.items." + warpName.toLowerCase() + ".name", warpName);
    }

    public List<String> getItemLore(String warpName) {
        List<String> lore = config.getStringList("inventory.items." + warpName.toLowerCase() + ".lore");
        return lore != null ? lore : Collections.emptyList();
    }

    // Additional methods from your original WarpConfig
    public Set<String> getWarpNames() {
        ConfigurationSection warpsSection = config.getConfigurationSection("warps");
        return warpsSection != null ? warpsSection.getKeys(false) : Set.of();
    }

    public boolean warpExists(String warpName) {
        return config.isConfigurationSection("warps." + warpName.toLowerCase());
    }

    public Location getWarpLocation(String warpName) {
        String path = "warps." + warpName.toLowerCase();
        
        if (!config.isConfigurationSection(path)) {
            return null;
        }
        
        String worldName = config.getString(path + ".world");
        double x = config.getDouble(path + ".x");
        double y = config.getDouble(path + ".y");
        double z = config.getDouble(path + ".z");
        float yaw = (float) config.getDouble(path + ".yaw", 0.0);
        float pitch = (float) config.getDouble(path + ".pitch", 0.0);
        
        return new Location(plugin.getServer().getWorld(worldName), x, y, z, yaw, pitch);
    }

    public String getWarpPermission(String warpName) {
        return config.getString("warps." + warpName.toLowerCase() + ".permission", "voidwarps.warp." + warpName.toLowerCase());
    }

    public void setWarp(String warpName, Location location, String permission) {
        String path = "warps." + warpName.toLowerCase();
        
        config.set(path + ".world", location.getWorld().getName());
        config.set(path + ".x", location.getX());
        config.set(path + ".y", location.getY());
        config.set(path + ".z", location.getZ());
        config.set(path + ".yaw", location.getYaw());
        config.set(path + ".pitch", location.getPitch());
        config.set(path + ".permission", permission);
        
        plugin.saveConfig();
    }

    public void removeWarp(String warpName) {
        config.set("warps." + warpName.toLowerCase(), null);
        plugin.saveConfig();
    }

    public Map<String, Location> getAllWarps() {
        Map<String, Location> warps = new HashMap<>();
        ConfigurationSection warpsSection = config.getConfigurationSection("warps");
        
        if (warpsSection != null) {
            for (String warpName : warpsSection.getKeys(false)) {
                warps.put(warpName, getWarpLocation(warpName));
            }
        }
        
        return warps;
    }
}