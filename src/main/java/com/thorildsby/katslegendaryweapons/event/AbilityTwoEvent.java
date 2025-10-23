package com.thorildsby.katslegendaryweapons.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class AbilityTwoEvent extends PlayerEvent {
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public AbilityTwoEvent(@NotNull Player who) {
        super(who);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}