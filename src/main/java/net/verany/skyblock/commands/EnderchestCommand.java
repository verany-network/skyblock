package net.verany.skyblock.commands;

import net.verany.Verany;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnderchestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (!player.hasPermission("skyblock.enderchest")) {
            player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "no_permission");
            return false;
        }
        player.openInventory(player.getEnderChest());

        return false;
    }

}
