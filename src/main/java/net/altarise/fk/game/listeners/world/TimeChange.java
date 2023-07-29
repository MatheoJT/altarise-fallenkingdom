package net.altarise.fk.game.listeners.world;

import net.altarise.fk.FallenKingdom;
import net.altarise.fk.game.events.GameTimeChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TimeChange implements Listener {

    @EventHandler
    public void onGameTimeChange(GameTimeChangeEvent event) {
        if (event.isDay()) {
            Bukkit.getWorld("world").setTime(6000);
            FallenKingdom.INSTANCE().getGameCoherence().getGameState().setDay(true);
        } else {
            Bukkit.getWorld("world").setTime(18000);
            FallenKingdom.INSTANCE().getGameCoherence().getGameState().setDay(false);
        }
    }
}
