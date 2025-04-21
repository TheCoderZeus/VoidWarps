package com.voidplugins.voidwarps.utils;

import com.voidplugins.voidwarps.WarpPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class WarpUtils {

    public static ItemStack createWarpItem(String warpName) {
        WarpPlugin plugin = WarpPlugin.getInstance();
        FileConfiguration config = plugin.getConfig();

        String pathName = "inventory.items." + warpName.toLowerCase() + ".name";
        String pathLore = "inventory.items." + warpName.toLowerCase() + ".lore";
        Material material = Material.ENDER_PEARL;

        String displayName = ChatColor.translateAlternateColorCodes('&', config.getString(pathName, "&e" + warpName));
        List<String> loreList = config.getStringList(pathLore);
        if (loreList.isEmpty()) {
            loreList.add("&7Clique para teleportar");
        }
        
        loreList = loreList.stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(loreList);
        item.setItemMeta(meta);

        return item;
    }
}