package net.verany.skyblock.commands;

import net.verany.Verany;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;

        if (!player.hasPermission("command.clearlag")) {
            player.sendMessage(Verany.getPrefix("BuildServer") + "§cKeine Rechte " );
            return false;
        } else if (strings.length == 0) {
            player.setHealth(20);
            player.setHealthScale(20);
            player.sendMessage(Verany.getPrefix("SkyBlock") + player.getKeyMessage("sb_heal_confirmation"));

        } else if (strings.length == 1) {
            Player target = Bukkit.getPlayer(strings[0]);
            if (target != null) {
                target.setHealth(20);
                target.setHealthScale(20);
                target.sendMessage(Verany.getPrefix("SkyBlock") + player.getKeyMessage("sb_heal_confirmation"));
                player.sendMessage(Verany.getPrefix("SkyBlock") + player.getKeyMessage("sb_healed_other_player", player.getNameWithColor()));
            } else
                player.sendMessage(Verany.getPrefix("SkyBlock") + player.getKeyMessage("sb_player_not_online"));
        }
        return false;
    }

}

