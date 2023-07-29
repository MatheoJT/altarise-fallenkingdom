package net.altarise.fk.game.listeners.block;

import net.altarise.api.utils.LocationUtils;
import net.altarise.api.utils.title.ActionBar;
import net.altarise.fk.FallenKingdom;
import net.altarise.gameapi.basic.GameState;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class BlockPlace implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlaceBlock(BlockPlaceEvent event) {
        if (!FallenKingdom.INSTANCE().getGamePlayers().contains(event.getPlayer())) return;
        if (FallenKingdom.INSTANCE().getGameAPI().getGameState().equals(GameState.WAITING) || FallenKingdom.INSTANCE().getGameAPI().getGameState().equals(GameState.STARTING)) {
            event.setCancelled(true);
            return;
        }

        if (event.getBlock().getType().equals(Material.TNT)) {
            if (FallenKingdom.INSTANCE().getGameCoherence().getGameState().getState() <= 2) {
                new ActionBar("§c❖ Les assauts ne sont pas encore actifs").sendToPlayer(event.getPlayer());
                event.setCancelled(true);
                return;
            }
            return;
        }

        if (!FallenKingdom.INSTANCE().getGameProperties().isPlayerTeamBase(event.getPlayer(), event.getBlock().getLocation())) {
            event.setCancelled(true);
            new ActionBar("§c❖ Vous ne pouvez pas placer de block dans les autres bases").sendToPlayer(event.getPlayer());
            event.getPlayer().damage(0.5);
        } else {
            if (LocationUtils.getAroundLocations(FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(event.getPlayer()).getCrystal().getLocation(), 3).contains(event.getBlock().getLocation())) {
                event.setCancelled(true);
                new ActionBar("§c❖ Vous ne pouvez pas placer de block autour du cristal").sendToPlayer(event.getPlayer());
                return;
            }
            if (event.getBlock().getType().equals(Material.FURNACE)) {
                event.getBlock().setMetadata("OWNER_UUID", new FixedMetadataValue(FallenKingdom.INSTANCE(), event.getPlayer().getUniqueId().toString()));
                new ActionBar("§6❖ Ce four vous appartient, seul vos ennemis peuvent interagir avec").sendToPlayer(event.getPlayer());
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.VILLAGER_IDLE, 1, 1);
            }
            return;
        }

        event.setCancelled(true);
        event.getPlayer().damage(0.5);
        new ActionBar("§c❖ Vous ne pouvez pas placer de block ici").sendToPlayer(event.getPlayer());

    }

}
