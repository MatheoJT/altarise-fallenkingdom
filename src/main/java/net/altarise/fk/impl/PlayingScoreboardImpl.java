package net.altarise.fk.impl;

import net.altarise.api.utils.LocationUtils;
import net.altarise.fk.FallenKingdom;
import net.altarise.gameapi.scoreboard.GameScoreboard;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayingScoreboardImpl extends GameScoreboard {

    private final Player player;


    public PlayingScoreboardImpl(Player player) {
        this.player = player;
    }

    @Override
    public Collection<String> getLines() {
        Collection<String> beforeTeams = Arrays.asList(" §fJour " + FallenKingdom.INSTANCE().getGameCoherence().day + " §3" + FallenKingdom.INSTANCE().getGameCoherence().getGameState().getDayNightEmote() + " §f" + FallenKingdom.INSTANCE().getScheduler().getStrTimer(),
                " §fPhase §3" + FallenKingdom.INSTANCE().getGameCoherence().getGameState().getEmote() + " §f" + FallenKingdom.INSTANCE().getGameCoherence().getGameState().getStrState(),
                "",
                "§3❯§f❯ §3Équipes");
        Collection<String> teams = new ArrayList<>();
        FallenKingdom.INSTANCE().getTeamProperties().getTeams().forEach(fkTeam -> teams.add(" §f" + fkTeam.getPlayers().size() + " " + fkTeam.getChatColor() + fkTeam.getName() + " " + fkTeam.getCrystal().getStrLife() + (fkTeam.getCrystal().isDead() ? "" : " §f" + LocationUtils.getDirection(player, fkTeam.getCrystal().getLocation()))));
        Collection<String> afterTeams = new ArrayList<>();
        afterTeams.add("");
        afterTeams.add("§3❯§f❯ §3Événements");

        if (!FallenKingdom.INSTANCE().getGameCoherence().getRandomDrop().isOpened()) {
            afterTeams.add(" §b▪ §fLargage: " +
                    FallenKingdom.INSTANCE().getGameCoherence().getRandomDrop().getStrTime(player));
        }

        afterTeams.add(FallenKingdom.INSTANCE().getGameCoherence().getGameState().isState(4) ?
                " §b▪ §fDeathMatch" :
                FallenKingdom.INSTANCE().getScheduler().getStrCooldown());
        afterTeams.add(" §b▪ §fStatistiques: §a" +
                FallenKingdom.INSTANCE().getGamePlayer(player.getUniqueId()).getKills() +
                "§8/§c" +
                FallenKingdom.INSTANCE().getGamePlayer(player.getUniqueId()).getDeaths());


        return Stream.of(beforeTeams, teams, afterTeams).flatMap(Collection::stream).collect(Collectors.toList());
    }


    @Override
    public void onUpdate() {

    }
}
