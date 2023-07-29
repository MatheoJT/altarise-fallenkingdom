package net.altarise.fk.game.listeners.player;

import net.altarise.fk.FallenKingdom;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPick(PlayerPickupItemEvent event) {
        if (event.getItem().getItemStack().getType().equals(Material.OBSIDIAN)) event.setCancelled(true);
        if (event.getItem().getItemStack().getType().equals(Material.BEDROCK)) event.setCancelled(true);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (FallenKingdom.INSTANCE().getGamePlayers().contains(event.getPlayer())) {
            event.setRespawnLocation(FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(event.getPlayer()).getLocation());
        }
    }

}
