package net.verany.skyblock.game.settings.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.enumhelper.VeranyEnum;
import org.bukkit.Material;

@AllArgsConstructor
@Getter
public enum IslandRank implements VeranyEnum {

    VISITOR(Material.LEATHER_HELMET) ,
    HELPER(Material.IRON_HELMET),
    MEMBER(Material.GOLDEN_HELMET),
    CREATOR(Material.DIAMOND_HELMET);

    private final Material material;

}
