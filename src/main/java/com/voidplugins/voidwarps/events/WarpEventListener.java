package com.voidplugins.voidwarps.events;

import com.voidplugins.voidwarps.WarpPlugin;
import com.voidplugins.voidwarps.config.WarpConfig;
import com.voidplugins.voidwarps.data.WarpStorage;
import com.voidplugins.voidwarps.models.Warp;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WarpEventListener implements Listener {

    private final WarpPlugin plugin = WarpPlugin.getInstance();
    private final WarpStorage storage = plugin.getWarpStorage();
    private final WarpConfig config = plugin.getConfigManager();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (!event.getView().getTitle().equals(config.getInventoryTitle())) return;
        event.setCancelled(true);
        if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;
        int clickedSlot = event.getSlot();
        for (String warpName : storage.getWarpsName()) {
            int slot = config.getSlot(warpName);
            if (slot == clickedSlot) {
                Warp warp = storage.getWarp(warpName);
                if (warp == null || warp.getLocation() == null) return;
                if (event.getWhoClicked() instanceof org.bukkit.entity.Player) {
                    org.bukkit.entity.Player player = (org.bukkit.entity.Player) event.getWhoClicked();
                    player.teleport(warp.getLocation());
                    String msg = plugin.getMessagesConfig().getConfig().getString("messages.warp-teleport",
                            "§aTeleportado para a warp §f" + warp.getName());
                    player.sendMessage(msg.replace("{warp}", warp.getName()));
                }
                return;
            }
        }
    }
}