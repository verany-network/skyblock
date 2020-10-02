package net.verany.skyblock.commands;

import net.verany.Verany;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (!player.hasPermission("sb.fly")) {
            player.sendMessage(Verany.getPrefix("SkyBlock") + player.getKeyMessage("no_permission"));
            return false;
        }
        if (player.getAllowFlight()) {
            player.setFlying(false);
            player.setAllowFlight(false);
            player.sendMessage(Verany.getPrefix("SkyBlock") + player.getKeyMessage("sb_fly_mode_deacticated"));
        } else {
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage(Verany.getPrefix("SkyBlock") + player.getKeyMessage("sb_fly_mode_activated"));
        }

        return false;
    }

}
