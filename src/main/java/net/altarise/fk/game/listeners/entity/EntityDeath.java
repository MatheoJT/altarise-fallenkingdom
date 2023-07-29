package net.altarise.fk.game.listeners.entity;

import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class EntityDeath implements Listener {

    @EventHandler
    public void mobDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            EntityType type = event.getEntity().getType();
            int r = new Random().nextInt(100) + 1;
            event.getDrops().clear();

            switch (type) {
                case WITCH:
                    if (r <= 25) {
                        event.getDrops().add(new ItemStack(Material.NETHER_STALK));
                    } else if (r <= 55) {
                        event.getDrops().add(new ItemStack(Material.GLASS_BOTTLE));
                    } else if (r <= 65) {
                        event.getDrops().add(new ItemStack(Material.EXP_BOTTLE, 2));
                    } else if (r <= 85) {
                        event.getDrops().add(new ItemStack(Material.STICK));
                    }
                    break;
                case SPIDER:
                    if (r <= 60) {
                        event.getDrops().add(new ItemStack(Material.STRING, new Random().nextInt(2) + 1));
                    } else if (r <= 90) {
                        event.getDrops().add(new ItemStack(Material.SPIDER_EYE));
                    }
                    break;
                case CREEPER:
                    Creeper creeper = (Creeper) event.getEntity();
                    if (creeper.isPowered()) {
                        event.getDrops().add(new ItemStack(Material.SULPHUR, new Random().nextInt(4) + 1));
                        event.getDrops().add(new ItemStack(Material.TNT, 1));
                    } else {
                        event.getDrops().add(new ItemStack(Material.SULPHUR, new Random().nextInt(4) + 1));
                    }
                    break;
                case ZOMBIE:
                    event.getDrops().add(new ItemStack(Material.SAND, new Random().nextInt(5) + 1));
                    if (new Random().nextInt(3) == 2) {
                        event.getDrops().add(new ItemStack(Material.ROTTEN_FLESH, new Random().nextInt(4) + 1));
                    }
                    break;
                case ENDERMAN:
                    if (r <= 50) {
                        event.getDrops().add(new ItemStack(Material.DIRT, new Random().nextInt(2) + 1));
                    } else if (r <= 70) {
                        event.getDrops().add(new ItemStack(Material.DIAMOND, 1));
                    } else {
                        event.getDrops().add(new ItemStack(Material.GOLDEN_APPLE, new Random().nextInt(3) + 1));
                    }
                    break;
            }
        }
    }

}
