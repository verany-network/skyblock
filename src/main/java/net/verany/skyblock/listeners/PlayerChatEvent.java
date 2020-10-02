package net.verany.skyblock.listeners;

import net.verany.Verany;
import net.verany.event.AbstractListener;
import net.verany.skyblock.module.SkyBlockModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatEvent extends AbstractListener<SkyBlockModule> {

    public PlayerChatEvent(SkyBlockModule module) {
        super(module);

        Verany.registerListener(module.getPlugin(), AsyncPlayerChatEvent.class, event -> {
            Player player = event.getPlayer();

            if (player.hasMetadata("search_player")) {
                event.setSend(false);

                if (event.getMessage().equalsIgnoreCase("exit")) {
                    player.removeMetadata("search_player");
                   // new SkyBlockInventory(player).setSearchInventory().load();
                    return;
                }
                if (event.getMessage().split(" ").length > 1) {
                    player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_enter_only_one_name");
                    return;
                }
                player.setMetadata("searched_player", event.getMessage());
                player.removeMetadata("search_player");
               // new SkyBlockInventory(player).setSearchInventory().load();
                player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_searched_player", event.getMessage());
                return;
            }
            if (player.hasMetadata("filter_player")) {
                event.setSend(false);

                if (event.getMessage().equalsIgnoreCase("exit")) {
                    player.removeMetadata("filter_player");
                   // new SkyBlockInventory(player).setSearchInventory().load();
                    return;
                }
                if (event.getMessage().split(" ").length > 1) {
                    player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_enter_only_one_name");
                    return;
                }
                player.setMetadata("filtered_player", event.getMessage());
                player.removeMetadata("filter_player");
               // new SkyBlockInventory(player).setIslandMember().load();
                player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_filtered_player", event.getMessage());
            }
        }, EventPriority.HIGHEST);
    }
}
