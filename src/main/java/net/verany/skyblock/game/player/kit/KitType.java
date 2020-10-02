package net.verany.skyblock.game.player.kit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.itembuilder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
public enum KitType {

    STARTER(Material.STONE_SWORD, new ItemStack[]{new ItemBuilder(Material.STONE_SWORD).setDisplayName("§cStarter Kit").build()}, new ItemStack[]{new ItemBuilder(Material.LEATHER_BOOTS).build()}),
    VIP(Material.IRON_HELMET, new ItemStack[]{new ItemBuilder(Material.SPONGE).setDisplayName("§cVIP_KIT").build()}, new ItemStack[]{}),
    MVP(Material.GOLDEN_HELMET, new ItemStack[]{new ItemBuilder(Material.SPONGE).setDisplayName("§cVIP_KIT").build()}, new ItemStack[]{}),
    MVP_PLUS(Material.DIAMOND_HELMET, new ItemStack[]{new ItemBuilder(Material.SPONGE).setDisplayName("§cVIP_KIT").build()}, new ItemStack[]{}),
    FOOD(Material.APPLE, new ItemStack[]{new ItemBuilder(Material.SPONGE).setDisplayName("§cVIP_KIT").build()}, new ItemStack[]{});

    private final Material icon;
    private final ItemStack[] inventoryItems, armorItems;

    public static KitType getTypeByMaterial(Material material) {
        for (KitType value : values())
            if (value.getIcon().equals(material))
                return value;
        return null;
    }

}
