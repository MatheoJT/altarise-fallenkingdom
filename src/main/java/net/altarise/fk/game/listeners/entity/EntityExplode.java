package net.altarise.fk.game.listeners.entity;

import net.altarise.fk.FallenKingdom;
import net.altarise.fk.game.res.Crystal;
import net.altarise.fk.impl.TeamPropertiesImpl;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class EntityExplode implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        if (event.getEntity() == null) return;
        if (event.getEntity().getType() == EntityType.ENDER_CRYSTAL) {
            event.setCancelled(true);
        }
        if (FallenKingdom.INSTANCE().getGameCoherence().getGameState().getState() <= 2) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void enderCrystalExplode(ExplosionPrimeEvent event) {
        if (event.getEntity() == null) return;
        if (event.getEntity().getType() == EntityType.ENDER_CRYSTAL) {
            Crystal crystal = TeamPropertiesImpl.getTeamByName(event.getEntity().getCustomName()).getCrystal();
            crystal.removeLife(null, 5);
            event.setCancelled(true);
        }
    }
}
