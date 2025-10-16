package com.thorildsby.katslegendaryweapons.item;

import com.thorildsby.katslegendaryweapons.CooldownTracker.CooldownType;
import static com.thorildsby.katslegendaryweapons.KatsLegendaryWeapons.COOLDOWN_TRACKER;
import static com.thorildsby.katslegendaryweapons.Util.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class TelekinesisSword extends Item {
    public TelekinesisSword(JavaPlugin plugin) {
        super(plugin, "TELEKINESIS_SWORD", Material.NETHERITE_SWORD);
    }

    @Override
    protected ItemStack generateItem(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(strForm("*dTelekinesis Sword"));

        ArrayList<String> description = new ArrayList<>();
        description.add(strForm("*7*oID: TELEKINESIS_SWORD"));
        meta.setLore(description);

        meta.setEnchantmentGlintOverride(true);

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!isApplicable(event)) return;

        Action action = event.getAction();
        if (!(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) return;

        Player player = event.getPlayer();

        //TODO: make smoother
        //Ability 1
        if (!player.isSneaking() && !COOLDOWN_TRACKER.isCooldownActive(player, CooldownType.TELEKINESIS_TELEPORT)) {
            Vector direction = player.getLocation().getDirection().multiply(5f);
            Location destination = player.getLocation().add(direction).add(0, 0.5f, 0);

            if (!destination.getBlock().isPassable()) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    new TextComponent(strForm("*cThis location is not safe to teleport to!")));
                player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1f, 1f);
                return;
            }
            player.teleport(destination);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_TELEPORT, 1f, 1f);

            COOLDOWN_TRACKER.scheduleCooldown(player, CooldownType.TELEKINESIS_TELEPORT, t3min);
        }
        //Ability 2
        else if (!COOLDOWN_TRACKER.isCooldownActive(player, CooldownType.TELEKINESIS_LEVITATE)) {
            var candidates = player.getNearbyEntities(10, 10, 10);

            Entity closest = null;
            double closestDistance = 100d;
            for (var candidate : candidates) {
                if (candidate.getType() != EntityType.PLAYER) continue;
                double distance = player.getLocation().distance(candidate.getLocation());
                if (distance < closestDistance) {
                    closest = candidate;
                    closestDistance = distance;
                }
            }

            if (closest == null) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    new TextComponent(strForm("*cNo closest player was found!")));
                player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1f, 1f);
                return;
            }

            new PotionEffect(PotionEffectType.LEVITATION, t10s, 0).apply((LivingEntity) closest);
            COOLDOWN_TRACKER.scheduleCooldown(player, CooldownType.TELEKINESIS_LEVITATE, t10min);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!isApplicable(event)) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) event.setCancelled(true);
    }
}