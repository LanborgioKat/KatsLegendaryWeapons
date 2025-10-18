package com.thorildsby.katslegendaryweapons.command;

import static com.thorildsby.katslegendaryweapons.KatsLegendaryWeapons.ITEM_MANAGER;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LegendariesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args[0].equals("*")) {
                for (var item : ITEM_MANAGER.getItemIDs())
                    player.getInventory().addItem(ITEM_MANAGER.getHandler(item).getItem());
                return true;
            }

            ItemStack item = ITEM_MANAGER.getHandler(args[0]).getItem();
            if (item == null) return false;
            player.getInventory().addItem(item);
            return true;
        }

        return false;
    }

    public static class TabCompleter implements org.bukkit.command.TabCompleter {
        @Nullable @Override
        public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
            var result =  ITEM_MANAGER.getItemIDs();
            result.add("*");
            return result;
        }
    }
}