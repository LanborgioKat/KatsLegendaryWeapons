package com.thorildsby.katslegendaryweapons.command;

import static com.thorildsby.katslegendaryweapons.KatsLegendaryWeapons.COOLDOWN_TRACKER;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CooldownRemoveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player player) {
            COOLDOWN_TRACKER.clearAll(player);
            return true;
        }
        return false;
    }
}
