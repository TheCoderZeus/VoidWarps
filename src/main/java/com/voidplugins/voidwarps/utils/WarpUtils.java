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

        String pathBase = "inventory.items." + warpName.toLowerCase();
        String pathName = pathBase + ".name";
        String pathLore = pathBase + ".lore";
        String pathMaterial = pathBase + ".material";
        String pathSkull = pathBase + ".skull-texture";

        ItemStack item;
        
        // Verifica se tem textura de skull configurada
        String skullTexture = config.getString(pathSkull);
        if (skullTexture != null && !skullTexture.isEmpty()) {
            item = SkullUtils.createCustomSkull(skullTexture);
        } else {
            // Se não tiver skull, usa o material normal
            Material material;
            try {
                material = Material.valueOf(config.getString(pathMaterial, "ENDER_PEARL").toUpperCase());
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Material inválido na config para warp " + warpName + ", usando ENDER_PEARL");
                material = Material.ENDER_PEARL;
            }
            item = new ItemStack(material);
        }

        String displayName = ChatColor.translateAlternateColorCodes('&', config.getString(pathName, "&e" + warpName));
        List<String> loreList = config.getStringList(pathLore);
        if (loreList.isEmpty()) {
            loreList.add("&7Clique para teleportar");
        }
        
        loreList = loreList.stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(loreList);
        item.setItemMeta(meta);

        return item;
    }
}