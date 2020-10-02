package net.verany.skyblock.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.verany.Verany;
import net.verany.message.AbstractComponentBuilder;
import net.verany.skyblock.SkyBlock;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TpAcceptCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (strings.length == 0) {
            List<Player> requests = getTpaRequests(player);
            if (requests.size() == 1) {
                teleport(player, requests.get(0));
                return false;
            }
            player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "tpa_requests", requests.size());
            for (Player tpaRequest : requests) {
                player.sendMessage(new AbstractComponentBuilder(player.getKeyMessage("tpa_requests_format", tpaRequest.getNameWithColor())) {
                    @Override
                    public void onCreate() {
                        setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + tpaRequest.getName()));
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
            teleport(player, target);
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

    private void teleport(Player player, Player target) {
        long time = target.getMetadata("tpaRequest_" + player.getUniqueId().toString()).get(0).asLong();
        if (time < System.currentTimeMillis()) {
            player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_time_expired");
            return;
        }
        target.removeMetadata("tpaRequest_" + player.getUniqueId().toString());
        if (target.hasPermission("skyblock.cooldown.bypass")) {
            target.teleport(player);
            target.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            return;
        }
        target.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_tpa_teleport");
        target.setMetadata("tpaCount", 3);
        target.setMetadata("tpaTask", Bukkit.getScheduler().scheduleSyncRepeatingTask(SkyBlock.getInstance(), () -> {
            int count = target.getMetadata("tpaCount").get(0).asInt();
            target.sendActionBar(player.getKeyMessage("sb_tpa_count", count));
            count--;
            target.setMetadata("tpaCount", count);
            if (count == -1) {
                target.teleport(player);
                target.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                Bukkit.getScheduler().cancelTask(target.getMetadata("tpaTask").get(0).asInt());
                player.removeMetadata("tpaTask");
            }
        }, 0, 20));
    }
}
