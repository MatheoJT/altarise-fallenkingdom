package net.altarise.fk.game.listeners.world;

import net.altarise.api.utils.title.Titles;
import net.altarise.fk.FallenKingdom;
import net.altarise.fk.game.GameState;
import net.altarise.fk.game.events.GameDayChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class DayChange implements Listener {

    @EventHandler
    public void onGameDayChange(GameDayChangeEvent event) {
        final int day = event.getDay();
        final GameState gameState = FallenKingdom.INSTANCE().getGameCoherence().getGameState();

        if (day >= 1) Bukkit.getWorld("world").setDifficulty(Difficulty.NORMAL);

        if (day == FallenKingdom.INSTANCE().getGameProperties().getPvpTime()) {
            FallenKingdom.INSTANCE().getScheduler().runCooldown(gameState.valueOf("assault"), FallenKingdom.INSTANCE().getGameProperties().getDayTime() * (FallenKingdom.INSTANCE().getGameProperties().getAssaultTime() - FallenKingdom.INSTANCE().getGameProperties().getPvpTime()));
            gameState.updateState();
            Bukkit.getOnlinePlayers().forEach(player -> Titles.sendTitle(player, 10, 30, 10, "§4⚔ §cPVP ACTIF §4⚔", null));
            Bukkit.getWorld("world").setPVP(true);
            FallenKingdom.INSTANCE().getGamePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.WOLF_GROWL, 1F, 1F));
            return;
        }

        if (day == FallenKingdom.INSTANCE().getGameProperties().getAssaultTime()) {
            FallenKingdom.INSTANCE().getScheduler().runCooldown(FallenKingdom.INSTANCE().getGameCoherence().getGameState().valueOf("deathmatch"), FallenKingdom.INSTANCE().getGameProperties().getDayTime() * (FallenKingdom.INSTANCE().getGameProperties().getDeathmatchTime() - FallenKingdom.INSTANCE().getGameProperties().getAssaultTime()));
            FallenKingdom.INSTANCE().getGameCoherence().getGameState().updateState();
            Bukkit.getOnlinePlayers().forEach(player -> Titles.sendTitle(player, 10, 30, 10, "§4⚡ §cASSAUTS ACTIF §4⚡", null));
            FallenKingdom.INSTANCE().getGamePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.WITHER_SPAWN, 1F, 1F));
            return;
        }

        if (day == FallenKingdom.INSTANCE().getGameProperties().getDeathmatchTime()) {
            FallenKingdom.INSTANCE().getGameCoherence().getGameState().updateState();
            Bukkit.getOnlinePlayers().forEach(player -> Titles.sendTitle(player, 10, 30, 10, "§4☠ §cDEATHMATCH §4☠", null));
            FallenKingdom.INSTANCE().getGameProperties().getTeams().forEach(team -> team.getCrystal().runDeathMatch());
            FallenKingdom.INSTANCE().getGamePlayers().forEach(player -> {
                player.playSound(player.getLocation(), Sound.WITHER_DEATH, 1F, 1F);
                player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
            });
        }

    }

}
