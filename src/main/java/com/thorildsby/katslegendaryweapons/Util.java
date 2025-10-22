package com.thorildsby.katslegendaryweapons;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Player;

public class Util {
    //Seconds
    public static final int t10s = 10*20;
    public static final int t20s = 20*20;
    public static final int t30s = 30*20;
    public static final int t45s = 45*20;

    //Minutes
    public static final int t1min = 60*20;
    public static final int t5min = 5*60*20;
    public static final int t8min = 8*60*20;
    public static final int t15min = 15*60*20;

    public static String strForm(String s) {
        return ChatColor.translateAlternateColorCodes('*', s);
    }

    @Deprecated
    public static Husk getClosestHusk(Player origin, double maxDistance) {
        var candidates = origin.getNearbyEntities(maxDistance, maxDistance, maxDistance);

        Husk closest = null;
        double closestDistance = 100d;
        for (var candidate : candidates) {
            if (candidate.getType() != EntityType.HUSK) continue;
            if (candidate.getUniqueId().equals(origin.getUniqueId())) continue;

            double distance = origin.getLocation().distance(candidate.getLocation());
            if (distance < closestDistance) {
                closest = (Husk) candidate;
                closestDistance = distance;
            }
        }

        return closest;
    }

    public static Player getClosestPlayer(Player origin, double maxDistance) {
        var candidates = origin.getNearbyEntities(maxDistance, maxDistance, maxDistance);

        Player closest = null;
        double closestDistance = 100d;
        for (var candidate : candidates) {
            if (candidate.getType() != EntityType.PLAYER) continue;
            if (candidate.getUniqueId().equals(origin.getUniqueId())) continue;

            double distance = origin.getLocation().distance(candidate.getLocation());
            if (distance < closestDistance) {
                closest = (Player) candidate;
                closestDistance = distance;
            }
        }

        return closest;
    }
}