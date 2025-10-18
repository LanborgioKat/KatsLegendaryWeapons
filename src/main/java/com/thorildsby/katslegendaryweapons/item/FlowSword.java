package com.thorildsby.katslegendaryweapons.item;

import com.thorildsby.katslegendaryweapons.CooldownTracker.CooldownType;
import static com.thorildsby.katslegendaryweapons.KatsLegendaryWeapons.COOLDOWN_TRACKER;
import static com.thorildsby.katslegendaryweapons.Util.*;
import com.thorildsby.katslegendaryweapons.event.WeaponAbilityOneEvent;
import com.thorildsby.katslegendaryweapons.event.WeaponAbilityTwoEvent;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.ArrayList;

public class FlowSword extends Item {
    public FlowSword(JavaPlugin plugin) {
        super(plugin, "FLOW_SWORD", Material.NETHERITE_SWORD);
    }

    @Override
    protected ItemStack generateItem(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(strForm("*dFlow Sword"));

        ArrayList<String> description = new ArrayList<>();
        description.add(strForm("*ePassive: Holder has the Conduit Power effect"));
        description.add(strForm("*5Flow: Gain some positive effects for 30 seconds (5min CD)"));
        description.add(strForm("*5Shambles: Clear all effects from the nearest player (8min CD)"));
        description.add(strForm("*7*oID: FLOW_SWORD"));
        meta.setLore(description);

        meta.setEnchantmentGlintOverride(true);

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @EventHandler
    public void onPlayerHold(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack ney = player.getInventory().getItem(event.getNewSlot());
        ItemStack previous = player.getInventory().getItem(event.getPreviousSlot());

        if (ney != null && isApplicable(ney))
            new PotionEffect(PotionEffectType.CONDUIT_POWER, Integer.MAX_VALUE, 0).apply(player);
        else if (previous != null && isApplicable(previous))
            player.removePotionEffect(PotionEffectType.CONDUIT_POWER);
    }

    @EventHandler
    public void abilityOne(WeaponAbilityOneEvent event) {
        if (!isApplicable(event)) return;
        Player player = event.getPlayer();

        if (!COOLDOWN_TRACKER.isCooldownActive(player, CooldownType.FLOW_FLOW)) {
            new PotionEffect(PotionEffectType.SATURATION, t30s, 0).apply(player);
            new PotionEffect(PotionEffectType.SPEED, t30s, 1).apply(player);
            new PotionEffect(PotionEffectType.JUMP_BOOST, t30s, 0).apply(player);

            player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_DEACTIVATE,1f, 1f);
            COOLDOWN_TRACKER.scheduleCooldown(player, CooldownType.FLOW_FLOW, t5min);
        }
        else noAbilityMessage(player, CooldownType.FLOW_FLOW);
    }

    @EventHandler
    public void abilityTwo(WeaponAbilityTwoEvent event) {
        if (!isApplicable(event)) return;
        Player player = event.getPlayer();

        if (!COOLDOWN_TRACKER.isCooldownActive(player, CooldownType.FLOW_SHAMBLES)) {
            var shambled = getClosestPlayer(player, 10);

            if (shambled == null) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    new TextComponent(strForm("*cNo closest player was found!")));
                player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1f, 1f);
                return;
            }

            var effects = shambled.getActivePotionEffects();
            for (var effect : effects) {
                shambled.removePotionEffect(effect.getType());
            }

            player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1f, 1f);
            COOLDOWN_TRACKER.scheduleCooldown(player, CooldownType.FLOW_SHAMBLES, t8min);
        }
        else noAbilityMessage(player, CooldownType.FLOW_SHAMBLES);
    }
}