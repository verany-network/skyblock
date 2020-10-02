package net.verany.skyblock.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.verany.Verany;
import net.verany.message.AbstractComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TpaDenyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (strings.length == 0) {
            List<Player> requests = getTpaRequests(player);
            if (requests.size() == 1) {
                denyRequest(player, requests.get(0));
                return false;
            }
            player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "tpa_requests", requests.size());
            for (Player tpaRequest : requests) {
                player.sendMessage(new AbstractComponentBuilder(player.getKeyMessage("tpa_requests_format", tpaRequest.getNameWithColor())) {
                    @Override
                    public void onCreate() {
                        setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny " + tpaRequest.getName()));
                        setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(player.getKeyMessage("sb_tpaccept_hover")).create()));
                    }
                });
            }
            player.sendMessage(" ");
        } else if (strings.length == 1) {
            Player target = Bukkit.getPlayer(strings[0]);
            if (target == null) {
                player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "player_not_found");
                return false;
            }
            denyRequest(player, target);
        }

        return false;
    }

    private List<Player> getTpaRequests(Player player) {
        List<Player> toReturn = new ArrayList<>();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            if (onlinePlayer.hasMetadata("tpaRequest_" + player.getUniqueId().toString()))
                toReturn.add(onlinePlayer);
        return toReturn;
    }

    private void denyRequest(Player player, Player target) {
        target.removeMetadata("tpaRequest_" + player.getUniqueId());
        player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "tpa_denied_successfully_sender", target.getNameWithColor());
        target.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "tpa_denies_successfully_target", player.getNameWithColor());
    }

}
