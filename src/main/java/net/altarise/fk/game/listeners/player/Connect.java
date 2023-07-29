package net.altarise.fk.game.listeners.player;

import net.altarise.api.AltariseAPI;
import net.altarise.api.utils.title.ActionBar;
import net.altarise.fk.FallenKingdom;
import net.altarise.fk.impl.GamePlayerImpl;
import net.altarise.gameapi.event.player.GamePlayerConnectEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Connect implements Listener {

    @EventHandler
    public void onConnect(GamePlayerConnectEvent event) {
        if (!FallenKingdom.INSTANCE().getGameCoherence().getGameState().isState(0)) {
            if (!FallenKingdom.INSTANCE().getGameCoherence().players.contains(event.getPlayer())) {
                FallenKingdom.INSTANCE().getGameAPI().getSpectatorManager().addSpectator(event.getPlayer());
                new ActionBar("§cLa partie a déjà commencé").sendToPlayer(event.getPlayer());
                AltariseAPI.get().getMinecraftProvider().getScoreboardManager().setPlayerScoreboard(event.getPlayer(), FallenKingdom.INSTANCE().getGameCoherence().getSpectatorScoreboard(event.getPlayer()));
                return;
            }
        }

        addHealthBar(event.getPlayer());

        FallenKingdom.INSTANCE().getGameStatistics().put(event.getPlayer().getUniqueId(), new GamePlayerImpl(event.getPlayer()));
        FallenKingdom.INSTANCE().getGameCoherence().players.add(event.getPlayer());
    }

    private void addHealthBar(Player player) {
        final ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard scoreboard = manager.getNewScoreboard();
        final Objective objective = scoreboard.registerNewObjective("health", "health");
        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        objective.setDisplayName("§c❤");
        player.setScoreboard(scoreboard);
    }

}
