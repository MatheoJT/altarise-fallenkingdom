package net.altarise.fk.game.listeners.block;

import net.altarise.fk.FallenKingdom;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class BlockChange implements Listener {

    @EventHandler
    public void onDropHitGround(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof FallingBlock) {
            if (event.getEntity().equals(FallenKingdom.INSTANCE().getGameCoherence().getRandomDrop().bedrock)) {

                event.setCancelled(true);
                event.getEntity().remove();
                FallenKingdom.INSTANCE().getGameCoherence().getRandomDrop().fallingBlocks.forEach(Entity::remove);
                Bukkit.getWorld("world").createExplosion(FallenKingdom.INSTANCE().getGameCoherence().getRandomDrop().getRandomLocation(), 0, false);
                FallenKingdom.INSTANCE().getGameCoherence().getRandomDrop().spawn();

            }
        }
    }

}
