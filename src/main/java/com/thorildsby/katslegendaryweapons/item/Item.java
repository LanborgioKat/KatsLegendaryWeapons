package com.thorildsby.katslegendaryweapons.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

//Thanks to buooobuoo on the Spigot forums
public abstract class Item implements Listener {
    protected final JavaPlugin plugin;
    public final String itemID;
    public final Material material;

    public Item(JavaPlugin plugin, String itemID, Material material) {
        this.plugin = plugin;
        this.itemID = itemID;
        this.material = material;
    }

    protected boolean isApplicable(PlayerInteractEvent event) {
        return isApplicable(event.getPlayer().getInventory().getItemInMainHand());
    }

    public boolean isApplicable(@NotNull ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) return false;

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return false;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        String pdcID = pdc.get(new NamespacedKey(plugin, "ITEM_ID"), PersistentDataType.STRING);
        return itemID.equals(pdcID);
    }

    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(new NamespacedKey(plugin, "ITEM_ID"), PersistentDataType.STRING, itemID);

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    protected abstract ItemStack generateItem(ItemStack itemStack);
}