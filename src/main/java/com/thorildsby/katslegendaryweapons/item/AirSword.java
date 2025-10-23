package com.thorildsby.katslegendaryweapons.item;

import com.thorildsby.katslegendaryweapons.CooldownTracker.CooldownType;
import static com.thorildsby.katslegendaryweapons.KatsLegendaryWeapons.COOLDOWN_TRACKER;
import static com.thorildsby.katslegendaryweapons.Util.*;
import com.thorildsby.katslegendaryweapons.event.AbilityTwoEvent;
import com.thorildsby.katslegendaryweapons.event.JumpEvent;
import com.thorildsby.katslegendaryweapons.event.AbilityOneEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import static java.lang.Math.*;

public class AirSword extends Item {
    private final HashSet<LivingEntity> fallDamageExempt = new HashSet<>();
    private final HashSet<Tornado> activeTornados = new HashSet<>();

    public AirSword(JavaPlugin plugin) {
        super(plugin, "AIR_SWORD", Material.NETHERITE_SWORD);
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (var tornado : activeTornados) tornado.tick();
        }, 0L, 1L);
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
        description.add(strForm("*7*oID: AIR_SWORD"));
        meta.setLore(description);

        meta.setEnchantmentGlintOverride(true);

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        boolean causeCheck = event.getCause() == EntityDamageEvent.DamageCause.FALL;
        if (event.getEntity() instanceof LivingEntity entity && fallDamageExempt.contains(entity) && causeCheck) {
            fallDamageExempt.remove(entity);
            event.setCancelled(true);
            return;
        }

        if (!isApplicable(event)) return;
        if (causeCheck) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJump(JumpEvent event) {
        if (!isApplicable(event)) return;

        Player player = event.getPlayer();
        //noinspection deprecation
        if (player.isOnGround()) return; //How the fuck is this even deprecated?
        if (player.isInWater()) return;
        if (player.isFlying()) return;

        if (COOLDOWN_TRACKER.isCooldownActive(player, CooldownType.AIR_DOUBLE_JUMP)) {
            noAbilityMessage(player, CooldownType.AIR_DOUBLE_JUMP);
            return;
        }

        player.setVelocity(player.getLocation().getDirection().multiply(0.5).setY(1));
        player.spawnParticle(Particle.EXPLOSION, player.getLocation(), 5);
        player.playSound(player.getLocation(), Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1f, 1f);

        COOLDOWN_TRACKER.scheduleCooldown(player, CooldownType.AIR_DOUBLE_JUMP, t30s);
        fallDamageExempt.add(player);
    }

    @EventHandler
    public void abilityOne(AbilityOneEvent event) {
        if (!isApplicable(event)) return;
        Player player = event.getPlayer();

        if (COOLDOWN_TRACKER.isCooldownActive(player, CooldownType.AIR_WIND_CANON)) {
            noAbilityMessage(player, CooldownType.AIR_WIND_CANON);
            return;
        }

        player.setVelocity(player.getLocation().getDirection().multiply(2));
        player.spawnParticle(Particle.EXPLOSION, player.getLocation(), 5);
        player.playSound(player.getLocation(), Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1f, 1f);

        COOLDOWN_TRACKER.scheduleCooldown(player, CooldownType.AIR_WIND_CANON, t3min);
        fallDamageExempt.add(player);
    }

    @EventHandler
    public void abilityTwo(AbilityTwoEvent event) {
        if (!isApplicable(event)) return;
        Player player = event.getPlayer();

        if (COOLDOWN_TRACKER.isCooldownActive(player, CooldownType.AIR_TORNADO)) {
            noAbilityMessage(player, CooldownType.AIR_TORNADO);
            return;
        }

        var victims = player.getNearbyEntities(5, 5, 5);
        victims.removeIf(victim -> victim.getUniqueId().equals(player.getUniqueId()));
        //TEST victims.removeIf(victim -> victim.getType() != EntityType.PLAYER);

        new Tornado(player, victims);
        COOLDOWN_TRACKER.scheduleCooldown(player, CooldownType.AIR_TORNADO, t10min);
    }

    private class Tornado {
        private final Player culprit;
        private final HashSet<LivingEntity> victims = new HashSet<>();
        private int timer = t5s;

        public Tornado(Player culprit, List<Entity> victims) {
            this.culprit = culprit;
            for (var victim : victims) if (victim instanceof LivingEntity entity) this.victims.add(entity);

            activeTornados.add(this);
        }

        public void tick() {
            timer--;
            if (timer == 0) activeTornados.remove(this);
            double timerScaled = ((timer - ((double) t5s/2))/20) * 2.5;

            Location target = culprit.getLocation().add(cos(timerScaled)*3, 5, sin(timerScaled)*3);

            for (var victim : victims) {
                Vector targetVector = target.toVector().subtract(victim.getLocation().toVector()).normalize();
                victim.setVelocity(targetVector);
                fallDamageExempt.add(victim);

                if (timer > t1s && timer % t1s == 0) victim.damage(1);
            }
        }
    }
}