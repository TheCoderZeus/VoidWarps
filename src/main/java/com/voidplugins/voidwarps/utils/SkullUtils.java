package com.voidplugins.voidwarps.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class SkullUtils {
    
    public static ItemStack createCustomSkull(String value) {
        ItemStack skull = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        
        if (value != null && !value.isEmpty()) {
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            profile.getProperties().put("textures", new Property("textures", value));
            
            try {
                Field profileField = meta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(meta, profile);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        
        skull.setItemMeta(meta);
        return skull;
    }
}