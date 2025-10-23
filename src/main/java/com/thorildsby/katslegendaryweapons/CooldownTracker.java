package com.thorildsby.katslegendaryweapons;

import static com.thorildsby.katslegendaryweapons.Util.strForm;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import java.util.HashMap;

//Ik this class is shit, but it works, so I won't change it
//NOTE: one possible way to fix this class would be to have the cooldowns be an array, and the enum index the array
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
            case FLOW_FLOW -> cooldowns.get(player).FLOW_FLOW > 0;
            case FLOW_SHAMBLES -> cooldowns.get(player).FLOW_SHAMBLES > 0;
            case SHIELD_BREAKER_HALF_DAMAGE -> cooldowns.get(player).SHIELD_BREAKER_HALF_DAMAGE >0;
            case AIR_DOUBLE_JUMP -> cooldowns.get(player).AIR_DOUBLE_JUMP > 0;
            case AIR_WIND_CANON -> cooldowns.get(player).AIR_WIND_CANON > 0;
            case AIR_TORNADO -> cooldowns.get(player).AIR_TORNADO > 0;
        };
        return false;
    }

    public int getCooldown(Player player, CooldownType type) {
        if (cooldowns.containsKey(player)) return switch (type) {
            case TELEKINESIS_TELEPORT -> cooldowns.get(player).TELEKINESIS_TELEPORT;
            case TELEKINESIS_LEVITATE -> cooldowns.get(player).TELEKINESIS_LEVITATE;
            case FLOW_FLOW -> cooldowns.get(player).FLOW_FLOW;
            case FLOW_SHAMBLES -> cooldowns.get(player).FLOW_SHAMBLES;
            case SHIELD_BREAKER_HALF_DAMAGE -> cooldowns.get(player).SHIELD_BREAKER_HALF_DAMAGE;
            case AIR_DOUBLE_JUMP -> cooldowns.get(player).AIR_DOUBLE_JUMP;
            case AIR_WIND_CANON -> cooldowns.get(player).AIR_WIND_CANON;
            case AIR_TORNADO -> cooldowns.get(player).AIR_TORNADO;
        };
        return -1;
    }

    public void scheduleCooldown(Player player, CooldownType type, int ticks) {
        if (!cooldowns.containsKey(player)) cooldowns.put(player, new Cooldown());

        Cooldown cooldown = cooldowns.get(player);
        switch (type) {
            case TELEKINESIS_TELEPORT -> cooldown.TELEKINESIS_TELEPORT = ticks;
            case TELEKINESIS_LEVITATE -> cooldown.TELEKINESIS_LEVITATE = ticks;
            case FLOW_FLOW -> cooldown.FLOW_FLOW = ticks;
            case FLOW_SHAMBLES -> cooldown.FLOW_SHAMBLES = ticks;
            case SHIELD_BREAKER_HALF_DAMAGE -> cooldown.SHIELD_BREAKER_HALF_DAMAGE = ticks;
            case AIR_DOUBLE_JUMP -> cooldown.AIR_DOUBLE_JUMP = ticks;
            case AIR_WIND_CANON -> cooldown.AIR_WIND_CANON = ticks;
            case AIR_TORNADO -> cooldown.AIR_TORNADO = ticks;
        }

        cooldowns.put(player, cooldown);
    }

    public void clearAll(Player player) {
        if (cooldowns.containsKey(player)) {
            Cooldown cooldown = cooldowns.get(player);

            cooldown.TELEKINESIS_TELEPORT = 0;
            cooldown.TELEKINESIS_LEVITATE = 0;
            cooldown.FLOW_FLOW = 0;
            cooldown.FLOW_SHAMBLES = 0;
            cooldown.SHIELD_BREAKER_HALF_DAMAGE = 0;
            cooldown.AIR_DOUBLE_JUMP = 0;
            cooldown.AIR_WIND_CANON = 0;
            cooldown.AIR_TORNADO = 0;

            cooldowns.put(player, cooldown);
        }
    }

    private void manageCooldowns() {
        for (var player : cooldowns.keySet()) cooldowns.get(player).negateAll(player);
    }

    private static class Cooldown {
        public int TELEKINESIS_TELEPORT = 0;
        public int TELEKINESIS_LEVITATE = 0;
        public int FLOW_FLOW = 0;
        public int FLOW_SHAMBLES = 0;
        public int SHIELD_BREAKER_HALF_DAMAGE = 0;
        public int AIR_DOUBLE_JUMP = 0;
        public int AIR_WIND_CANON = 0;
        public int AIR_TORNADO = 0;

        void negateAll(Player player) {
            boolean flag = false;
            //TODO: refactor
            if (TELEKINESIS_TELEPORT > 0) {
                TELEKINESIS_TELEPORT--;
                if (player.isOnline() && TELEKINESIS_TELEPORT <= 0) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new TextComponent(strForm("*eYour Telekinesis Sword's teleport cooldown has expired!")));
                    flag = true;
                }
            }
            if (TELEKINESIS_LEVITATE > 0) {
                TELEKINESIS_LEVITATE--;
                if (player.isOnline() && TELEKINESIS_LEVITATE <= 0) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new TextComponent(strForm("*eYour Telekinesis Sword's levitation cooldown has expired!")));
                    flag = true;
                }
            }
            if (FLOW_FLOW > 0) {
                FLOW_FLOW--;
                if (player.isOnline() && FLOW_FLOW <= 0) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new TextComponent(strForm("*eYour Flow Sword's flow cooldown has expired!")));
                    flag = true;
                }
            }
            if (FLOW_SHAMBLES > 0) {
                FLOW_SHAMBLES--;
                if (player.isOnline() && FLOW_SHAMBLES <= 0) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new TextComponent(strForm("*eYour Flow Sword's shambles cooldown has expired!")));
                    flag = true;
                }
            }
            if (SHIELD_BREAKER_HALF_DAMAGE > 0) {
                SHIELD_BREAKER_HALF_DAMAGE--;
                if (player.isOnline() && SHIELD_BREAKER_HALF_DAMAGE <= 0) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new TextComponent(strForm("*eYour Shield Breaker Axe's cooldown has expired!")));
                    flag = true;
                }
            }
            if (AIR_DOUBLE_JUMP > 0) {
                AIR_DOUBLE_JUMP--;
                if (player.isOnline() && AIR_DOUBLE_JUMP <= 0) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new TextComponent(strForm("*eYour Air Sword's double jump cooldown has expired!")));
                    flag = true;
                }
            }
            if (AIR_WIND_CANON > 0) {
                AIR_WIND_CANON--;
                if (player.isOnline() && AIR_WIND_CANON <= 0) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new TextComponent(strForm("*eYour Air Sword's wind canon cooldown has expired!")));
                    flag = true;
                }
            }
            if (AIR_TORNADO > 0) {
                AIR_TORNADO--;
                if (player.isOnline() && AIR_TORNADO <= 0) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new TextComponent(strForm("*eYour Air Sword's tornado cooldown has expired!")));
                    flag = true;
                }
            }

            if (flag && player.isOnline()) player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        }
    }

    public enum CooldownType {
        TELEKINESIS_TELEPORT,
        TELEKINESIS_LEVITATE,
        FLOW_FLOW,
        FLOW_SHAMBLES,
        SHIELD_BREAKER_HALF_DAMAGE,
        AIR_DOUBLE_JUMP,
        AIR_WIND_CANON,
        AIR_TORNADO
    }
}