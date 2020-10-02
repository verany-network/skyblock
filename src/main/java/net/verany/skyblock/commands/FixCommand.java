package net.verany.skyblock.commands;

import net.verany.Verany;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FixCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if(!player.hasPermission("skyblock.fix")) {
            player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "no_permission");
            return false;
        }
        player.getItemInHand().setDurability((short) 0);
        player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_fixed_item");
        return true;
    }

}
