package net.verany.skyblock.inventories;

import net.verany.Verany;
import net.verany.inventory.AbstractVeranyInventory;
import net.verany.itembuilder.ItemBuilder;
import net.verany.skyblock.game.player.GamePlayer;
import net.verany.skyblock.game.player.kit.KitType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class KitsInventory {

    private final Player player;
    private AbstractVeranyInventory inventory;

    public KitsInventory(Player player) {
        this.player = player;
    }

    public KitsInventory setKits() {
        this.inventory = Bukkit.createInventory(player, 9 * 5, player.getKeyMessage("sb_kits_inv"), event -> {
            event.setCancelled(true);

            KitType kitType = KitType.getTypeByMaterial(event.getCurrentItem().getType());
            if (kitType != null) {
                if (player.hasMetadata(kitType.name().toLowerCase() + "_kit_cooldown")) {
                    long time = player.getMetadata(kitType.name().toLowerCase() + "_kit_cooldown").get(0).asLong();
                    if (time > System.currentTimeMillis()) {
                        player.closeInventory();
                        player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_" + kitType.name().toLowerCase() + "_cooldown_message", Verany.formatSeconds((int) ((time - System.currentTimeMillis()) / 1000)));
                        return;
                    }
                }
                player.getVeranyPlayer(GamePlayer.class).setKitItems(kitType);
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1.7F);
                player.setMetadata(kitType.name().toLowerCase() + "_kit_cooldown", (System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)));
            }

        });
        inventory.fillCycle(Verany.getPlaceholder(Material.LIGHT_BLUE_STAINED_GLASS_PANE));

        int[] slots = {10, 16, 22, 28, 34};
        for (int i = 0; i < KitType.values().length; i++) {
            KitType kitType = KitType.values()[i];
            inventory.setItem(slots[i], new ItemBuilder(kitType.getIcon()).setDisplayName(kitType.name()).build());
        }

        return this;
    }

    public void load() {
        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1.7F);
    }
}
