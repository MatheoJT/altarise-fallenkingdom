package net.altarise.fk.game;

import net.altarise.api.AltariseAPI;
import net.altarise.api.minecraft.scoreboard.Scoreboard;
import net.altarise.api.utils.ColorUtils;
import net.altarise.api.utils.LocationUtils;
import net.altarise.api.utils.PlayerUtils;
import net.altarise.fk.FallenKingdom;
import net.altarise.fk.game.res.PlayerSkeleton;
import net.altarise.fk.game.res.RandomDrop;
import net.altarise.fk.impl.PlayingScoreboardImpl;
import net.altarise.fk.impl.SpecScoreboardImpl;
import net.altarise.fk.impl.TeamImpl;
import net.altarise.gameapi.GameAPI;
import net.altarise.gameapi.engine.coherence.GameCoherence;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.material.Step;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GameManager implements GameCoherence {


    private final RandomDrop randomDrop;
    private final GameState gameState;
    public final HashMap<Skeleton, PlayerSkeleton> playerSkeleton = new HashMap<>();
    public final Map<EnderCrystal, TeamImpl> crystals = new HashMap<>();
    public final List<Player> players = new ArrayList<>();

    public GameManager() {
        randomDrop = new RandomDrop();
        gameState = new GameState();

    }


    public int day = 0;

    public void updateEnding(TeamImpl team) {

        if (FallenKingdom.INSTANCE().getGameAPI().getGameState().equals(net.altarise.gameapi.basic.GameState.ENDING))
            return;

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("   §e♛ §fL'équipe " + team.getChatColor() + ChatColor.BOLD + team.getName() + " §fremporte la partie !");
        Bukkit.broadcastMessage(""); Bukkit.broadcastMessage("         §b◢ §f§lStatistiques");
        Bukkit.broadcastMessage("         §b» §6" + team.getHearthKill() + "§7 Coeur(s) détruit(s)");
        Bukkit.broadcastMessage("         §b» §6" + team.getKills() + "§7 Kill(s)"); Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("");


        TextComponent space1 = new TextComponent("    ");


        TextComponent REPLAY = new TextComponent("REJOUER"); REPLAY.setBold(true);
        REPLAY.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        REPLAY.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Cliquez §epour rejouer !").create()));
        REPLAY.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/re"));

        TextComponent space2 = new TextComponent("         ");

        TextComponent LEAVE = new TextComponent("QUITTER"); LEAVE.setBold(true);
        LEAVE.setColor(net.md_5.bungee.api.ChatColor.RED);
        LEAVE.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Cliquez §epour retourner au lobby !").create()));
        LEAVE.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lobby"));

        space1.addExtra(REPLAY);
        space1.addExtra(space2);
        space1.addExtra(LEAVE);


        Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(space1));

        Bukkit.broadcastMessage("");

        FallenKingdom.INSTANCE().getScheduler().stop();
        FallenKingdom.INSTANCE().getGameAPI().getGameManager().setTeamWinner(team.getPlayers(), team.getChatColor() + StringUtils.capitalize(team.getName()));
        FallenKingdom.INSTANCE().getGameAPI().getGameManager().setWinners(team.getPlayers());


    }

    public void setLose(Player damager, TeamImpl team) {

        if (FallenKingdom.INSTANCE().getGameAPI().getGameState().equals(net.altarise.gameapi.basic.GameState.ENDING))
            return;

        if(team.getPlayers().size() >= 1) {
            team.getPlayers().forEach(player -> {
            FallenKingdom.INSTANCE().getGamePlayer(player.getUniqueId()).setLose(true);
            GameAPI.get().getSpectatorManager().addSpectator(player);
        });
    }


        if(FallenKingdom.INSTANCE().getGameCoherence().day >= FallenKingdom.INSTANCE().getGameProperties().getPvpTime()) {
            Bukkit.getWorld("world").playSound(team.getLocation(), Sound.FUSE, 5F, 5F);
            Bukkit.getWorld("world").playSound(team.getLocation(), Sound.FUSE, 5F, 5F);
            Bukkit.getWorld("world").playSound(team.getLocation(), Sound.FUSE, 5F, 5F);
            new BukkitRunnable() {
                int i = 60;

                @Override
                public void run() {
                    team.getCrystal().updateName("§cExplosion §fdans §c" + i + " §fsecondes");

                    if (i == 0) {
                        Bukkit.getWorld("world").playSound(team.getLocation(), Sound.EXPLODE, 5F, 5F);
                        Bukkit.getWorld("world").createExplosion(team.getLocation(), 25F);
                        List<Location> aroundLocations = LocationUtils.getAroundLocations(team.getLocation(), 10);
                        team.getCrystal().getCrystal().remove();
                        Random random = new Random();

                        for (Location loc : aroundLocations) {
                            if (random.nextInt(100) < 20) {
                                loc.getWorld().playEffect(loc, Effect.FLAME, 1);
                            }
                        }

                        this.cancel();
                    }

                    i--;
                }
            }.runTaskTimer(FallenKingdom.INSTANCE(), 20, 20);
        }
        if (damager != null) {
            Bukkit.broadcastMessage(""); Bukkit.broadcastMessage("                      §7_ §c❢ §7_");
            Bukkit.broadcastMessage("  §fL'équipe " + team.getChatColor() + team.getName() + "§f a été §céliminer §fpar l'équipe " + FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(damager).getChatColor() + FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(damager).getName());
            Bukkit.broadcastMessage(""); FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(damager).addHKill();
            FallenKingdom.INSTANCE().getGameProperties().getTeams().remove(team);

            if (FallenKingdom.INSTANCE().getGameProperties().getTeams().size() <= 1)
                updateEnding(FallenKingdom.INSTANCE().getGameProperties().getTeams().get(0));

        } else {

            Bukkit.broadcastMessage(""); Bukkit.broadcastMessage("                      §7_ §c❢ §7_");
            Bukkit.broadcastMessage("     §fL'équipe " + team.getChatColor() + team.getName() + "§f a été §céliminer");
            Bukkit.broadcastMessage(""); FallenKingdom.INSTANCE().getGameProperties().getTeams().remove(team);
            if (FallenKingdom.INSTANCE().getGameProperties().getTeams().size() <= 1)
                updateEnding(FallenKingdom.INSTANCE().getGameProperties().getTeams().get(0));


        }

    }

    private final Map<String, ItemStack> cachedBanners = new HashMap<>();


    public void updateChest(TeamImpl team) {
        Chest teamChest = (Chest) Bukkit.getWorld("world").getBlockAt(team.getChestLocation()).getState();
        Inventory inventory = teamChest.getBlockInventory();

        int[] breadSlots = {3, 5, 21, 23};
        int[] pickaxeSlots = {4, 22, 12, 14};
        int[] stoneSlots = {2, 6, 11, 15, 20, 24};

        for (int slot : breadSlots) {
            inventory.setItem(slot, new ItemStack(Material.BREAD, 5));
        }

        for (int slot : pickaxeSlots) {
            inventory.setItem(slot, new ItemStack(Material.WOOD_PICKAXE, 1));
        }

        inventory.setItem(13, new ItemStack(Material.GOLDEN_APPLE, 1));

        for (int slot : stoneSlots) {
            inventory.setItem(slot, new Step(Material.STONE).toItemStack(1));
        }

        teamChest.update();
    }

    @Override
    public void onGameStart() {

        Scheduler scheduler = FallenKingdom.INSTANCE().getScheduler();
        scheduler.runTaskTimer(FallenKingdom.INSTANCE(), 0L, 20L);
        scheduler.runCooldown(gameState.valueOf("pvp"), FallenKingdom.INSTANCE().getGameProperties().getDayTime() * FallenKingdom.INSTANCE().getGameProperties().getPvpTime());
        gameState.updateState();


        FallenKingdom.INSTANCE().getGamePlayers().forEach(player -> {
            PlayerUtils.cleanPlayer(player, GameMode.SURVIVAL);
            player.teleport(FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(player).getLocation());

            ItemStack banner = cachedBanners.computeIfAbsent(player.getName(), p -> {
                ItemStack itemStack = new ItemStack(Material.BANNER);
                BannerMeta meta = (BannerMeta) itemStack.getItemMeta();
                meta.setBaseColor(ColorUtils.colorToDyeColor(FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(player).getColor()));
                itemStack.setItemMeta(meta);
                return itemStack;
            });
            player.getInventory().setHelmet(banner);
            AltariseAPI.get().getMinecraftProvider().getScoreboardManager().setPlayerScoreboard(player, getPlayingScoreboard(player));
        });

        scheduleStartingTeamTask();
    }

    private void scheduleStartingTeamTask() {
        new BukkitRunnable(){
            @Override
            public void run() {
                for (TeamImpl fkTeam : FallenKingdom.INSTANCE().getGameProperties().getTeams()) {
                    if (fkTeam.getPlayers().size() == 0) {
                        setLose(null, fkTeam);
                    } else {
                        fkTeam.getCrystal().spawn();
                        crystals.put(fkTeam.getCrystal().getCrystal(), fkTeam);
                        updateChest(fkTeam);
                    }
                }
            }

        }.runTaskLater(FallenKingdom.INSTANCE(), 20);

    }


    public GameState getGameState() {
        return gameState;
    }

    @Override
    public Scoreboard getPlayingScoreboard(Player player) {
        return new PlayingScoreboardImpl(player);
    }

    @Override
    public Scoreboard getSpectatorScoreboard(Player player) {
        return new SpecScoreboardImpl();
    }


    public RandomDrop getRandomDrop() {
        return randomDrop;
    }
}
