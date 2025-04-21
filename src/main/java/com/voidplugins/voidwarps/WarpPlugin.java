package com.voidplugins.voidwarps;

import co.aikar.commands.PaperCommandManager;
import com.voidplugins.voidwarps.commands.WarpCommand;
import com.voidplugins.voidwarps.config.WarpConfig;
import com.voidplugins.voidwarps.config.MessagesConfig;
import com.voidplugins.voidwarps.config.WarpsConfig;
import com.voidplugins.voidwarps.data.WarpStorage;
import com.voidplugins.voidwarps.events.WarpEventListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class WarpPlugin extends JavaPlugin {

    private static WarpPlugin instance;

    private WarpConfig configManager;
    private WarpsConfig warpsConfig;
    private MessagesConfig messagesConfig;

    private WarpStorage warpStorage;

    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        saveResourceIfNotExists("warps.yml");
        saveResourceIfNotExists("messages.yml");

        this.configManager = new WarpConfig(this);
        this.warpsConfig = new WarpsConfig(this);
        this.messagesConfig = new MessagesConfig(this);

        warpStorage = new WarpStorage(this);
        warpStorage.loadWarps();

        commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new WarpCommand());

        getServer().getPluginManager().registerEvents(new WarpEventListener(), this);
    }

    @Override
    public void onDisable() {
        warpStorage.saveWarps();
    }

    private void saveResourceIfNotExists(String resourceName) {
        File file = new File(getDataFolder(), resourceName);
        if (!file.exists()) {
            saveResource(resourceName, false);
        }
    }

    public static WarpPlugin getInstance() {
        return instance;
    }

    public WarpConfig getConfigManager() {
        return configManager;
    }

    public WarpsConfig getWarpsConfig() {
        return warpsConfig;
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    public WarpStorage getWarpStorage() {
        return warpStorage;
    }

    public FileConfiguration getWarpsFileConfiguration() {
        return warpsConfig.getConfig();
    }

    public FileConfiguration getMessagesFileConfiguration() {
        return messagesConfig.getConfig();
    }
}