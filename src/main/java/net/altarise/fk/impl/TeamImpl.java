package net.altarise.fk.impl;

import net.altarise.api.utils.ColorUtils;
import net.altarise.fk.game.res.Crystal;
import net.altarise.fk.game.res.Region;
import net.altarise.gameapi.engine.properties.Team;
import org.bukkit.ChatColor;
import org.bukkit.Location;


public class TeamImpl extends Team {

    private final String id;
    private final String name;
    private final byte byteColor;
    private final Region baseRegion;
    private int hearthKill = 0;
    private int kills = 0;
    private final Location  chestLocation;
    private final Crystal crystal;


    public TeamImpl(String id, String name, Location chestLocation, ChatColor chatColor, byte byteColor, Location spawnLocation, Location crystalLocation, Region baseRegion) {
        super(id, name, chatColor, ColorUtils.chatColorToColor(chatColor), spawnLocation);
        this.id = id;
        this.name = name;
        this.byteColor = byteColor;
        this.baseRegion = baseRegion;
        this.chestLocation = chestLocation;
        crystal = new Crystal(this, crystalLocation);
    }




    public void addKill() {
        kills++;
    }

    public void addHKill() {
        hearthKill++;
    }


    @Override
    public String getId() {
        return id;
    }

    public Crystal getCrystal() {
        return crystal;
    }

    public Region getRegion() {
        return baseRegion;
    }

    @Override
    public String getName() {
        return name;
    }

    public byte getByteColor() {
        return byteColor;
    }

    public Location getChestLocation() {
        return chestLocation;
    }

    public int getKills() {
        return kills;
    }

    public int getHearthKill() {
        return hearthKill;
    }




}
