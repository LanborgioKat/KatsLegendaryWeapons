package com.thorildsby.katslegendaryweapons;

import com.thorildsby.katslegendaryweapons.item.CustomItemManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

//TODO: commands
@SuppressWarnings("unused")
public final class KatsLegendaryWeapons extends JavaPlugin {
    public static Logger LOGGER;
    public static CustomItemManager ITEM_MANAGER;

    @Override
    public void onEnable() {
        LOGGER = getLogger();
        ITEM_MANAGER = new CustomItemManager(this);
        Objects.requireNonNull(getCommand("legendaries")).setExecutor(new LegendariesCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerEvents(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }
}