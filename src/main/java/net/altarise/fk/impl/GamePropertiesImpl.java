package net.altarise.fk.impl;

import net.altarise.api.utils.LocationUtils;
import net.altarise.fk.FallenKingdom;
import net.altarise.fk.game.res.Region;
import net.altarise.gameapi.engine.properties.Team;
import net.altarise.gameapi.engine.properties.TeamProperties;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class GamePropertiesImpl implements net.altarise.gameapi.engine.properties.GameProperties {


    private final int      maxPlayersPerTeam;
    private final int      maxPlayer;
    private final int      minPlayerStart;
    private final int      startTimer;
    private final Location spawn;
    private final long     dayTime;
    private final long     pvpTime;
    private final long     assaultTime;
    private final long     deathmatchTime;


    private final List<TeamImpl> teams = new ArrayList<>();


    private final List<Location> dropLocations = new ArrayList<>();


    public GamePropertiesImpl(Plugin plugin) {
        final ConfigurationSection configuration = plugin.getConfig().getConfigurationSection("properties");
        maxPlayer = configuration.getInt("max-player");
        minPlayerStart = configuration.getInt("min-player-start"); startTimer = configuration.getInt("start-timer");
        spawn = LocationUtils.str2loc(configuration.getString("spawn"));
        dayTime = configuration.getLong("day-time");
        pvpTime = configuration.getLong("pvp-time");
        assaultTime = configuration.getLong("assault-time");
        deathmatchTime = configuration.getLong("deathmatch-time");
        maxPlayersPerTeam = configuration.getInt("max-per-team");


        Block middleChest = Bukkit.getWorld("world").getBlockAt(LocationUtils.str2loc(configuration.getString("middle-chest")));
        middleChest.setType(Material.CHEST);
        Chest chest = (Chest) middleChest.getState();

        ItemStack fireResist = new ItemStack(Material.POTION); Potion potion = new Potion(PotionType.FIRE_RESISTANCE);
        potion.setSplash(true); potion.setHasExtendedDuration(true); potion.apply(fireResist);

        ItemStack instantDamage = new ItemStack(Material.POTION);
        Potion    potion1       = new Potion(PotionType.INSTANT_DAMAGE); potion1.setSplash(true);
        potion1.apply(instantDamage);


        chest.getBlockInventory().setItem(3, new ItemStack(Material.TNT, 1));
        chest.getBlockInventory().setItem(4, new ItemStack(Material.TNT, 1));
        chest.getBlockInventory().setItem(5, new ItemStack(Material.TNT, 1));
        chest.getBlockInventory().setItem(12, fireResist);

        ItemStack ironPick = new ItemStack(Material.IRON_PICKAXE);
        ironPick.setDurability((short) 230);
        ironPick.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 2);
        ironPick.addEnchantment(Enchantment.DIG_SPEED, 2);

        chest.getBlockInventory().setItem(13, ironPick); chest.getBlockInventory().setItem(14, instantDamage);
        chest.getBlockInventory().setItem(21, new ItemStack(Material.GOLDEN_APPLE, 1));
        chest.getBlockInventory().setItem(22, new ItemStack(Material.GOLDEN_APPLE, 1));
        chest.getBlockInventory().setItem(23, new ItemStack(Material.GOLDEN_APPLE, 1));
        chest.update();

        configuration.getStringList("drop-locations").forEach(s -> dropLocations.add(LocationUtils.str2loc(s)));
        final ConfigurationSection teamSection = plugin.getConfig().getConfigurationSection("teams");
        teamSection.getKeys(false).forEach(s -> {
            teams.add(new TeamImpl(s, teamSection.getString(s + ".name"),
                    LocationUtils.str2loc(teamSection.getString(s + ".chest")),
                    ChatColor.getByChar(String.valueOf(teamSection.getString(s + ".color"))),
                    (byte) teamSection.getInt(s + ".byte-color"),
                    LocationUtils.str2loc(teamSection.getString(s + ".spawn")),
                    LocationUtils.str2loc(teamSection.getString(s + ".crystal")),
                    new Region(LocationUtils.str2loc(teamSection.getString(s + ".build-pos1")),
                            LocationUtils.str2loc(teamSection.getString(s + ".build-pos2")))));
        });


    }


    public void loadChunks(Location location) {
        loadChunks(location.getChunk());
    }

    public void loadChunks(Location l1, Location l2) {
        LocationUtils.getCuboidLocations(l1, l2).forEach(this::loadChunks);
    }


    public void loadChunks(Chunk chunk) {
        Bukkit.getWorld("world").loadChunk(chunk);
    }


    public List<Location> getDropLocations() {
        return dropLocations;
    }

    public List<TeamImpl> getTeams() {
        return teams;
    }


    public long getAssaultTime() {
        return assaultTime;
    }

    public long getDeathmatchTime() {
        return deathmatchTime;
    }

    public int getMaxPlayersPerTeam() {
        return maxPlayersPerTeam;
    }

    public long getPvpTime() {
        return pvpTime;
    }


    public long getDayTime() {
        return dayTime;
    }


    @Override
    public int getMaxPlayerCount() {
        return maxPlayer;
    }

    @Override
    public int getMinPlayerToStart() {
        return minPlayerStart;
    }

    @Override
    public int getStartTimer() {
        return startTimer;
    }

    @Override
    public Location getSpawn() {
        return spawn;
    }

    @Override
    public boolean useWaitingRoom() {
        return true;
    }

    public boolean isPlayerTeamBase(Player player, Location location) {
        TeamImpl team = FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(player);
        return team != null && team.getRegion().isInArea(location);
    }

    public boolean isInATeamBase(Location location) {
        return FallenKingdom.INSTANCE().getTeamProperties().getTeams().stream().anyMatch(team -> team.getRegion().isInArea(location));
    }

    @Nullable
    @Override
    public TeamProperties<? extends Team> getTeamProperties() {
        return FallenKingdom.INSTANCE().getTeamProperties();
    }


}
