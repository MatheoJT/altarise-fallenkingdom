package net.altarise.fk.impl;

import net.altarise.gameapi.engine.GamePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GamePlayerImpl extends GamePlayer {


    private boolean isInBase;
    private final List<Block> furnace;
    private int kills;
    private int deaths;
    private boolean damage = true;


    public GamePlayerImpl(Player player) {
        super(player);
        this.furnace = new ArrayList<>();
        this.isInBase = false;
        this.kills = 0;
        this.deaths = 0;
    }

    public void addKill() {
        kills++;
    }

    public void addDeath() {
        deaths++;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDamage(boolean damage) {
        this.damage = damage;
    }

    public boolean canDamage() {
        return damage;
    }

    public List<Block> getFurnace() {
        return furnace;
    }

    public boolean isInBase() {
        return isInBase;
    }

    public void setBase(boolean inBase) {
        isInBase = inBase;
    }
    @Override
    public Map<String, String> getGameData(Map<String, String> map) {
        return null;
    }
}
