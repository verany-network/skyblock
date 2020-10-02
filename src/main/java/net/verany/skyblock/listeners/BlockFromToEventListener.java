package net.verany.skyblock.listeners;

import com.google.common.collect.Lists;
import net.verany.Verany;
import net.verany.skyblock.SkyBlock;
import net.verany.skyblock.game.cobbleGenerator.CobbleMaterial;
import net.verany.skyblock.game.cobbleGenerator.RarityType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockFromToEventListener implements Listener {

    private final BlockFace[] faces = {BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
    private final List<CobbleMaterial> blocks = Lists.newArrayList(
            new CobbleMaterial(Material.COAL_ORE, RarityType.NORMAL),
            new CobbleMaterial(Material.DIAMOND_ORE, RarityType.LEGENDARY),
            new CobbleMaterial(Material.IRON_ORE, RarityType.RARE),
            new CobbleMaterial(Material.GOLD_ORE, RarityType.RARE),
            new CobbleMaterial(Material.LAPIS_ORE, RarityType.RARE),
            new CobbleMaterial(Material.REDSTONE_ORE, RarityType.EXCLUSIVE),
            new CobbleMaterial(Material.SAND, RarityType.NORMAL),
            new CobbleMaterial(Material.GRAVEL, RarityType.NORMAL),
            new CobbleMaterial(Material.SANDSTONE, RarityType.RARE),
            new CobbleMaterial(Material.OAK_LOG, RarityType.NORMAL)

    );

    @EventHandler
    public void cobbleGeneration(BlockFromToEvent event) {
        Material block = event.getBlock().getType();
        if ((block.equals(Material.LAVA) || block.equals(Material.WATER)) && generatesCobble(event.getToBlock())) {
            int random = Verany.getRandomNumberBetween(0, 100);
            if (random < 30) {
                Material newBlock = getMaterial(RarityType.getRarityTypeByPercentage(new Random().nextInt(99))).getMaterial();
                Bukkit.getScheduler().runTaskLater(SkyBlock.getInstance(), () -> event.getToBlock().setType(newBlock), 1);
            }
        }
    }

    private CobbleMaterial getMaterial(RarityType rarityType) {
        List<CobbleMaterial> possibleItems = new ArrayList<>();
        for (CobbleMaterial block : blocks)
            if (block.getRarityType().equals(rarityType))
                possibleItems.add(block);
        return possibleItems.get(new Random().nextInt(possibleItems.size()));
    }

    private boolean generatesCobble(Block block) {
        BlockFace[] arrayOfBlockFace;
        int j = (arrayOfBlockFace = this.faces).length;
        for (int i = 0; i < j; i++) {
            BlockFace face = arrayOfBlockFace[i];
            Block r = block.getRelative(face, 1);
            if ((r.getType().equals(Material.WATER)) || (r.getType().equals(Material.LAVA)))
                return true;
        }
        return false;
    }
}
