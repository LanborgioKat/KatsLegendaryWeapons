package com.thorildsby.katslegendaryweapons.item;

import com.thorildsby.katslegendaryweapons.CooldownTracker.CooldownType;
import static com.thorildsby.katslegendaryweapons.KatsLegendaryWeapons.COOLDOWN_TRACKER;
import static com.thorildsby.katslegendaryweapons.Util.strForm;
import static com.thorildsby.katslegendaryweapons.Util.t30s;
import com.thorildsby.katslegendaryweapons.event.JumpEvent;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.HashSet;

public class AirSword extends Item {
    private final HashSet<Player> fallDamageExempt = new HashSet<>();

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

    @EventHandler
    public void onPlayerJump(JumpEvent event) {
        if (!isApplicable(event)) return;

        Player player = event.getPlayer();
        //noinspection deprecation
        if (player.isOnGround()) return; //How the fuck is this even deprecated?
        if (player.isInWater()) return;
        if (COOLDOWN_TRACKER.isCooldownActive(player, CooldownType.AIR_DOUBLE_JUMP)) {
            noAbilityMessage(player, CooldownType.AIR_DOUBLE_JUMP);
            return;
        }

        player.setVelocity(player.getLocation().getDirection().setY(1));
        player.spawnParticle(Particle.EXPLOSION, player.getLocation(), 5);
        player.playSound(player.getLocation(), Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1f, 1f);

        COOLDOWN_TRACKER.scheduleCooldown(player, CooldownType.AIR_DOUBLE_JUMP, t30s);
        fallDamageExempt.add(player);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        boolean causeCheck = event.getCause() == EntityDamageEvent.DamageCause.FALL;

        if (event.getEntity() instanceof Player player && fallDamageExempt.contains(player) && causeCheck) {
            fallDamageExempt.remove(player);
            event.setCancelled(true);
            return;
        }

        if (!isApplicable(event)) return;
        if (causeCheck) event.setCancelled(true);
    }
}