package net.verany.skyblock.listeners;

import net.verany.Verany;
import net.verany.event.AbstractListener;
import net.verany.skyblock.SkyBlock;
import net.verany.skyblock.module.SkyBlockModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitEventListener extends AbstractListener<SkyBlockModule> {

    public PlayerQuitEventListener(SkyBlockModule module) {
        super(module);

        Verany.registerListener(module.getPlugin(), PlayerQuitEvent.class, event -> {
            Player player = event.getPlayer();

            event.setQuitMessage(null);

            SkyBlock.getInstance().getGamePlayer(player).update(module);

            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "player_quit", player.getNameWithColor()));
        });
    }

}
