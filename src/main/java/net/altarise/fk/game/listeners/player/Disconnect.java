package net.altarise.fk.game.listeners.player;

import net.altarise.fk.FallenKingdom;
import net.altarise.fk.game.res.PlayerSkeleton;
import net.altarise.fk.impl.TeamImpl;
import net.altarise.gameapi.event.player.GamePlayerDisconnectEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class Disconnect implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(GamePlayerDisconnectEvent event) {
        if (FallenKingdom.INSTANCE() == null || FallenKingdom.INSTANCE().getGameCoherence().getGameState().isState(0))
            return;
        final TeamImpl team = FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(event.getPlayer());

        int playersOnline = 0;
        List<Player> players = new ArrayList<>(team.getPlayers());
        for (Player player : players) {
            if (player.isOnline()) {
                playersOnline++;
            }
        }

        if (playersOnline <= 1) {
            team.getCrystal().removeLife(null, 1000);
        }

        PlayerSkeleton playerSkeleton = new PlayerSkeleton(event.getPlayer(), team);
        playerSkeleton.spawn();

        FallenKingdom fk = FallenKingdom.INSTANCE();
        if (fk != null) {
            fk.getGameCoherence().playerSkeleton.put(playerSkeleton.getSkeleton(), playerSkeleton);
        }
        Bukkit.broadcastMessage("§c● " + team.getChatColor() + event.getPlayer().getName() + " §7a quitté la partie");
    }

}
