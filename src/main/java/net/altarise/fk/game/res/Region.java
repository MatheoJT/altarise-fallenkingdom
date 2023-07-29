package net.altarise.fk.game.res;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Region {
    public final Location minLoc, maxLoc, first, second;


    public Region(Location first, Location second) {
        this.first = first; this.second = second;
        minLoc     = new Location(first.getWorld(), min(first.getX(), second.getX()), min(first.getY(), second.getY()), min(first.getZ(), second.getZ()));
        maxLoc     = new Location(first.getWorld(), max(first.getX(), second.getX()), max(first.getY(), second.getY()), max(first.getZ(), second.getZ()));
    }

    public double min(double a, double b) {
        return Math.min(a, b);
    }

    public double max(double a, double b) {
        return Math.max(a, b);
    }

    public boolean isInArea(Location loc) {
        return (minLoc.getX() <= loc.getX() && minLoc.getZ() <= loc.getZ() && maxLoc.getX() >= loc.getX() && maxLoc.getZ() >= loc.getZ());
    }

    public Location randomLocation() {
        Location min   = first; Location max = second;
        Location range = new Location(min.getWorld(), Math.abs(max.getX() - min.getX()), min.getY(), Math.abs(max.getZ() - min.getZ()));
        return new Location(min.getWorld(), (Math.random() * range.getX()) + (Math.min(min.getX(), max.getX())), range.getY(), (Math.random() * range.getZ()) + (Math.min(min.getZ(), max.getZ())));
    }


    @SuppressWarnings("unused")
    public Location getMiddle() {
        double a, b; a = (maxLoc.getX() - minLoc.getX()) / 2D + minLoc.getX();
                     b = (maxLoc.getZ() - minLoc.getZ()) / 2D + minLoc.getZ();

        return new Location(Bukkit.getWorld("world"), a, minLoc.getY(), b);
    }
}
