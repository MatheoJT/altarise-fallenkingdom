package net.altarise.fk.game.res;

import net.altarise.fk.impl.TeamImpl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.Inventory;

public class PlayerSkeleton {

    private final Player player;
    private final Inventory playerInventory;
    private final TeamImpl teamPlayer;
    private Skeleton skeleton;
    private boolean dead = false;

    public PlayerSkeleton(Player player, TeamImpl teamPlayer) {
        this.player = player;
        playerInventory = player.getInventory();
        this.teamPlayer = teamPlayer;
    }

    public void spawn() {
        this.skeleton = (Skeleton) player.getWorld().spawnEntity(player.getLocation(), EntityType.SKELETON);
        this.skeleton.setCustomName(teamPlayer.getChatColor() + player.getName());
        this.skeleton.setCustomNameVisible(true);
        this.skeleton.setHealth(20);
        this.skeleton.setSkeletonType(Skeleton.SkeletonType.WITHER);
    }

    public boolean isTeammate(Player player) {
        return teamPlayer.getPlayers().contains(player);
    }

    public boolean isDead() {
        return dead;
    }


    public void setDead() {
        this.dead = !dead;
    }

    public Player getPlayer() {
        return player;
    }

    public TeamImpl getTeamPlayer() {
        return teamPlayer;
    }

    public Skeleton getSkeleton() {
        return skeleton;
    }

    public Inventory getPlayerInventory() {
        return playerInventory;
    }
}
