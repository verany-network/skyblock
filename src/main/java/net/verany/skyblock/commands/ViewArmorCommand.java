package net.verany.skyblock.commands;

import net.verany.Verany;
import net.verany.skyblock.inventories.SkyBlockInventory;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class ViewArmorCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof ConsoleCommandSender) {
            System.out.println("Dieser Command ist nur für Spieler");
            return false;
        }
        Player player = (Player) commandSender;

        if (player.hasPermission("skyblock.viewarmor")) {
            if (strings.length == 1) {
                Player target = Bukkit.getPlayer(strings[0]);
                if (target == null) {

                    return false;
                }
                new SkyBlockInventory(player).setViewArmor(target).load();
            }
        }

        return false;
    }

}
