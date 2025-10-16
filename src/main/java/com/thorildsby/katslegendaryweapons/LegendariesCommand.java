package com.thorildsby.katslegendaryweapons;

import static com.thorildsby.katslegendaryweapons.KatsLegendaryWeapons.ITEM_MANAGER;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LegendariesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            ItemStack item = ITEM_MANAGER.getHandler(args[0]).getItem();
            if (item == null) return false;
            player.getInventory().addItem(item);
        }

        return false;
    }
}