package net.verany.skyblock.inventories;

import net.verany.inventory.AbstractVeranyInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TradeInventory {

    private final Player first;
    private final Player second;
    private AbstractVeranyInventory inventory;

    public TradeInventory(Player first, Player second) {
        this.first = first;
        this.second = second;
    }

    public TradeInventory setItems(Player player) {
        inventory = Bukkit.createInventory(null,9*6, player.getKeyMessage("sb_trade_inventory", player.equals(first) ? second.getNameWithColor() : first.getNameWithColor()), event -> {

        });



        return this;
    }

    public void load() {
        first.openInventory(inventory);
        second.openInventory(inventory);
    }

}
