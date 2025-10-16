package com.thorildsby.katslegendaryweapons;

import org.bukkit.ChatColor;

public class Util {
    public static final int t10s = 10*20;

    public static final int t3min = 3*60*20;
    public static final int t10min = 10*60*20;

    public static String strForm(String s) {
        return ChatColor.translateAlternateColorCodes('*', s);
    }
}