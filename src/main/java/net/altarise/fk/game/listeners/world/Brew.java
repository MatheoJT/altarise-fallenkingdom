package net.altarise.fk.game.listeners.world;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class Brew implements Listener {
    @EventHandler
    public void onBrew(BrewEvent event) {
        if (event.getContents() == null) return;
        if (event.getContents().getIngredient() == null) return;
        if (event.getContents().getIngredient().getType().equals(Material.BLAZE_POWDER)) event.setCancelled(true);
        if (event.getContents().getItem(3).getType().equals(Material.POTION)) {
            Potion potion = Potion.fromItemStack(event.getContents().getItem(3));
            if (potion.getType().equals(PotionType.NIGHT_VISION) && event.getContents().getIngredient().getType().equals(Material.FERMENTED_SPIDER_EYE))
                event.setCancelled(true);
        }
    }
}
