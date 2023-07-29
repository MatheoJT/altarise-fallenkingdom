package net.altarise.fk.impl;

import net.altarise.fk.FallenKingdom;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TeamPropertiesImpl extends net.altarise.gameapi.engine.properties.TeamProperties<TeamImpl> {

    private static final Map<String, TeamImpl> teamsByName = new HashMap<>();

    public TeamPropertiesImpl() {
        for (TeamImpl team : FallenKingdom.INSTANCE().getGameProperties().getTeams()) {
            teamsByName.put(team.getId(), team);
        }
    }


    public static TeamImpl getTeamByName(String name) {
        return teamsByName.get(name);
    }


    @Override
    public boolean useTeams() {
        return true;
    }

    @Override
    public int getMaxPlayerPerTeam() {
        return FallenKingdom.INSTANCE().getGameProperties().getMaxPlayersPerTeam();
    }

    @Override
    public TeamImpl getTeamPlayer(Player player) {
        for (TeamImpl team : FallenKingdom.INSTANCE().getGameProperties().getTeams()) {
            if (team.getPlayers().contains(player)) return team;
        }

        return super.getTeamPlayer(player);
    }

    public static TeamImpl getTeamByID(String ID) {
        for (TeamImpl team : FallenKingdom.INSTANCE().getGameProperties().getTeams()) {
            if (team.getId().equals(ID)) return team;
        }

        return null;
    }

    @Override
    public Collection<TeamImpl> getTeams() {
        return FallenKingdom.INSTANCE().getGameProperties().getTeams();
    }
}
