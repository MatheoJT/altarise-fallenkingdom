package net.altarise.fk.game;

import net.altarise.api.utils.title.ActionBar;
import net.altarise.fk.FallenKingdom;
import net.altarise.fk.game.res.Laser;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PingCommand implements CommandExecutor {

    private final List<Player> cooldown = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) return false;
        if (FallenKingdom.INSTANCE().getGameCoherence().getGameState().isState(0)) return false;
        if (!FallenKingdom.INSTANCE().getGamePlayers().contains((Player) commandSender)) return false;

        Player sender = (Player) commandSender;

        if (!cooldown.contains(sender)) {
            for (Player player : FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(sender).getPlayers()) {
                player.playSound(player.getLocation(), Sound.FIREWORK_BLAST, 1F, 1F);
                new ActionBar("§6❖ Un allié a besoin d'aide !").sendToPlayer(player);
            }

            new Laser(sender.getLocation(), new ArrayList<>(FallenKingdom.INSTANCE().getTeamProperties().getTeamPlayer(sender).getPlayers()), 10);
            cooldown.add(sender);
            Bukkit.getScheduler().runTaskLater(FallenKingdom.INSTANCE(), () -> cooldown.remove(sender), 20);
        } else {
            new ActionBar("§c❖ Vous devez attendre 1 seconde avant de pouvoir utiliser cette commande").sendToPlayer(sender);
        }
        return false;
    }
}
