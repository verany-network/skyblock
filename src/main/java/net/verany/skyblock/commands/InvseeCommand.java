package net.verany.skyblock.commands;

import net.verany.Verany;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvseeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (!player.hasPermission("skyblock.invsee")) {
            player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "no_permission");
            return false;
        }
        if (strings.length == 1) {
            Player target = Bukkit.getPlayer(strings[0]);
            if(target == null) {
                player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "player_not_found");
                return false;
            }
            player.openInventory(target.getInventory());
            player.setMetadata("invsee", target);
        }

        return false;
    }

}
