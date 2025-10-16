package com.thorildsby.katslegendaryweapons;

import static com.thorildsby.katslegendaryweapons.Util.strForm;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import java.util.HashMap;

public class CooldownTracker {
    private final HashMap<Player, Cooldown> cooldowns = new HashMap<>();

    public CooldownTracker(KatsLegendaryWeapons plugin) {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this::manageCooldowns, 0L, 1L);
    }

    public boolean isCooldownActive(Player player, CooldownType type) {
        //There HAS to be a better way to do this
        if (cooldowns.containsKey(player)) return switch (type) {
            case TELEKINESIS_TELEPORT -> cooldowns.get(player).TELEKINESIS_TELEPORT > 0;
            case TELEKINESIS_LEVITATE -> cooldowns.get(player).TELEKINESIS_LEVITATE > 0;
        };
        return false;
    }

    public void scheduleCooldown(Player player, CooldownType type, int ticks) {
        if (!cooldowns.containsKey(player)) cooldowns.put(player, new Cooldown());

        Cooldown cooldown = cooldowns.get(player);
        switch (type) {
            case TELEKINESIS_TELEPORT -> cooldown.TELEKINESIS_TELEPORT = ticks;
            case TELEKINESIS_LEVITATE -> cooldown.TELEKINESIS_LEVITATE = ticks;
        }

        cooldowns.put(player, cooldown);
    }

    private void manageCooldowns() {
        for (var player : cooldowns.keySet()) cooldowns.get(player).negateAll(player);
    }

    private static class Cooldown {
        public int TELEKINESIS_TELEPORT = 0;
        public int TELEKINESIS_LEVITATE = 0;

        void negateAll(Player player) {
            boolean flag = false;
            if (TELEKINESIS_TELEPORT > 0) {
                TELEKINESIS_TELEPORT--;
                if (TELEKINESIS_TELEPORT > 0) return;
                if (player.isOnline()) player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    new TextComponent(strForm("*eYour Telekinesis Sword's teleport cooldown has expired!")));
                flag = true;
            }
            if (TELEKINESIS_LEVITATE > 0) {
                TELEKINESIS_LEVITATE--;
                if (TELEKINESIS_LEVITATE > 0) return;
                if (player.isOnline()) player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    new TextComponent(strForm("*eYour Telekinesis Sword's levitation cooldown has expired!")));
                flag = true;
            }

            if (flag && player.isOnline()) player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        }
    }

    public enum CooldownType {
        TELEKINESIS_TELEPORT,
        TELEKINESIS_LEVITATE
    }
}