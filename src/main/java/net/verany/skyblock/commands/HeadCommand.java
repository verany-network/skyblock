package net.verany.skyblock.commands;

import net.verany.Verany;
import net.verany.skin.AbstractSkinData;
import net.verany.skullchanger.SkullBuilder;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HeadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;

        if (!player.hasPermission("skyblock.head")) {
            player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "no_permission");
            return false;
        }
        if (strings.length == 0) {
            player.sendMessage(Verany.getPrefix("SkyBlock") + "§7/head <Name>");
        } else if (strings.length == 1) {
            String name = strings[0];
            AbstractSkinData skinData = Verany.getPlayerData(name);
            ItemStack skull = new SkullBuilder(skinData).setDisplayName("§8◗§7◗ §7Kopf von §b" + name).addLoreArray(" ", "§8• §7Erstellt auf dem §bVerany.net §7SkyBlock Server§8.").build();
            player.getInventory().addItem(skull);
            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
        } else {

            return false;

        }


        return false;
    }
}
