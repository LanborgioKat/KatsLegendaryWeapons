package com.thorildsby.katslegendaryweapons;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Player;

public class Util {
    //Seconds
    public static final int t1s = tSeconds(1);
    public static final int t5s = tSeconds(5);
    public static final int t10s = tSeconds(10);
    public static final int t20s = tSeconds(20);
    public static final int t30s = tSeconds(30);
    public static final int t45s = tSeconds(45);

    //Minutes
    public static final int t1min = tMinutes(1);
    public static final int t3min = tMinutes(3);
    public static final int t5min = tMinutes(5);
    public static final int t8min = tMinutes(8);
    public static final int t10min = tMinutes(10);
    public static final int t15min = tMinutes(15);

    public static int tSeconds(int n) {
        return n*20;
    }

    public static int tMinutes(int n) {
        return tSeconds(n*60);
    }

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