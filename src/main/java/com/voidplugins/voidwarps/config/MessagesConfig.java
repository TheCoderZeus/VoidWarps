package com.voidplugins.voidwarps.config;

import com.voidplugins.voidwarps.WarpPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MessagesConfig {
    private final WarpPlugin plugin;
    private FileConfiguration config;
    private File configFile;

    public MessagesConfig(WarpPlugin plugin) {
        this.plugin = plugin;
        setup();
        loadDefaults();
    }

    private void setup() {
        configFile = new File(plugin.getDataFolder(), "messages.yml");
        
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("messages.yml", false);
        }
        
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    private void loadDefaults() {
        if (config.getConfigurationSection("messages") == null) {
            config.set("messages.warp.success", "&aVocê foi teleportado para o warp %warp%!");
            config.set("messages.warp.notfound", "&cEsse warp não existe!");
            config.set("messages.warp.nopermission", "&cVocê não tem permissão para acessar esse warp!");
            config.set("messages.warp.create", "&aWarp %warp% criado com sucesso!");
            config.set("messages.warp.remove", "&aWarp %warp% removido com sucesso!");
            saveConfig();
        }
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save messages.yml!");
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public String getMessage(String path) {
        return config.getString("messages." + path, "Missing message: " + path);
    }

    public String getColoredMessage(String path) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', getMessage(path));
    }

    public String getColoredMessage(String path, String... replacements) {
        String message = getMessage(path);
        
        if (replacements != null && replacements.length % 2 == 0) {
            for (int i = 0; i < replacements.length; i += 2) {
                message = message.replace(replacements[i], replacements[i + 1]);
            }
        }
        
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }

    public List<String> getMessageList(String path) {
        List<String> messages = config.getStringList("messages." + path);
        return messages != null ? messages : Collections.emptyList();
    }
}