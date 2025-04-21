package com.voidplugins.voidwarps.data;

import com.voidplugins.voidwarps.WarpPlugin;
import com.voidplugins.voidwarps.models.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WarpStorage {
    private final WarpPlugin plugin;
    private final FileConfiguration config;
    private final Map<String, Warp> warpMap = new HashMap<>();
    private final File warpsFile;

    public WarpStorage(WarpPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getWarpsFileConfiguration();
        this.warpsFile = new File(plugin.getDataFolder(), "warps.yml");
        loadWarps();
    }

    public void loadWarps() {
        warpMap.clear();
        
        if (!warpsFile.exists()) {
            try {
                warpsFile.createNewFile();
                config.createSection("warps");
                config.save(warpsFile);
            } catch (IOException e) {
                plugin.getLogger().severe("Erro ao criar warps.yml: " + e.getMessage());
                return;
            }
        }

        ConfigurationSection warpsSection = config.getConfigurationSection("warps");
        if (warpsSection == null) {
            config.createSection("warps");
            return;
        }

        for (String key : warpsSection.getKeys(false)) {
            if (!isWarpConfigured(key)) {
                continue;
            }

            ConfigurationSection warpSection = warpsSection.getConfigurationSection(key);
            if (warpSection == null) continue;
            
            if (!warpSection.contains("world") || !warpSection.contains("x") || !warpSection.contains("y") || !warpSection.contains("z")) continue;

            String world = warpSection.getString("world");
            double x = warpSection.getDouble("x");
            double y = warpSection.getDouble("y");
            double z = warpSection.getDouble("z");
            float yaw = (float) warpSection.getDouble("yaw", 0.0);
            float pitch = (float) warpSection.getDouble("pitch", 0.0);

            Location loc = null;
            if (Bukkit.getWorld(world) != null) {
                loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
            }

            warpMap.put(key.toLowerCase(), new Warp(key.toLowerCase(), loc));
        }
    }

    public void saveWarps() {
        config.set("warps", null);
        
        for (Map.Entry<String, Warp> entry : warpMap.entrySet()) {
            String key = entry.getKey();
            Warp warp = entry.getValue();
            
            if (!isWarpConfigured(key)) {
                continue;
            }
            
            if (warp == null || warp.getLocation() == null) continue;
            
            String path = "warps." + key;
            config.set(path + ".world", warp.getLocation().getWorld().getName());
            config.set(path + ".x", warp.getLocation().getX());
            config.set(path + ".y", warp.getLocation().getY());
            config.set(path + ".z", warp.getLocation().getZ());
            config.set(path + ".yaw", warp.getLocation().getYaw());
            config.set(path + ".pitch", warp.getLocation().getPitch());
        }

        try {
            config.save(warpsFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Erro ao salvar warps.yml: " + e.getMessage());
        }
    }

    public Warp getWarp(String name) {
        if (name == null) return null;
        String key = name.toLowerCase();
        if (!isWarpConfigured(key)) {
            return null;
        }
        return warpMap.get(key);
    }

    public void setWarp(String name, Warp warp) {
        if (name == null) return;
        String key = name.toLowerCase();
        
        if (!isWarpConfigured(key)) {
            return;
        }
        
        if (warp == null) {
            warpMap.remove(key);
        } else {
            warpMap.put(key, warp);
        }
        
        saveWarps();
    }

    public Set<String> getWarpsName() {
        warpMap.keySet().removeIf(warp -> !isWarpConfigured(warp));
        return Collections.unmodifiableSet(warpMap.keySet());
    }
    
    private boolean isWarpConfigured(String warpName) {
        FileConfiguration config = plugin.getConfig();
        return config.contains("inventory.slots." + warpName.toLowerCase()) && 
               config.contains("inventory.items." + warpName.toLowerCase());
    }
}