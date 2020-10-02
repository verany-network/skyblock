package net.verany.skyblock.commands;

import net.verany.Verany;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;

public class FixallCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof ConsoleCommandSender) {
            System.out.println("Dieser Command ist nur für Spieler");
            return false;
        }

        Player player = (Player) commandSender;

        if (!player.hasPermission("skyblock.fixall")) {
            player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "no_permission");
            return false;
        }
        if (player.hasMetadata("fixCooldown")) {
            long updated = player.getMetadata("fixCooldown").get(0).asLong();
            if (updated > System.currentTimeMillis()) {
                player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_fix_cooldown", Verany.formatSeconds((int) ((updated - System.currentTimeMillis()) / 1000)));
                return false;
            }
        }
        if (!player.hasPermission("skyblock.bypass.cooldown"))
            player.setMetadata("fixCooldown", System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30));
        fixall(player);
        player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_fixed_all_items");
        return true;
    }

    public static void fixall(Player player) {
        ItemStack[] items = player.getInventory().getContents();
        ItemStack[] armor = player.getInventory().getArmorContents();

        for (ItemStack item : items)
            if (item != null)
                item.setDurability((short) 0);

        for (ItemStack item : armor)
            if (item != null)
                item.setDurability((short) 0);
    }
}
