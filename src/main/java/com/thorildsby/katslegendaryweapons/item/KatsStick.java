package com.thorildsby.katslegendaryweapons.item;

import static com.thorildsby.katslegendaryweapons.Util.strForm;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class KatsStick extends Item {
    public KatsStick(JavaPlugin plugin) {
        super(plugin, "KATS_STICK", Material.STICK);
    }

    @Override
    protected ItemStack generateItem(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(strForm("*eKat's Stick"));

        ArrayList<String> description = new ArrayList<>();
        description.add(strForm("*d*oID: KATS_STICK"));
        meta.setLore(description);

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!isApplicable(event)) return;

        Player player = event.getPlayer();
        player.getWorld().strikeLightningEffect(player.getLocation().add(1, 0, 0));
        player.sendMessage(strForm("*7BANG!"));
    }
}