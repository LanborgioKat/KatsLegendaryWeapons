package com.thorildsby.katslegendaryweapons;

import static com.thorildsby.katslegendaryweapons.Util.t15min;
import com.thorildsby.katslegendaryweapons.command.AbilityCommand;
import com.thorildsby.katslegendaryweapons.command.CooldownRemoveCommand;
import com.thorildsby.katslegendaryweapons.command.LegendariesCommand;
import com.thorildsby.katslegendaryweapons.item.CustomItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public final class KatsLegendaryWeapons extends JavaPlugin {
    public static Logger LOGGER;
    public static CustomItemManager ITEM_MANAGER;
    public static CooldownTracker COOLDOWN_TRACKER;

    @Override
    public void onEnable() {
        LOGGER = getLogger();
        ITEM_MANAGER = new CustomItemManager(this);
        COOLDOWN_TRACKER = new CooldownTracker(this);

        Objects.requireNonNull(getCommand("legendaries")).setExecutor(new LegendariesCommand());
        Objects.requireNonNull(getCommand("legendaries")).setTabCompleter(new LegendariesCommand.TabCompleter());

        Objects.requireNonNull(getCommand("ability")).setExecutor(new AbilityCommand());
        Objects.requireNonNull(getCommand("ability")).setTabCompleter(new AbilityCommand.TabCompleter());

        Objects.requireNonNull(getCommand("clearCooldown")).setExecutor(new CooldownRemoveCommand());

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
                    Bukkit.broadcastMessage(player.getDisplayName() + " is located at " + x + " " + y + " " + z);
                }
            }
        }, 0L, t15min);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerEvents(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }
}