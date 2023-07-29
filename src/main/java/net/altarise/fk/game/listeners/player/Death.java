package net.altarise.fk.game.listeners.player;

import net.altarise.fk.FallenKingdom;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class Death implements Listener {


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!FallenKingdom.INSTANCE().getGamePlayers().contains(player)) return;

        if (player.getHealth() - event.getFinalDamage() <= 0) {
            event.setCancelled(true);
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setFireTicks(0);
            FallenKingdom.INSTANCE().getGamePlayer(player.getUniqueId()).addDeath();
            player.teleport(FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(player).getLocation());

            if (event.getCause() == null) {
                Bukkit.broadcastMessage(" §4☠ §fLe joueur §c" + FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(player).getChatColor() + player.getName() + "§f est mort");
                return;
            }

            if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                //check if damager is a player with player.LastDamageCause()
                if (player.getLastDamageCause().getEntity() != null) {
                    Player damager = (Player) player.getLastDamageCause().getEntity();
                    if (FallenKingdom.INSTANCE().getGamePlayers().contains(damager)) {
                        Bukkit.broadcastMessage(" §4⚔ §fLe joueur §c" + FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(player).getChatColor() + player.getName() + " §fa été tué par §c" + FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(damager).getChatColor() + damager.getName());
                        FallenKingdom.INSTANCE().getGamePlayer(damager.getUniqueId()).addKill();
                        FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(damager).addKill();
                        return;
                    }
                }

                if (event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                    //check if projectile was shot by a player
                    if (player.getLastDamageCause().getEntity() != null) {
                        if (player.getLastDamageCause().getEntity() instanceof Arrow) {
                            Arrow arrow = (Arrow) player.getLastDamageCause().getEntity();
                            Player damager = (Player) arrow.getShooter();
                            if (FallenKingdom.INSTANCE().getGamePlayers().contains(damager)) {
                                Bukkit.broadcastMessage(" §4⚔ §fLe joueur §c" + FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(player).getChatColor() + player.getName() + " §fa été tué par §c" + FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(damager).getChatColor() + damager.getName());
                                FallenKingdom.INSTANCE().getGamePlayer(damager.getUniqueId()).addKill();
                                FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(damager).addKill();
                                return;
                            }
                        }
                    }
                }

                String cause;

                switch (event.getCause()) {
                    case FALL:
                        cause = "de §4chute";
                        break;
                    case ENTITY_EXPLOSION:
                    case BLOCK_EXPLOSION:
                        cause = "dans une §4explosion";
                        break;
                    case FIRE:
                    case FIRE_TICK:
                        cause = "dans le §4feu";
                        break;
                    case LAVA:
                        cause = "dans la §4lave";
                        break;
                    case MAGIC:
                        cause = "par la §4magie";
                        break;
                    case LIGHTNING:
                        cause = "par la §4foudre";
                        break;
                    case POISON:
                        cause = "par le §4poison";
                        break;
                    case DROWNING:
                        cause = "de §4noyade";
                        break;
                    case ENTITY_ATTACK:
                        switch (player.getLastDamageCause().getEntity().getType()) {
                            case CREEPER:
                                cause = "par un §4creeper";
                                break;
                            case SKELETON:
                                cause = "par un §4squelette";
                                break;
                            case SPIDER:
                                cause = "par une §4araignée";
                                break;
                            case ZOMBIE:
                                cause = "par un §4zombie";
                                break;
                            case SLIME:
                                cause = "par un §4slime";
                                break;
                            case GHAST:
                                cause = "par un §4ghast";
                                break;
                            case PIG_ZOMBIE:
                                cause = "par un §4zombie pigman";
                                break;
                            case ENDERMAN:
                                cause = "par un §4enderman";
                                break;
                            case CAVE_SPIDER:
                                cause = "par une §4araignée de cave";
                                break;
                            case SILVERFISH:
                                cause = "par un §4silverfish";
                                break;
                            case BLAZE:
                                cause = "par un §4blaze";
                                break;
                            case MAGMA_CUBE:
                                cause = "par un §4magma cube";
                                break;
                            case ENDER_DRAGON:
                                cause = "par le §4dragon";
                                break;
                            case WITHER:
                                cause = "par le §4wither";
                                break;
                            case WITCH:
                                cause = "par une §4sorcière";
                                break;
                            case ENDERMITE:
                                cause = "par un §4endermite";
                                break;
                            case GUARDIAN:
                                cause = "par un §4guardian";
                                break;
                            default:
                                cause = "par un §4monstre";
                                break;
                        }
                        break;
                    case FALLING_BLOCK:
                        cause = "par un §4bloc";
                        break;
                    default:
                        cause = "";
                        break;
                }

                Bukkit.broadcastMessage(" §4☠ §fLe joueur §c" + FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(player).getChatColor() + player.getName() + "§f est mort " + cause);

            }


        }


    }
}
