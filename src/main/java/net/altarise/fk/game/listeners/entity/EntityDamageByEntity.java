package net.altarise.fk.game.listeners.entity;

import net.altarise.api.utils.title.ActionBar;
import net.altarise.fk.FallenKingdom;
import net.altarise.fk.game.res.PlayerSkeleton;
import net.altarise.fk.impl.GamePlayerImpl;
import net.altarise.fk.impl.TeamImpl;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class EntityDamageByEntity implements Listener {

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {

        if (FallenKingdom.INSTANCE().getGameCoherence().getGameState().isState(0)) {
            event.setCancelled(true);
            return;
        }

        if (event.getEntity() instanceof EnderCrystal) {
            if (event.getDamager() instanceof TNTPrimed) {
                event.setCancelled(true);
            }
        }

        if (event.getDamager() instanceof Player && (event.getEntity() instanceof Player || event.getEntity() instanceof Skeleton)) {
            if (FallenKingdom.INSTANCE().getGameCoherence().getGameState().getState() <= 1) {
                event.setCancelled(true);
                new ActionBar("§6❖ Le PVP n'est pas encore actif").sendToPlayer((Player) event.getDamager());
                return;
            }

            if (event.getEntity() instanceof Player) {
                if (FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getDamager()).getPlayers().contains((Player) event.getEntity())) {
                    event.setCancelled(true);
                }
            }

        }

        if (event.getDamager() instanceof TNTPrimed) {
            TNTPrimed tnt = (TNTPrimed) event.getDamager();
            if (tnt.getSource() instanceof Player && event.getEntity() instanceof Player) {
                if (FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer((Player) tnt.getSource()).getPlayers().contains((Player) event.getEntity())) {
                    event.setCancelled(true);
                }
            }
        }


        if (event.getEntity() instanceof Skeleton) {
            if (event.getDamager() instanceof Player) {
                Skeleton skeleton = (Skeleton) event.getEntity();
                if (FallenKingdom.INSTANCE().getGameCoherence().playerSkeleton.containsKey(skeleton)) {
                    PlayerSkeleton playerSkeleton = FallenKingdom.INSTANCE().getGameCoherence().playerSkeleton.get(skeleton);
                    if (playerSkeleton.isTeammate((Player) event.getDamager())) {
                        event.setCancelled(true);
                    } else {
                        if (skeleton.getHealth() - event.getDamage() <= 0) {
                            Arrays.stream(playerSkeleton.getPlayerInventory().getContents()).forEach(itemStack -> Bukkit.getWorld("world").dropItem(skeleton.getLocation(), itemStack));
                            playerSkeleton.setDead();
                            FallenKingdom.INSTANCE().getGameCoherence().playerSkeleton.remove(skeleton);
                        }
                    }

                }
            } else event.setCancelled(true);

        }


        if (event.getEntity() instanceof EnderCrystal) {

            Player damager = getDamager(event.getDamager());
            if (damager == null) return;

            GamePlayerImpl gamePlayer = FallenKingdom.INSTANCE().getGamePlayer(damager.getUniqueId());
            if (!gamePlayer.canDamage()) return;
            setOnCooldown(gamePlayer);

            TeamImpl victim = FallenKingdom.INSTANCE().getGameCoherence().crystals.get((EnderCrystal) event.getEntity());
            if (victim == null || victim.getPlayers().contains(damager)) {
                new ActionBar("§c❖ Vous ne pouvez pas attaquer votre coeur").sendToPlayer(damager);
                return;
            }

            if (FallenKingdom.INSTANCE().getGameCoherence().getGameState().isState(0) ||
                    FallenKingdom.INSTANCE().getGameCoherence().getGameState().isState(1) ||
                    FallenKingdom.INSTANCE().getGameCoherence().getGameState().isState(2)) {
                new ActionBar("§c❖ Les assauts ne sont pas encore actifs").sendToPlayer(damager);
                return;
            }


            if (!victim.getCrystal().isDead())
                damager.playSound(damager.getLocation(), Sound.ENDERDRAGON_HIT, 2F, 0.5F);
            victim.getPlayers().forEach(players -> new ActionBar("§4❖ Votre coeur est attaqué !").sendToPlayer(players));

            int damage = calculateDamage(damager);
            victim.getCrystal().removeLife(damager, damage);


        }
    }

    private void setOnCooldown(GamePlayerImpl player) {
        player.setDamage(false);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.setDamage(true);
            }
        }.runTaskLater(FallenKingdom.INSTANCE(), 10);
    }

    private Player getDamager(Entity damager) {
        if (damager instanceof Player) {
            return (Player) damager;
        }
        if (damager instanceof Arrow) {
            Arrow arrow = (Arrow) damager;
            arrow.remove();
            if (arrow.getShooter() instanceof Player) {
                return (Player) arrow.getShooter();
            }
        } else if (damager instanceof TNTPrimed) {
            TNTPrimed tntPrimed = (TNTPrimed) damager;
            if (tntPrimed.getSource() instanceof Player) {
                return (Player) tntPrimed.getSource();
            }
        }
        return null;
    }

    private int calculateDamage(Player damager) {
        int damage = 1;
        ItemStack item = damager.getItemInHand();
        if (item.getType() == Material.BOW) {
            damage = 10;
        } else if (item.getType() == Material.TNT) {
            damage = 5;
        } else if (item.getType().toString().contains("SWORD")) {
            switch (item.getType()) {
                case WOOD_SWORD:
                    damage = 5;
                    break;
                case STONE_SWORD:
                    damage = 6;
                    break;
                case GOLD_SWORD:
                    damage = 7;
                    break;
                case IRON_SWORD:
                    damage = 8;
                    break;
                case DIAMOND_SWORD:
                    damage = 10;
                    break;
            }
        }
        return damage;
    }
}
