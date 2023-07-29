package net.altarise.fk.game.listeners.player;

import net.altarise.api.utils.title.ActionBar;
import net.altarise.fk.FallenKingdom;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Move implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (event.getTo().getBlockX() == event.getFrom().getBlockX() && event.getTo().getBlockY() == event.getFrom().getBlockY() && event.getTo().getBlockZ() == event.getFrom().getBlockZ())
            return;
        if (FallenKingdom.INSTANCE().getGameCoherence().getGameState().isState(0)) return;

        final boolean isInBase = FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(player).getRegion().isInArea(player.getLocation().getBlock().getLocation());
        final boolean wasInBase = FallenKingdom.INSTANCE().getGamePlayer(player.getUniqueId()).isInBase();

        if (isInBase && !wasInBase) {
            new ActionBar("§a❖ Vous entrez dans votre base").sendToPlayer(player);
        } else if (!isInBase && wasInBase) {
            new ActionBar("§c❖ Vous sortez de votre base").sendToPlayer(player);
        }

        FallenKingdom.INSTANCE().getGamePlayer(player.getUniqueId()).setBase(isInBase);
    }


}
