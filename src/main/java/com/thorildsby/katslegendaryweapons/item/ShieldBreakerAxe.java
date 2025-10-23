package com.thorildsby.katslegendaryweapons.item;

import com.thorildsby.katslegendaryweapons.CooldownTracker.CooldownType;
import static com.thorildsby.katslegendaryweapons.KatsLegendaryWeapons.COOLDOWN_TRACKER;
import static com.thorildsby.katslegendaryweapons.KatsLegendaryWeapons.shieldSchedule;
import static com.thorildsby.katslegendaryweapons.Util.strForm;
import static com.thorildsby.katslegendaryweapons.Util.t20s;
import static java.lang.Math.max;
import static java.lang.Math.min;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import java.util.ArrayList;

public class ShieldBreakerAxe extends Item {
    public ShieldBreakerAxe(JavaPlugin plugin) {
        super(plugin, "SHIELD_BREAKER_AXE", Material.NETHERITE_AXE);
    }

    @Override
    protected ItemStack generateItem(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(strForm("*dShield Breaker Axe"));

        ArrayList<String> description = new ArrayList<>();
        description.add(strForm("*c*l*oShatters shields and bypasses all blocking"));
        description.add(strForm("*4*oDeals full damage through shields"));
        description.add(strForm("*f*oDisables shields for 20 seconds on hit (20 CD)"));
        description.add(strForm("*f*oAfter shield disable: 20 seconds half damage"));
        description.add(strForm("*7*oID: SHIELD_BREAKER_AXE"));
        meta.setLore(description);

        meta.setEnchantmentGlintOverride(true);

        @SuppressWarnings("deprecation")
        AttributeModifier attackDamage =
            new AttributeModifier("attack_damage", 12, AttributeModifier.Operation.ADD_NUMBER);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, attackDamage);

        @SuppressWarnings("deprecation")
        AttributeModifier attackSpeed =
            new AttributeModifier("attack_speed", -3, AttributeModifier.Operation.ADD_NUMBER);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeed);

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        if (!isApplicable(event)) return;
        if (!(event.getDamager() instanceof Player damager)) return;
        if (!(event.getEntity() instanceof Player attacked)) return;

        //Holy shit I didn't know pattern variables were allowed like that
        if (COOLDOWN_TRACKER.isCooldownActive(damager, CooldownType.SHIELD_BREAKER_HALF_DAMAGE)) {
            event.setDamage(event.getDamage()/2);
            return;
        }

        if (!attacked.isBlocking()) return;

        //Ahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh
        AttributeInstance armourAttribute = attacked.getAttribute(Attribute.GENERIC_ARMOR);
        double armour = armourAttribute != null? armourAttribute.getValue() : 0;

        AttributeInstance toughnessAttribute = attacked.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);
        double toughness = toughnessAttribute != null? toughnessAttribute.getValue() : 0;

        var resistanceEffect = attacked.getPotionEffect(PotionEffectType.RESISTANCE);
        var strengthEffect = damager.getPotionEffect(PotionEffectType.STRENGTH);
        var weaknessEffect = damager.getPotionEffect(PotionEffectType.WEAKNESS);

        int axeBaseDamage = 12;

        double sharpnessModifier = 0.5*damager.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.SHARPNESS) + 0.5;
        double armourModifier = min(80, max(0.8*armour, 4*armour - (16*(axeBaseDamage+sharpnessModifier))/(toughness+8)))/100;
        double resistanceModifier = resistanceEffect != null? 1 - 0.2*resistanceEffect.getAmplifier() : 1;
        double critModifier = attacked.getVelocity().getY() < 0? 1.5 : 1;
        double strengthModifier = strengthEffect != null? 3*strengthEffect.getAmplifier() : 0;
        double weaknessModifier = weaknessEffect != null? -4*weaknessEffect.getAmplifier() : 0;

        double damage = (axeBaseDamage + sharpnessModifier + strengthModifier + weaknessModifier) *
            (1-armourModifier) * resistanceModifier * critModifier;
        event.setDamage(damage);

        shieldSchedule.put(attacked, damage);
        COOLDOWN_TRACKER.scheduleCooldown(damager, CooldownType.SHIELD_BREAKER_HALF_DAMAGE, t20s);
    }
}