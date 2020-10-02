package net.verany.skyblock.commands;

import com.google.common.collect.ImmutableList;
import net.verany.Verany;
import net.verany.skyblock.game.player.GamePlayer;
import net.verany.skyblock.game.player.kit.KitType;
import net.verany.skyblock.inventories.KitsInventory;
import org.bukkit.Sound;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class KitsCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof ConsoleCommandSender) {
            System.out.println("Nur für Spieler");
            return false;
        }

        Player player = (Player) commandSender;

        if (strings.length == 0) {
            player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_loading_kits");
            new KitsInventory(player).setKits().load();
        } else if (strings.length == 1) {
            try {
                KitType kitType = KitType.valueOf(strings[0].toUpperCase());
                if (player.hasMetadata(kitType.name().toLowerCase() + "_kit_cooldown")) {
                    long time = player.getMetadata(kitType.name().toLowerCase() + "_kit_cooldown").get(0).asLong();
                    if (time > System.currentTimeMillis()) {
                        player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_" + kitType.name().toLowerCase() + "_cooldown_message", Verany.formatSeconds((int) ((time - System.currentTimeMillis()) / 1000)));
                        return false;
                    }
                }
                player.getVeranyPlayer(GamePlayer.class).setKitItems(kitType);
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1.7F);
                player.setMetadata(kitType.name().toLowerCase() + "_kit_cooldown", (System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)));
            } catch (Exception e) {
                player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_kit_not_found", strings[0]);
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> types = new ArrayList<>();
        for (KitType value : KitType.values())
            types.add(value.name().toLowerCase());

        if (strings.length == 1)
            return StringUtil.copyPartialMatches(strings[0], types, new ArrayList<>(types.size()));
        return ImmutableList.of();
    }
}
