package com.thorildsby.katslegendaryweapons.item;

import com.thorildsby.katslegendaryweapons.KatsLegendaryWeapons;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CustomItemManager {
    private final KatsLegendaryWeapons plugin;
    private final Set<Item> itemRegistry = new HashSet<>();

    public CustomItemManager(KatsLegendaryWeapons plugin) {
        this.plugin = plugin;

        registerHandler(new KatsStick(plugin));
        registerHandler(new TelekinesisSword(plugin));
        registerHandler(new FlowSword(plugin));
    }

    private void registerHandler(Item handler) {
        itemRegistry.add(handler);
        plugin.registerEvents(handler);
    }

    public Item getHandler(String itemID) {
        for (Item handler : itemRegistry) if (handler.itemID.equals(itemID)) return handler;
        return null;
    }

    public ArrayList<String> getItemIDs() {
        ArrayList<String> result = new ArrayList<>();
        for (var item : itemRegistry) result.add(item.itemID);
        return result;
    }
}