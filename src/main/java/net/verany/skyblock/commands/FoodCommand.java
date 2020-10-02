package net.verany.skyblock.commands;

import net.verany.Verany;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FoodCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        player.setFoodLevel(20);
        player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"),"sb_food_command");

        return false;
    }

}
