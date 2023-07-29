package net.altarise.fk.game.listeners.block;

import net.altarise.api.utils.title.ActionBar;
import net.altarise.fk.FallenKingdom;
import net.altarise.gameapi.basic.GameState;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreak implements Listener {

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        if (!FallenKingdom.INSTANCE().getGamePlayers().contains(event.getPlayer())) return;
        if (FallenKingdom.INSTANCE().getGameAPI().getGameState().equals(GameState.WAITING) || FallenKingdom.INSTANCE().getGameAPI().getGameState().equals(GameState.STARTING)) {
            event.setCancelled(true);
            return;
        }

        if (event.getBlock().getType().equals(Material.FURNACE)) {
            if (FallenKingdom.INSTANCE().getGameCoherence().getGameState().getState() <= 2) {
                new ActionBar("§c❖ Les assauts ne sont pas encore actifs").sendToPlayer(event.getPlayer());
                event.setCancelled(true);
                return;
            }
            return;
        }

        if (FallenKingdom.INSTANCE().getGameProperties().isInATeamBase(event.getBlock().getLocation()) && !FallenKingdom.INSTANCE().getGameProperties().isPlayerTeamBase(event.getPlayer(), event.getBlock().getLocation())) {
            event.setCancelled(true);
            new ActionBar("§c❖ Vous ne pouvez pas casser de block dans les autres bases").sendToPlayer(event.getPlayer());
        } else {
            if (event.getBlock().getType().equals(Material.FURNACE)) {
                if (event.getBlock().hasMetadata("OWNER_UUID")) {
                    if (!event.getBlock().getMetadata("OWNER_UUID").get(0).asString().equals(event.getPlayer().getUniqueId().toString())) {
                        event.setCancelled(true);
                        new ActionBar("§c❖ Ce four ne vous appartient pas").sendToPlayer(event.getPlayer());
                    } else {
                        event.getBlock().removeMetadata("OWNER_UUID", FallenKingdom.INSTANCE());
                    }
                }
            }
        }

        if (event.getBlock().getType().equals(Material.HAY_BLOCK)) {
            event.setCancelled(true);
            event.getPlayer().getInventory().addItem(new ItemStack(Material.BREAD, 7));
            event.getPlayer().updateInventory();
            event.getBlock().setType(Material.AIR);
        }
    }

}
