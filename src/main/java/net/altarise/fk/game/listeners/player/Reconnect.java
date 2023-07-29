package net.altarise.fk.game.listeners.player;

import net.altarise.api.AltariseAPI;
import net.altarise.api.utils.title.ActionBar;
import net.altarise.fk.FallenKingdom;
import net.altarise.fk.game.res.PlayerSkeleton;
import net.altarise.fk.impl.TeamImpl;
import net.altarise.gameapi.event.player.GamePlayerReconnectEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Reconnect implements Listener {


    @EventHandler
    public void onReconnect(GamePlayerReconnectEvent event) {
        if (FallenKingdom.INSTANCE().getGameCoherence().getGameState().isState(4)) return;

        if (getPlayerSkeleton(event.getPlayer()) == null) {
            FallenKingdom.INSTANCE().getGameAPI().getSpectatorManager().addSpectator(event.getPlayer());
            new ActionBar("§cVous êtes maintenant spectateur").sendToPlayer(event.getPlayer());
            AltariseAPI.get().getMinecraftProvider().getScoreboardManager().setPlayerScoreboard(event.getPlayer(), FallenKingdom.INSTANCE().getGameCoherence().getSpectatorScoreboard(event.getPlayer()));
            return;
        }

        PlayerSkeleton skeleton = getPlayerSkeleton(event.getPlayer());
        TeamImpl team = skeleton.getTeamPlayer();


        if (team == null || team.getCrystal().isDead()) {
            FallenKingdom.INSTANCE().getGameAPI().getSpectatorManager().addSpectator(event.getPlayer());
            new ActionBar("§cVotre équipe a été éliminée").sendToPlayer(event.getPlayer());
            AltariseAPI.get().getMinecraftProvider().getScoreboardManager().setPlayerScoreboard(event.getPlayer(), FallenKingdom.INSTANCE().getGameCoherence().getSpectatorScoreboard(event.getPlayer()));
            skeleton.setDead();
            FallenKingdom.INSTANCE().getGameCoherence().playerSkeleton.remove(skeleton.getSkeleton());
            skeleton.getSkeleton().remove();
            return;
        }

        team.getTeam().addPlayer(event.getPlayer());
        team.getPlayers().add(event.getPlayer());
        if (!FallenKingdom.INSTANCE().getGamePlayers().contains(event.getPlayer()))
            FallenKingdom.INSTANCE().getGamePlayers().add(event.getPlayer());

        FallenKingdom.INSTANCE().getAltariseAPI().getMinecraftProvider().getScoreboardManager().setPlayerScoreboard(event.getPlayer(), FallenKingdom.INSTANCE().getGameCoherence().getPlayingScoreboard(event.getPlayer()));


        if (!skeleton.isDead()) {
            event.getPlayer().getInventory().setContents(skeleton.getPlayerInventory().getContents());
            event.getPlayer().teleport(skeleton.getSkeleton().getLocation());
            event.getPlayer().setFoodLevel(20);
        }

        if (skeleton.isDead()) event.getPlayer().teleport(team.getLocation());

        FallenKingdom.INSTANCE().getGameCoherence().playerSkeleton.remove(skeleton.getSkeleton());
        skeleton.getSkeleton().remove();
        Bukkit.broadcastMessage("§a● " + team.getChatColor() + event.getPlayer().getName() + " §7s'est reconnecté");
    }

    private PlayerSkeleton getPlayerSkeleton(Player player) {
        return FallenKingdom.INSTANCE().getGameCoherence().playerSkeleton.values().stream().filter(playerSkeleton -> playerSkeleton.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst().orElse(null);
    }

}
