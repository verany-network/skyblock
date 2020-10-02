package net.verany.skyblock.game.cobbleGenerator;

import org.bukkit.Material;

public class CobbleMaterial {

    private final Material material;
    private final RarityType rarityType;

    public CobbleMaterial(Material material, RarityType rarityType) {
        this.material = material;
        this.rarityType = rarityType;
    }

    public Material getMaterial() {
        return material;
    }

    public RarityType getRarityType() {
        return rarityType;
    }
}
