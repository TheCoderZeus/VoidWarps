package com.voidplugins.voidwarps.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.voidplugins.voidwarps.WarpPlugin;
import com.voidplugins.voidwarps.data.WarpStorage;
import com.voidplugins.voidwarps.inventory.WarpInventory;
import com.voidplugins.voidwarps.models.Warp;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

@CommandAlias("warp|warps")
public class WarpCommand extends BaseCommand {

    private final WarpPlugin plugin = WarpPlugin.getInstance();
    private final WarpStorage storage = plugin.getWarpStorage();

    @Default
    @CommandPermission("warps.use")
    public void onWarps(Player player) {
        if (storage.getWarpsName().isEmpty()) {
            player.sendMessage(plugin.getMessagesConfig().getConfig().getString("messages.no-warps",
                    "§cNenhuma warp configurada."));
            return;
        }
        new WarpInventory(player).open();
    }

    @Default
    @CommandCompletion("@warps")
    @Syntax("<nome>")
    @CommandPermission("warps.use")
    public void onWarp(Player player, String warpName) {
        Set<String> warps = storage.getWarpsName();
        if (!warps.contains(warpName.toLowerCase())) {
            player.sendMessage(plugin.getMessagesConfig().getConfig().getString("messages.warp-not-found", "§cWarp não encontrada."));
            return;
        }
        Warp warp = storage.getWarp(warpName.toLowerCase());
        if (warp == null || warp.getLocation() == null) {
            player.sendMessage(plugin.getMessagesConfig().getConfig().getString("messages.warp-no-location", "§cWarp não possui localização definida."));
            return;
        }
        player.teleport(warp.getLocation());
        String msg = plugin.getMessagesConfig().getConfig().getString("messages.warp-teleport",
                "§aTeleportado para a warp §f" + warp.getName());
        player.sendMessage(msg.replace("{warp}", warp.getName()));
    }

    @Subcommand("set")
    @Syntax("<nome>")
    @CommandPermission("warps.set")
    public void onSet(Player player, String warpName) {
        Location loc = player.getLocation();
        Warp warp = new Warp(warpName.toLowerCase(), loc);
        storage.setWarp(warpName.toLowerCase(), warp);
        storage.saveWarps();
        String msg = plugin.getMessagesConfig().getConfig().getString("messages.warp-set",
                "§aWarp §f{warp} §asetada na sua localização atual.");
        player.sendMessage(msg.replace("{warp}", warpName));
    }

    @Subcommand("list")
    @CommandPermission("warps.list")
    public void onList(Player player) {
        if (storage.getWarpsName().isEmpty()) {
            player.sendMessage(plugin.getMessagesConfig().getConfig().getString("messages.no-warps",
                    "§cNenhuma warp configurada."));
            return;
        }
        new WarpInventory(player).open();
    }

    @Subcommand("del|delete|remove")
    @Syntax("<nome>")
    @CommandPermission("warps.admin")
    public void onDelete(Player player, String warpName) {
        if (!storage.getWarpsName().contains(warpName.toLowerCase())) {
            player.sendMessage(plugin.getMessagesConfig().getConfig().getString("messages.warp-not-found", "§cWarp não encontrada."));
            return;
        }
        storage.setWarp(warpName.toLowerCase(), null);
        storage.saveWarps();
        String msg = plugin.getMessagesConfig().getConfig().getString("messages.warp-removed",
                "§cWarp §f{warp} §cremovida com sucesso.");
        player.sendMessage(msg.replace("{warp}", warpName));
    }
}