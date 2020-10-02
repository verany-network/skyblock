package net.verany.skyblock.game.island;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.cuboid.Cuboid;
import net.verany.interfaces.IDefault;
import net.verany.location.VeranyLocation;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.*;

import java.util.UUID;

public interface IIslandObject extends IDefault<UUID> {

    void createIsland(IslandType islandType);

    void deleteIsland();

    void setIslandLocation(VeranyLocation location);

    VeranyLocation getIslandLocation();

    int getIslandSize();

    Cuboid getCuboid();

    @AllArgsConstructor
    @Getter
    enum IslandType {
        OAK_ISLAND(Material.OAK_SAPLING, Sheep.class, Biome.PLAINS),
        BIRCH_ISLAND(Material.BIRCH_SAPLING, Cow.class, Biome.BIRCH_FOREST),
        SPRUCE_ISLAND(Material.SPRUCE_SAPLING, Fox.class, Biome.TAIGA),
        DARK_OAK_ISLAND(Material.DARK_OAK_SAPLING, Rabbit.class, Biome.DARK_FOREST),
        JUNGLE_ISLAND(Material.JUNGLE_SAPLING, Parrot.class, Biome.JUNGLE);

        private final Material material;
        private final Class<? extends Entity> islandEntity;
        private final Biome defaultBiome;

        public static IslandType getTypeByMaterial(Material material) {
            for (IslandType value : values())
                if(value.getMaterial().equals(material))
                    return value;
            return null;
        }
    }
}
