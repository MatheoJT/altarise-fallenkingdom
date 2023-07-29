package net.altarise.fk.game.listeners.player;

import net.altarise.api.utils.LocationUtils;
import net.altarise.api.utils.title.ActionBar;
import net.altarise.fk.FallenKingdom;
import net.altarise.gameapi.basic.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Door;
import org.bukkit.material.Gate;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Interact implements Listener {

    private final List<Material> unInteractItems = Arrays.asList(Material.BOAT, Material.WATER_BUCKET, Material.LAVA_BUCKET);
    private final HashMap<Block, BukkitTask> closed = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (FallenKingdom.INSTANCE().getGameCoherence().getGameState().isState(0)) return;
        if (!FallenKingdom.INSTANCE().getGamePlayers().contains(event.getPlayer())) return;

        if (event.getPlayer().getItemInHand() != null) {
            if (unInteractItems.contains(event.getPlayer().getItemInHand().getType())) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getClickedBlock() != null) {
            Block block = event.getClickedBlock();

            if (!event.getPlayer().isSneaking()) {
                if (block.getType().equals(Material.FURNACE)) {
                    if (block.hasMetadata("OWNER_UUID")) {
                        if (!block.getMetadata("OWNER_UUID").get(0).asString().equals(event.getPlayer().getUniqueId().toString())) {
                            event.setCancelled(true);
                            new ActionBar("§c❖ Ce four ne vous appartient pas").sendToPlayer(event.getPlayer());
                        } else return;
                    } else {
                        block.setMetadata("OWNER_UUID", new FixedMetadataValue(FallenKingdom.INSTANCE(), event.getPlayer().getUniqueId().toString()));
                        new ActionBar("§6❖ Ce four vous appartient, seul vos ennemis peuvent interagir avec").sendToPlayer(event.getPlayer());
                        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.VILLAGER_IDLE, 1, 1);
                        return;
                    }
                }
            }


            if (block.getType().equals(Material.CHEST)) {
                if (FallenKingdom.INSTANCE().getGameCoherence().getRandomDrop() != null && !FallenKingdom.INSTANCE().getGameCoherence().getRandomDrop().isOpened() && FallenKingdom.INSTANCE().getGameCoherence().getRandomDrop().getDropTimer() <= 0) {
                    if (LocationUtils.getAroundLocations(FallenKingdom.INSTANCE().getGameCoherence().getRandomDrop().bedrock.getLocation(), 2).contains(block.getLocation())) {
                        Bukkit.broadcastMessage("§3§lFallen Kingdom §8▪  §7" + FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(event.getPlayer()).getChatColor() + event.getPlayer().getName() + " §7a ouvert le coffre du larguage !");
                        FallenKingdom.INSTANCE().getGameCoherence().getRandomDrop().setOpened(true);
                    }
                }
                return;
            }

            if (!FallenKingdom.INSTANCE().getGameProperties().isPlayerTeamBase(event.getPlayer(), event.getClickedBlock().getLocation()) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (!FallenKingdom.INSTANCE().getGameProperties().isInATeamBase(event.getClickedBlock().getLocation()))
                    return;
                new ActionBar("§c❖ Vous ne pouvez pas intéragir ici").sendToPlayer(event.getPlayer());
                event.setCancelled(true);
            } else {
                if (isDoor(block)) closeDoor(block);
            }


        }


    }


    private boolean isDoor(Block block) {
        BlockState state = block.getState();
        if (state.getData() instanceof Door) return true;
        return state.getData() instanceof Gate;
    }

    private void closeDoor(Block block) {
        BlockState state = block.getState();


        if (state.getData() instanceof Door) {
            Door door = (Door) state.getData();
            if (door.isTopHalf()) {
                state = block.getRelative(BlockFace.DOWN).getState();
                door = (Door) state.getData();
            }
            if (closed.containsKey(block)) {
                closed.get(block).cancel();
                closed.remove(block);
            }
            if (door.isOpen()) return;
            Door finalDoor = door;
            BlockState finalState = state;
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    finalDoor.setOpen(false);
                    finalState.setData(finalDoor);
                    finalState.update();
                    block.getWorld().playSound(block.getLocation(), Sound.DOOR_CLOSE, 1.0F, 0);
                    closed.remove(block);
                }
            }.runTaskLater(FallenKingdom.INSTANCE(), 20 * 5);
            closed.put(block, task);
            return;
        }

        if (state.getData() instanceof Gate) {
            Gate gate = (Gate) state.getData();
            if (closed.containsKey(block)) {
                closed.get(block).cancel();
                closed.remove(block);
            }
            if (gate.isOpen()) return;
            BlockState finalState1 = state;
            BukkitTask fenceTask = new BukkitRunnable() {
                @Override
                public void run() {
                    gate.setOpen(false);
                    finalState1.setData(gate);
                    finalState1.update();
                    block.getWorld().playSound(block.getLocation(), Sound.DOOR_CLOSE, 1.0F, 0);

                }
            }.runTaskLater(FallenKingdom.INSTANCE(), 20 * 5);
            closed.put(block, fenceTask);

        }
    }


    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (FallenKingdom.INSTANCE().getGameAPI().getGameState().equals(GameState.WAITING)) event.setCancelled(true);
        if (FallenKingdom.INSTANCE().getGameAPI().getGameState().equals(GameState.STARTING)) event.setCancelled(true);
    }

}
