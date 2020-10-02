package net.verany.skyblock.commands;

import net.verany.Verany;
import net.verany.player.IPlayerInfo;
import net.verany.skyblock.SkyBlock;
import net.verany.skyblock.game.player.GamePlayer;
import net.verany.skyblock.game.player.settings.SkyBlockSettings;
import net.verany.skyblock.inventories.SkyBlockInventory;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkyBlockCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Player player = (Player) commandSender;
        GamePlayer gamePlayer = SkyBlock.getInstance().getGamePlayer(player);
        if (strings.length == 0) {
            new SkyBlockInventory(player).setMainPage().load();
        } else if (strings.length == 1) {
            if (strings[0].equalsIgnoreCase("teleport") || strings[0].equalsIgnoreCase("tp")) {
                if (gamePlayer.getIslandObject().getIslandLocation() == null) {
                    player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_no_island");
                    return false;
                }
                player.teleport(gamePlayer.getIslandObject().getIslandLocation());
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1.7F);
                player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_teleported_own_island");
            } else if (strings[0].equalsIgnoreCase("delete")) {
                player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_deleting_island");
                gamePlayer.getIslandObject().deleteIsland();
                player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_deleting_success");
            }/* else if(strings[0].equalsIgnoreCase("create")) {
                new SkyBlockInventory(player).setIslandTypes().load();
            }*/
        } else if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase("teleport") || strings[0].equalsIgnoreCase("tp")) {
                IPlayerInfo targetInfo = Verany.getPlayerInfo(strings[1]);
                GamePlayer target = SkyBlock.getInstance().getGamePlayer(targetInfo.getUniqueId());
                if (target.getIslandObject().getIslandLocation() == null) {
                    player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_no_island_target");
                    return false;
                }
                if (targetInfo.getSettingValue(SkyBlockSettings.OTHER_ISLAND_TELEPORT).equals(false)) {
                    player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_target_disabled_teleport");
                    return false;
                }
                player.teleport(target.getIslandObject().getIslandLocation());
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1.7F);
                player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_teleport_other_island", targetInfo.getNameWithColor());
            }
        }

        return false;
    }

}
