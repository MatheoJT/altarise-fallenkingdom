package net.altarise.fk.game.listeners.entity;

import net.altarise.fk.FallenKingdom;
import net.altarise.fk.impl.TeamImpl;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawn implements Listener {


    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) event.getEntity();
            if (skeleton.getSkeletonType() == Skeleton.SkeletonType.WITHER) {
                return;
            }
        }

        if ((Math.random() * 100) <= 50) {
            event.setCancelled(true);
        }

        for (TeamImpl team : FallenKingdom.INSTANCE().getGameProperties().getTeams()) {
            if (team.getRegion().isInArea(event.getLocation())) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getEntityType() == EntityType.CREEPER) {
            if ((Math.random() * 100) <= 25) {
                Creeper creeper = (Creeper) Bukkit.getWorld("world").spawnEntity(event.getEntity().getLocation(), EntityType.CREEPER);
                creeper.setPowered(true);
                event.getEntity().remove();
                Bukkit.getWorld("world").playSound(creeper.getLocation(), Sound.AMBIENCE_THUNDER, 1F, 1F);
            }
        }

    }


}
