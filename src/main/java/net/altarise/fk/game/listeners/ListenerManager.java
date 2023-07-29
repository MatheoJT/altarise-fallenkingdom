package net.altarise.fk.game.listeners;

import net.altarise.fk.game.listeners.block.BlockBreak;
import net.altarise.fk.game.listeners.block.BlockChange;
import net.altarise.fk.game.listeners.block.BlockPlace;
import net.altarise.fk.game.listeners.entity.*;
import net.altarise.fk.game.listeners.player.*;
import net.altarise.fk.game.listeners.world.Brew;
import net.altarise.fk.game.listeners.world.DayChange;
import net.altarise.fk.game.listeners.world.TimeChange;
import net.altarise.fk.game.listeners.world.World;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class ListenerManager {

    private final Plugin plugin;

    public ListenerManager(Plugin plugin) {
        this.plugin = plugin;
        entity();
        player();
        block();
        world();
    }

    private void register(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    private void block() {
        register(new BlockBreak());
        register(new BlockChange());
        register(new BlockPlace());
    }

    private void entity() {
        register(new CreatureSpawn());
        register(new EntityAggro());
        register(new EntityDamage());
        register(new EntityDamageByEntity());
        register(new EntityDeath());
        register(new EntityExplode());
        register(new EntityListener());
    }

    private void player() {
        register(new Connect());
        register(new Death());
        register(new Disconnect());
        register(new Interact());
        register(new Move());
        register(new PlayerListener());
        register(new Respawn());
        register(new Reconnect());
    }

    private void world() {
        register(new Brew());
        register(new World());
        register(new DayChange());
        register(new TimeChange());
    }


}
