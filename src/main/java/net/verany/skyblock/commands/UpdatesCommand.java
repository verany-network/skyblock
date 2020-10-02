package net.verany.skyblock.commands;

import net.verany.Verany;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UpdatesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        String[] updates = player.getKeyMessageArray("sb_updates", "~", Verany.getPrefix("SkyBlock"));
        player.sendMessage(updates);

        return false;
    }
}
