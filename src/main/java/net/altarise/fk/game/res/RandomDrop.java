package net.altarise.fk.game.res;

import net.altarise.api.utils.LocationUtils;
import net.altarise.fk.FallenKingdom;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomDrop {
    public FallingBlock bedrock;
    public final List<FallingBlock> fallingBlocks = new ArrayList<>();

    private final List<TinyLocation> woolLocations = new ArrayList<>();
    private final List<TinyLocation> woodLocations = new ArrayList<>();
    private final List<TinyLocation> barrierLocations = new ArrayList<>();
    private final TinyLocation bedrockLocation = new TinyLocation(0, 0, 0);

    private final Location randomLocation;
    private boolean isOpened = false;


    public RandomDrop() {
        randomLocation = FallenKingdom.INSTANCE().getGameProperties().getDropLocations().get(new Random().nextInt(FallenKingdom.INSTANCE().getGameProperties().getDropLocations().size()));

        for(int x = -1; x <= 1; x++) {
            for(int z = -1; z <= 1; z++) {
                for(int y = 0; y <= 1; y++) {
                    if(x != 0 && y != 0 && z != 0) woodLocations.add(new TinyLocation(x, y, z));
                }
            }
        }

        for(int x = -1; x <= 1; x+=2) {
            for(int z = -1; z <= 1; z+=2) {
                for(int y = 2; y <= 3; y++) {
                    barrierLocations.add(new TinyLocation(x, y, z));
                }
            }
        }

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = 4; y <= 6; y++) {
                    woolLocations.add(new TinyLocation(x, y, z));
                }
            }
        }

        for(int y = 4; y <= 6; y++) {
            woolLocations.add(new TinyLocation(0, y, 2));
            woolLocations.add(new TinyLocation(0, y, -2));
            woolLocations.add(new TinyLocation(2, y, 0));
            woolLocations.add(new TinyLocation(-2, y, 0));
        }

        woolLocations.add(new TinyLocation(-2, 5, -1));
        woolLocations.add(new TinyLocation(-2, 5, 1));
        woolLocations.add(new TinyLocation(-1, 5, 2));
        woolLocations.add(new TinyLocation(1, 5, 2));
        woolLocations.add(new TinyLocation(2, 5, 1));
        woolLocations.add(new TinyLocation(2, 5, -1));
        woolLocations.add(new TinyLocation(1, 5, -2));
        woolLocations.add(new TinyLocation(-1, 5, -2));

        woolLocations.add(new TinyLocation(0, 7, 0));
        woolLocations.add(new TinyLocation(-1, 7, 0));
        woolLocations.add(new TinyLocation(1, 7, 0));
        woolLocations.add(new TinyLocation(0, 7, -1));
        woolLocations.add(new TinyLocation(0, 7, 1));
    }


    @SuppressWarnings("deprecation")
    public void drop() {

        Bukkit.getWorld("world").loadChunk(randomLocation.getChunk());
        Bukkit.getWorld("world").getChunkAt(randomLocation).load(true);

        bedrock = Bukkit.getWorld("world").spawnFallingBlock(randomLocation.clone().add(bedrockLocation.getX(), bedrockLocation.getY() + 200, bedrockLocation.getZ()), Material.BEDROCK, (byte) 0);
        bedrock.setDropItem(false);
        bedrock.setHurtEntities(false);
        bedrock.setVelocity(bedrock.getVelocity().multiply(0.2));

        for (TinyLocation location : woodLocations) {
            spawnFallingBlock(location, Material.LOG);
        }

        for(TinyLocation location : barrierLocations) {
            spawnFallingBlock(location, Material.BARRIER);
        }

        for(TinyLocation location : woolLocations) {
            spawnFallingBlock(location, Material.WOOL);
        }



    }



    public void spawn() {
        final Location spawnLocation = Bukkit.getWorld("world").getHighestBlockAt(randomLocation).getLocation().add(0, -1, 0);
        spawnLocation.getBlock().setType(Material.BEDROCK);
        spawnLocation.getBlock().getRelative(0, 1, 0).setType(Material.CHEST);
        spawnLocation.clone().add(-3, 0, 0).getBlock().getRelative(0, 1, 0).setType(Material.WOOD);
        spawnLocation.clone().add(-4, 0, 0).getBlock().getRelative(0, 1, 0).setType(Material.WOOD);
        spawnLocation.clone().add(-2, 0, 2).getBlock().getRelative(0, 1, 0).setType(Material.WOOD);
        spawnLocation.clone().add(-2, 0, 1).getBlock().setType(Material.WOOD);
        spawnLocation.clone().add(-6, 0, -1).getBlock().getRelative(0, 1, 0).setType(Material.WOOL);
        spawnLocation.clone().add(-6, 0, -2).getBlock().getRelative(0, 1, 0).setType(Material.WOOL);
        spawnLocation.clone().add(-6, 0, -3).getBlock().getRelative(0, 1, 0).setType(Material.WOOL);
        spawnLocation.clone().add(-7, 0, -1).getBlock().getRelative(0, 1, 0).setType(Material.WOOL);
        spawnLocation.clone().add(-7, 0, -2).getBlock().getRelative(0, 1, 0).setType(Material.WOOL);
        spawnLocation.clone().add(-7, 0, -3).getBlock().getRelative(0, 1, 0).setType(Material.WOOL);
        spawnLocation.clone().add(-7, 0, -2).getBlock().getRelative(0, 2, 0).setType(Material.WOOL);
        spawnLocation.clone().add(-6, 0, -2).getBlock().getRelative(0, 2, 0).setType(Material.WOOL);
        spawnLocation.clone().add(-8, 0, -2).getBlock().getRelative(0, 1, 0).setType(Material.WOOL);
        spawnLocation.clone().add(-5, 0, -2).getBlock().getRelative(0, 1, 0).setType(Material.WOOL);

        Chest chest = (Chest) spawnLocation.getBlock().getRelative(0, 1, 0).getState();
        chest.getBlockInventory().setItem(11, new ItemStack(Material.TNT, 10));
        chest.getBlockInventory().setItem(13, new ItemStack(Material.ENDER_PEARL, 1));
        chest.getBlockInventory().setItem(15, new ItemStack(Material.GOLDEN_APPLE, 3));
        chest.update();

        for(int i = 0; i<5; i++) {
            Bukkit.getWorld("world").strikeLightningEffect(randomLocation);
        }



    }

    public String getStrTime(Player player) {
        if (FallenKingdom.INSTANCE().getScheduler().getDropTimer() <= 0) return "§e" + LocationUtils.getDirection(player, randomLocation);
        return DurationFormatUtils.formatDuration(FallenKingdom.INSTANCE().getScheduler().getDropTimer() * 1000, "§emm§7:§ess ") + LocationUtils.getDirection(player, randomLocation);

    }


    @SuppressWarnings("deprecation")
    private void spawnFallingBlock(TinyLocation location, Material material) {
        FallingBlock fallingBlock = Bukkit.getWorld("world").spawnFallingBlock(randomLocation.clone().add(location.getX(), location.getY() + 200, location.getZ()), material, (byte) 0);
        fallingBlock.setDropItem(false);
        fallingBlock.setHurtEntities(false);
        fallingBlock.setVelocity(fallingBlock.getVelocity().multiply(3.8));
        fallingBlocks.add(fallingBlock);
    }


    public long getDropTimer() {
        return FallenKingdom.INSTANCE().getScheduler().getDropTimer();
    }

    public Location getRandomLocation() {
        return randomLocation;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    static class TinyLocation {

        private final double x, y, z;

        public TinyLocation(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }


    }



}
