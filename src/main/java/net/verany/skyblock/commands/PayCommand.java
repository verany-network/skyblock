package net.verany.skyblock.commands;

import net.verany.Verany;
import net.verany.player.IPlayerInfo;
import net.verany.skyblock.SkyBlock;
import net.verany.skyblock.game.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (strings.length == 2) {
            IPlayerInfo targetInfo = Verany.getPlayerInfo(strings[0]);
            if (targetInfo == null) {
                player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "player_not_found");
                return false;
            }
            GamePlayer target = SkyBlock.getInstance().getGamePlayer(targetInfo.getUniqueId());
            try {
                double amount = Integer.parseInt(strings[1]);
                if (SkyBlock.getInstance().getGamePlayer(player).getGems() == 0) {
                    player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "not_enough_gems");
                    return false;
                }
                if ((SkyBlock.getInstance().getGamePlayer(player).getGems() - amount) < 0) {
                    amount = SkyBlock.getInstance().getGamePlayer(player).getGems();
                    SkyBlock.getInstance().getGamePlayer(player).setGems(0);
                } else
                    SkyBlock.getInstance().getGamePlayer(player).setGems(SkyBlock.getInstance().getGamePlayer(player).getGems() - amount);
                target.setGems(target.getGems() + amount);
                player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "gems_removed", amount);
                if (Bukkit.getPlayer(targetInfo.getUniqueId()) != null)
                    Bukkit.getPlayer(targetInfo.getUniqueId()).sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "gems_added", amount);
            } catch (NumberFormatException e) {
                player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "enter_real_number");
            }
        } else {
            player.sendMessage("§b/pay <player> <gems>");
        }


        return false;
    }

}
