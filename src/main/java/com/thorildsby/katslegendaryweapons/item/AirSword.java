package com.thorildsby.katslegendaryweapons.item;

import static com.thorildsby.katslegendaryweapons.Util.strForm;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;

public class AirSword extends Item {
    public AirSword(JavaPlugin plugin) {
        super(plugin, "AIR_SWORD", Material.NETHERITE_SWORD);
    }

    @Override
    protected ItemStack generateItem(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(strForm("*dAir Sword"));

        ArrayList<String> description = new ArrayList<>();
        description.add(strForm("*ePassive: Holder doesn't take fall damage"));
        description.add(strForm("*bDouble jump: Press your jump key in the air to perform a double jump (30s CD)"));
        description.add(strForm("*5Wind Canon: Launch ten blocks into the direction you are looking (3min CD)"));
        description.add(strForm("*5Tornado: Launches a powerful tornado at nearby players (10min CD)"));
        description.add(strForm("*7*oID: FLOW_SWORD"));
        meta.setLore(description);

        meta.setEnchantmentGlintOverride(true);

        itemStack.setItemMeta(meta);
        return itemStack;
    }
}