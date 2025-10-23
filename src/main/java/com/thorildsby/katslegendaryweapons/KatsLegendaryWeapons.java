package com.thorildsby.katslegendaryweapons;

import static com.thorildsby.katslegendaryweapons.Util.*;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.thorildsby.katslegendaryweapons.command.AbilityCommand;
import com.thorildsby.katslegendaryweapons.command.CooldownRemoveCommand;
import com.thorildsby.katslegendaryweapons.command.LegendariesCommand;
import com.thorildsby.katslegendaryweapons.event.JumpEvent;
import com.thorildsby.katslegendaryweapons.item.CustomItemManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public final class KatsLegendaryWeapons extends JavaPlugin {
    public static Logger LOGGER;
    public static CustomItemManager ITEM_MANAGER;
    public static CooldownTracker COOLDOWN_TRACKER;
    public static HashMap<Player, Double> shieldSchedule = new HashMap<>();
    public static ProtocolManager PROTOCOL_MANAGER;

    @Override
    public void onEnable() {
        LOGGER = getLogger();
        ITEM_MANAGER = new CustomItemManager(this);
        COOLDOWN_TRACKER = new CooldownTracker(this);
        PROTOCOL_MANAGER = ProtocolLibrary.getProtocolManager();

        PROTOCOL_MANAGER.addPacketListener(new PacketAdapter(
            this, ListenerPriority.NORMAL, PacketType.Play.Client.STEER_VEHICLE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                //Actually goofed up code :sob:, but I won't elaborate
                String s = event.getPacket().getHandle().toString();
                //TODO: after releasing space, you have one second to hit space again
                if (s.contains("jump=true") && !s.contains("left=true") && !s.contains("right=true"))
                    Bukkit.getPluginManager().callEvent(new JumpEvent(event.getPlayer()));
            }
        });

        Objects.requireNonNull(getCommand("legendaries")).setExecutor(new LegendariesCommand());
        Objects.requireNonNull(getCommand("legendaries")).setTabCompleter(new LegendariesCommand.TabCompleter());

        Objects.requireNonNull(getCommand("ability")).setExecutor(new AbilityCommand());
        Objects.requireNonNull(getCommand("ability")).setTabCompleter(new AbilityCommand.TabCompleter());

        Objects.requireNonNull(getCommand("clearCooldown")).setExecutor(new CooldownRemoveCommand());

        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (var player : shieldSchedule.keySet()) {
                player.setCooldown(Material.SHIELD, t20s);
                player.damage(shieldSchedule.get(player));
                shieldSchedule.remove(player);
            }
        }, 0L, 1L);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (var player : getServer().getOnlinePlayers()) for (var item : player.getInventory()) {
                if (item == null) continue;
                if (!item.hasItemMeta()) continue;

                ItemMeta meta = item.getItemMeta();
                assert meta != null;

                if (meta.getPersistentDataContainer().has(new NamespacedKey(this, "ITEM_ID"))) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
                    player.spigot().sendMessage(ChatMessageType.CHAT,
                        new TextComponent(strForm("*cYour location will be revealed in 1 minute!")));
                }
            }
        }, 0L, t15min);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (var player : getServer().getOnlinePlayers()) for (var item : player.getInventory()) {
                if (item == null) continue;
                if (!item.hasItemMeta()) continue;

                ItemMeta meta = item.getItemMeta();
                assert meta != null;

                if (meta.getPersistentDataContainer().has(new NamespacedKey(this, "ITEM_ID"))) {
                    Location location = player.getLocation();
                    int x = (int) location.getX();
                    int y = (int) location.getY();
                    int z = (int) location.getZ();

                    //TODO: dimension
                    Bukkit.broadcastMessage(player.getDisplayName() + " is located at " + x + " " + y + " " + z +
                        " in " + player.getWorld().getName());
                }
            }
        }, t1min, t15min);
    }

    @Override
    public void onDisable() {}

    public void registerEvents(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }
}