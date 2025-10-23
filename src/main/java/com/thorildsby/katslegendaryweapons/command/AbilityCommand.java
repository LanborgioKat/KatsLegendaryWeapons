package com.thorildsby.katslegendaryweapons.command;

import com.thorildsby.katslegendaryweapons.event.AbilityOneEvent;
import com.thorildsby.katslegendaryweapons.event.AbilityTwoEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AbilityCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args[0].equals("1")) {
            Bukkit.getPluginManager().callEvent(new AbilityOneEvent((Player) sender));
            return true;
        }
        if (args[0].equals("2")) {
            Bukkit.getPluginManager().callEvent(new AbilityTwoEvent((Player) sender));
            return true;
        }
        return false;
    }

    public static class TabCompleter implements org.bukkit.command.TabCompleter {
        @Nullable @Override
        public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
            ArrayList<String> result = new ArrayList<>();
            result.add("1");
            result.add("2");
            return result;
        }
    }
}