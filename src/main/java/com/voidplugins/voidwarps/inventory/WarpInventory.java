package com.voidplugins.voidwarps.inventory;

import com.voidplugins.voidwarps.WarpPlugin;
import com.voidplugins.voidwarps.config.WarpConfig;
import com.voidplugins.voidwarps.data.WarpStorage;
import com.voidplugins.voidwarps.models.Warp;
import com.voidplugins.voidwarps.utils.WarpUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class WarpInventory {

    private final Player player;
    private final WarpPlugin plugin = WarpPlugin.getInstance();
    private final WarpConfig config = plugin.getConfigManager();
    private final WarpStorage storage = plugin.getWarpStorage();

    public WarpInventory(Player player) {
        this.player = player;
    }

    public void open() {
        int size = config.getInventorySize();
        String title = config.getInventoryTitle();
        Inventory inv = Bukkit.createInventory(null, size, title);

        Set<String> warps = storage.getWarpsName();

        for (String warpName : warps) {
            int slot = config.getSlot(warpName);
            if (slot < 0 || slot >= size) continue;

            ItemStack item = WarpUtils.createWarpItem(warpName);
            inv.setItem(slot, item);
        }
        player.openInventory(inv);
    }
}