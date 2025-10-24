package com.thorildsby.katslegendaryweapons.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class DoubleJumpEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    protected Player player;

    public DoubleJumpEvent(@NotNull Player who) {
        super(true);
        this.player = who;
    }

    public final Player getPlayer() {
        return this.player;
    }

    @NotNull @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}