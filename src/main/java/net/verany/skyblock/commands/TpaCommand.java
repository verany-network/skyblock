package net.verany.skyblock.commands;

import net.verany.Verany;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class TpaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (strings.length == 1) {
            Player target = Bukkit.getPlayer(strings[0]);
            if (target == null) {
                player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "player_not_found");
                return false;
            }
            if (target.equals(player)) {
                player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "tpa_cant_sent_yourself");
                return false;
            }
            if (player.hasMetadata("tpaRequest_" + target.getUniqueId().toString())) {
                long time = player.getMetadata("tpaRequest_" + target.getUniqueId().toString()).get(0).asLong();
                if (time < System.currentTimeMillis())
                    player.removeMetadata("tpaRequest_" + target.getUniqueId().toString());
                else {
                    player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "tpa_already_sent");
                    return false;
                }
            }
            player.setMetadata("tpaRequest_" + target.getUniqueId().toString(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30));
            player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "tpa_sent", target.getNameWithColor());
            target.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "tpa_get", player.getNameWithColor());
        }

        return false;
    }

}
