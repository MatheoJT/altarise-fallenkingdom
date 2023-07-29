package net.altarise.fk;


import net.altarise.fk.game.GameManager;
import net.altarise.fk.game.PingCommand;
import net.altarise.fk.game.Scheduler;
import net.altarise.fk.game.listeners.ListenerManager;
import net.altarise.fk.impl.GamePlayerImpl;
import net.altarise.fk.impl.GamePropertiesImpl;
import net.altarise.fk.impl.TeamPropertiesImpl;
import net.altarise.gameapi.engine.Game;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;

import java.util.UUID;


public class FallenKingdom extends Game<GamePlayerImpl> {


    private static FallenKingdom fallenKingdom;
    private GamePropertiesImpl gameProperties;
    private GameManager gameCoherence;
    private TeamPropertiesImpl teamProperties;
    private Scheduler scheduler;

    @Override
    public void onEnable() {
        super.onEnable();

        saveDefaultConfig();

        fallenKingdom = this;
        gameProperties = new GamePropertiesImpl(this);
        gameCoherence = new GameManager();
        teamProperties = new TeamPropertiesImpl();
        scheduler = new Scheduler();

        new ListenerManager(this);

        getCommand("ping").setExecutor(new PingCommand());

        Bukkit.getWorld("world").setTime(18000L);
        Bukkit.getWorld("world").setGameRuleValue("doMobSpawning", String.valueOf(true));
        Bukkit.getWorld("world").setGameRuleValue("doDaylightCycle", String.valueOf(false));
        Bukkit.getWorld("world").setGameRuleValue("doMobLoot", String.valueOf(true));
        Bukkit.getWorld("world").setGameRuleValue("doEntityDrops", String.valueOf(true));
        Bukkit.getWorld("world").setGameRuleValue("doFireTick", String.valueOf(false));
        Bukkit.getWorld("world").setGameRuleValue("mobGriefing", String.valueOf(true));
        Bukkit.getWorld("world").setGameRuleValue("tntExplodes", String.valueOf(true));
        Bukkit.getWorld("world").setPVP(false);
        Bukkit.getWorld("world").setDifficulty(Difficulty.PEACEFUL);


        this.getGameAPI().registerNewGame(this);

    }

    @Override
    public GamePropertiesImpl getGameProperties() {
        return gameProperties;
    }

    public TeamPropertiesImpl getTeamProperties() {
        return teamProperties;
    }

    @Override
    public GameManager getGameCoherence() {
        return gameCoherence;
    }
    public static FallenKingdom INSTANCE() {
        return fallenKingdom;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public GamePlayerImpl getGamePlayer(UUID playerId) {
        return super.getGamePlayer(playerId);
    }
}
