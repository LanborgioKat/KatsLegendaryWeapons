package com.thorildsby.katslegendaryweapons.item;

import com.thorildsby.katslegendaryweapons.KatsLegendaryWeapons;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class CustomItemManager {
    private final KatsLegendaryWeapons plugin;
    private final Set<Item> itemRegistry = new HashSet<>();

    public CustomItemManager(KatsLegendaryWeapons plugin) {
        this.plugin = plugin;

        registerHandler(new KatsStick(plugin));
        registerHandler(new TelekinesisSword(plugin));
    }

    private void registerHandler(Item handler) {
        itemRegistry.add(handler);
        plugin.registerEvents(handler);
    }

    @Nullable
    public <T> T getHandler(Class<? extends T> clazz) {
        for (Item handler : itemRegistry) if (handler.getClass().equals(clazz)) //noinspection unchecked
            return (T) handler;
        return null;
    }

    public Item getHandler(String itemID) {
        for (Item handler : itemRegistry) if (handler.itemID.equals(itemID)) return handler;
        return null;
    }
}