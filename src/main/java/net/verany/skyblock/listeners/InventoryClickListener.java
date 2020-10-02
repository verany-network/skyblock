package net.verany.skyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void handleClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(player.getOpenInventory().getTitle().equals("§bViewArmor")) {
            event.setCancelled(true);
        } else if (player.hasMetadata("invsee")) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void handleClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (player.hasMetadata("invsee"))
            player.removeMetadata("invsee");

    }
}
