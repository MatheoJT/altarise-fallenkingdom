package net.altarise.fk.game.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameTimeChangeEvent extends Event {

    public static final HandlerList HANDLERS = new HandlerList();

    private final boolean isDay;

    public GameTimeChangeEvent(long minutes, long minutesPerDay) {
        double timeRatio = (double) minutes / minutesPerDay;
        int hours = (int) (timeRatio * 24);
        this.isDay = (hours >= 6 && hours < 18);
    }


    public boolean isDay() {
        return isDay;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
