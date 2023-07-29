package net.altarise.fk.game.res;

import net.altarise.api.utils.title.Titles;
import net.altarise.fk.FallenKingdom;
import net.altarise.fk.impl.TeamImpl;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Crystal {

    private final Location location;
    private Integer life;
    private final TeamImpl team;
    private EnderCrystal crystal;
    private boolean dead;
    private BukkitTask regenTask = null;
    private boolean regenRunning = false;
    private boolean cooldown = false;



    public Crystal(TeamImpl team, Location location) {
        this.life = 500;
        this.location = location;
        this.dead = false;
        this.team = team;
    }

    public void spawn() {
        this.crystal = (EnderCrystal) location.getWorld().spawnEntity(location, EntityType.ENDER_CRYSTAL);
        this.crystal.setCustomName("§fCoeur " + team.getChatColor() + team.getName() + " §8: " + getStrLife());
        this.crystal.setCustomNameVisible(true);
    }

    public void updateName(String value) {
        this.crystal.setCustomName("§fCoeur " + team.getChatColor() + team.getName() + " §8: " + value);
    }


    public boolean isDead() {
        return dead;
    }


    public Location getLocation() {
        return location;
    }

        public void removeLife(Player damager, int i) {
            if (dead) return;
            life = life - i;
            updateName(getStrLife());

            stopRegen();
            regen();
            ping(damager);
            if (life == 1) {
                team.getPlayers().forEach(player -> player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL)));
            }

            if (life <= 1) {
                this.dead = true;
                stopRegen();
                FallenKingdom.INSTANCE().getGameCoherence().setLose(damager, team);

            }


        }

    public EnderCrystal getCrystal() {
        return crystal;
    }

    public String getStrLife() {
        if (dead) return "§cEliminée";
        if (life <= 100) return ChatColor.DARK_RED + String.valueOf(life) + "§c❤";
        if (life <= 250) return ChatColor.GOLD + String.valueOf(life) + "§c❤";
        return ChatColor.GREEN + String.valueOf(life) + "§c❤";
    }

    private void stopRegen() {
        if (regenTask != null) {
            regenTask.cancel();
            regenTask = null;
            regenRunning = false;
        }
    }


    private void regen() {
        if (!regenRunning) {
            regenRunning = true;
            regenTask = new BukkitRunnable() {
                @Override
                public void run() {
                    if (isDead() || FallenKingdom.INSTANCE().getGameCoherence().getGameState().isState(4)) {
                        cancel();
                        regenTask = null;
                        regenRunning = false;
                        return;
                    }
                    if (life < 500) {
                        life++;
                       }
                  }
               }.runTaskTimer(FallenKingdom.INSTANCE(), 20 * 30, 20);
         }
        }



        private void ping(Player damager) {
            if (damager == null) return; if (cooldown) return; cooldown = true;
            team.getPlayers().forEach(player -> {
                player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                Titles.sendTitle(player, 10, 20, 10, "§cVotre coeur est attaqué !", " " );

            });

            new BukkitRunnable() {
                @Override
                public void run() {
                    cooldown = false;
                }
            }.runTaskLater(FallenKingdom.INSTANCE(), 20 * 5);
        }

        public void runDeathMatch() {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (life > 1) {
                        life--;
                    }
                }
            }.runTaskTimer(FallenKingdom.INSTANCE(), 0, 20 * 2);
        }



}
