package net.altarise.fk.game.listeners.entity;

import net.altarise.fk.FallenKingdom;
import net.altarise.fk.game.res.PlayerSkeleton;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityAggro implements Listener {

    @EventHandler
    public void onAggro(EntityTargetEvent event) {
        if (!(event.getEntity() instanceof Skeleton)) return;
        if (!(event.getTarget() instanceof Player)) return;

        Skeleton skeleton = (Skeleton) event.getEntity();

        if (!FallenKingdom.INSTANCE().getGameCoherence().playerSkeleton.containsKey(skeleton)) return;
        PlayerSkeleton playerSkeleton = FallenKingdom.INSTANCE().getGameCoherence().playerSkeleton.get(skeleton);
        if (playerSkeleton.isTeammate((Player) event.getTarget())) event.setCancelled(true);
    }
}
