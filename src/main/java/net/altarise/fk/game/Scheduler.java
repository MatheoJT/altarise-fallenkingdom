package net.altarise.fk.game;

import net.altarise.api.utils.LocationUtils;
import net.altarise.fk.FallenKingdom;
import net.altarise.fk.game.events.GameDayChangeEvent;
import net.altarise.fk.game.events.GameTimeChangeEvent;
import net.altarise.fk.impl.TeamImpl;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

public class Scheduler extends BukkitRunnable {

    private long timer = 0;
    private long cooldown = 0;
    private int stage;
    private long randomTimer = ThreadLocalRandom.current().nextLong(FallenKingdom.INSTANCE().getGameProperties().getDayTime(), (FallenKingdom.INSTANCE().getGameProperties().getDayTime() * FallenKingdom.INSTANCE().getGameProperties().getAssaultTime()));


    @Override
    public void run() {
        timer++;
        Bukkit.getPluginManager().callEvent(new GameTimeChangeEvent(timer, FallenKingdom.INSTANCE().getGameProperties().getDayTime()));
        if(randomTimer > 0) randomTimer--;
        if(cooldown != 0) cooldown--;


        if (timer == FallenKingdom.INSTANCE().getGameProperties().getDayTime()) {
            FallenKingdom.INSTANCE().getGameCoherence().day++;
            Bukkit.getPluginManager().callEvent(new GameDayChangeEvent(FallenKingdom.INSTANCE().getGameCoherence().day));
            timer = 0;
        }

         
            dropRunner();
            verifyTeamsIsEmpty();
            fireIfOnCrystal();



    }

    public long getDropTimer() {
        return randomTimer;
    }

    public void stop() {
        cancel();
    }

    public void runCooldown(int stage, long time) {
        this.cooldown = time; this.stage = stage;
    }


    private void dropRunner() {
        if (randomTimer == 30) Bukkit.broadcastMessage("§3§lFallen Kingdom §8▪ §cLargage dans §e30 §csecondes");
        if (randomTimer <= 3 && randomTimer >= 1) {
            FallenKingdom.INSTANCE().getGamePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1F, 1F));
        } if (randomTimer == 0) {
            FallenKingdom.INSTANCE().getGameCoherence().getRandomDrop().drop();
            randomTimer = -1;
        }


    }

    private void verifyTeamsIsEmpty() {
        if (FallenKingdom.INSTANCE().getGameProperties().getTeams().size() <= 1) {
            FallenKingdom.INSTANCE().getGameCoherence().updateEnding(FallenKingdom.INSTANCE().getGameProperties().getTeams().get(0));
        }
    }

    public String getStrCooldown() {
        return " §b▪ §f" + FallenKingdom.INSTANCE().getGameCoherence().getGameState().getStrStage(stage) + ": " + DurationFormatUtils.formatDuration(cooldown * 1000, "§emm§7:§ess");
    }

    public String getStrTimer() {
        double timeRatio = (double) timer / FallenKingdom.INSTANCE().getGameProperties().getDayTime();
        int hours = (int) (timeRatio * 24);
        return ChatColor.WHITE + String.valueOf(hours) + "§3h";
    }



    private void fireIfOnCrystal() {
        for(Player player : FallenKingdom.INSTANCE().getGamePlayers()) {
            for(TeamImpl team : FallenKingdom.INSTANCE().getGameProperties().getTeams()) {
                if(LocationUtils.getAroundLocations(team.getCrystal().getLocation(), 1).contains(player.getLocation().getBlock().getLocation())) {
                    if(team.getPlayers().contains(player)) {
                        player.setFireTicks(40);
                        player.damage(1);
                    }
                }
            }
        }
    }
}
