package com.thorildsby.katslegendaryweapons.item;

import com.thorildsby.katslegendaryweapons.CooldownTracker;
import static com.thorildsby.katslegendaryweapons.KatsLegendaryWeapons.COOLDOWN_TRACKER;
import static com.thorildsby.katslegendaryweapons.Util.strForm;
import com.thorildsby.katslegendaryweapons.event.JumpEvent;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
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

    protected final boolean isApplicable(PlayerEvent event) {
        return isApplicable(event.getPlayer().getInventory().getItemInMainHand());
    }

    protected final boolean isApplicable(EntityEvent event) {
        return event.getEntity() instanceof Player player && isApplicable(player.getInventory().getItemInMainHand());
    }

    protected final boolean isApplicable(EntityDamageByEntityEvent event) {
        return event.getDamager() instanceof Player player && isApplicable(player.getInventory().getItemInMainHand());
    }
  
    protected final boolean isApplicable(JumpEvent event) {
        return isApplicable(event.getPlayer().getInventory().getItemInMainHand());
    }

    protected static void noAbilityMessage(Player player, CooldownTracker.CooldownType type) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
            new TextComponent(strForm("*cThis ability will be available in " + COOLDOWN_TRACKER.getCooldown(player, type)/20 + "s!")));
        player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1f, 1f);
    }

    public final boolean isApplicable(@NotNull ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) return false;

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return false;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        String pdcID = pdc.get(new NamespacedKey(plugin, "ITEM_ID"), PersistentDataType.STRING);
        return itemID.equals(pdcID);
    }

    public final ItemStack getItem() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(new NamespacedKey(plugin, "ITEM_ID"), PersistentDataType.STRING, itemID);

        itemStack.setItemMeta(meta);

        itemStack = generateItem(itemStack);
        return itemStack;
    }

    protected abstract ItemStack generateItem(ItemStack itemStack);
}