package net.verany.skyblock.listeners;

import net.verany.Verany;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void handleDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        Player killer = player.getKiller();

        event.setDeathMessage(null);

        if (killer == null) {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "player_died", player.getNameWithColor()));
        } else {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "player_died", player.getNameWithColor(), killer.getNameWithColor()));
        }
    }

}
