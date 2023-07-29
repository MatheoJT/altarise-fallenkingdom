package net.altarise.fk.game.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameDayChangeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final int day;

    public GameDayChangeEvent(int day) {
        this.day = day;
    }


    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


    public int getDay() {
        return day;
    }

}
