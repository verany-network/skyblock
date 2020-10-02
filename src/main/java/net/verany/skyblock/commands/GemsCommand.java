package net.verany.skyblock.commands;

import net.verany.Verany;
import net.verany.player.IPlayerInfo;
import net.verany.skyblock.SkyBlock;
import net.verany.skyblock.game.player.GamePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GemsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (strings.length == 0) {
            player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_gems_info", Verany.format(SkyBlock.getInstance().getGamePlayer(player).getGems()));
        } else if (strings.length == 3) {
            if (player.hasPermission("skyblock.gems.admin")) {
                if (strings[0].equalsIgnoreCase("add")) {
                    IPlayerInfo targetInfo = Verany.getPlayerInfo(strings[1]);
                    if (targetInfo == null) {
                        player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "player_not_found");
                        return false;
                    }
                    GamePlayer target = SkyBlock.getInstance().getGamePlayer(targetInfo.getUniqueId());
                    target.setGems(target.getGems() + Integer.parseInt(strings[2]));
                    player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_added_gems_to_other", targetInfo.getNameWithColor(), strings[2]);
                } else if (strings[0].equalsIgnoreCase("set")) {
                    IPlayerInfo targetInfo = Verany.getPlayerInfo(strings[1]);
                    if (targetInfo == null) {
                        player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "player_not_found");
                        return false;
                    }
                    GamePlayer target = SkyBlock.getInstance().getGamePlayer(targetInfo.getUniqueId());
                    target.setGems(Integer.parseInt(strings[2]));
                    player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_set_gems_from_other", targetInfo.getNameWithColor(), strings[2]);
                } else if (strings[0].equalsIgnoreCase("remove")) {
                    IPlayerInfo targetInfo = Verany.getPlayerInfo(strings[1]);
                    if (targetInfo == null) {
                        player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "player_not_found");
                        return false;
                    }
                    GamePlayer target = SkyBlock.getInstance().getGamePlayer(targetInfo.getUniqueId());
                    target.setGems(target.getGems() - Integer.parseInt(strings[2]));
                    player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_removed_gems_from_other", targetInfo.getNameWithColor(), strings[2]);
                }
            }
            else {
                player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"),"no_permission");
                return false;
            }
        }

        return false;
    }

}
