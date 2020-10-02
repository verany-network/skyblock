package net.verany.skyblock.game.island;

import net.verany.Verany;
import net.verany.cuboid.Cuboid;
import net.verany.location.VeranyLocation;
import net.verany.module.AbstractVeranyModule;
import net.verany.permission.PermissionGroup;
import net.verany.skyblock.SkyBlock;
import net.verany.skyblock.game.island.chunk.PacketWorldChunk;
import net.verany.world.IWorldObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class IslandObject implements IIslandObject {

    private UUID uuid;
    private Player player;

    private VeranyLocation islandLocation;
    private Cuboid islandCuboid;

    @Override
    public void load(AbstractVeranyModule abstractVeranyModule, UUID uuid) {
        this.uuid = uuid;

        if (Bukkit.getPlayer(uuid) != null)
            player = Bukkit.getPlayer(uuid);

        String databaseLocation = Verany.getObject(abstractVeranyModule, uuid, "skyBlockPlayer", "islandLocation", String.class);
        if (!databaseLocation.equals("a"))
            islandLocation = VeranyLocation.fromString(databaseLocation);

        loadCuboid();
    }

    private void loadCuboid() {
        if (getIslandLocation() == null) {
            islandCuboid = null;
        } else {
            Location firstLocation = new Location(Bukkit.getWorld("island"), islandLocation.getX() + (getIslandSize() / 2F), 0, islandLocation.getZ() + (getIslandSize() / 2F));
            Location secondLocation = new Location(Bukkit.getWorld("island"), islandLocation.getX() - (getIslandSize() / 2F), 256, islandLocation.getZ() - (getIslandSize() / 2F));
            islandCuboid = new Cuboid(firstLocation, secondLocation);
        }
    }

    @Override
    public void update(AbstractVeranyModule abstractVeranyModule) {
        Verany.update(abstractVeranyModule, uuid, "skyBlockPlayer", "islandLocation", (islandLocation == null ? "a" : islandLocation.toString()));
    }

    public void createIsland(IslandType islandType) {
        player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_creating_island");

        Location location = SkyBlock.getInstance().getNextIsland().toBukkitLocation();
        location.getWorld().refreshChunk(location.getBlockX(), location.getBlockZ());
        location.getWorld().loadChunk(location.getChunk());
        player.setMetadata("newIsland", location);

        Block barrier = location.getWorld().getBlockAt(location.clone().add(0, 0, 40));
        barrier.setType(Material.BARRIER);

        player.teleport(location.clone().add(0.5, 1.4, 40.5));
        Location playerLocation = player.getLocation();
        playerLocation.setPitch(0);
        playerLocation.setYaw(180);
        player.teleport(playerLocation);

        player.setMetadata("freeze", true);

        Verany.WORLD_OBJECT.loadSchematics(islandType.name(), player);
        Verany.WORLD_OBJECT.pasteSchematic(SkyBlock.getInstance(), player, location, null, IWorldObject.PasteType.STEP, false, (aFloat, blocks) -> {
            String progress = Verany.getProgressBar(Math.round(aFloat), 100, 10, '▇', ChatColor.GREEN, ChatColor.RED);
            player.sendTitle(player.getKeyMessage("sb_loading_title"), player.getKeyMessage("sb_loading_subtitle", progress, Verany.round(aFloat)), 0, 20 * 5, 0);
            for (Block block : blocks)
                SkyBlock.getInstance().getCreatingBlocks().add(block.getLocation());

            blocks.forEach(block -> {
                if (block.getType().equals(Material.CHEST)) {
                    Chest chest = (Chest) block.getState();

                    chest.getBlockInventory().addItem(
                            new ItemStack(Material.LAVA_BUCKET),
                            new ItemStack(Material.ICE, 2),
                            new ItemStack(Material.BREAD, 16),
                            new ItemStack(Material.valueOf(islandType.name().split("_")[0] + "_SAPLING"), 6));

                }
            });
        }, pasteFinish -> {
            for (Block placedBlock : pasteFinish.getPlacedBlocks()) {
                SkyBlock.getInstance().getCreatingBlocks().remove(placedBlock.getLocation());

                placedBlock.setBiome(islandType.getDefaultBiome());

                PacketWorldChunk packet = new PacketWorldChunk(placedBlock.getChunk());
                packet.send(player);
            }
            String progress = Verany.getProgressBar(100, 100, 10, '▇', ChatColor.GREEN, ChatColor.RED);
            player.sendTitle(player.getKeyMessage("sb_loading_title_complete"), player.getKeyMessage("sb_loading_subtitle", progress, Verany.round(100)), 0, 20 * 2, 10);
            player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_paste_complete");
            player.teleport(location);
            player.removeMetadata("freeze");
            player.removeMetadata("newIsland");
            barrier.setType(Material.AIR);

            Entity entity = location.getWorld().spawn(location, islandType.getIslandEntity());
            entity.setCustomNameVisible(true);
            entity.setCustomName(player.getNameWithColor() + "§7's Pet");

            if (entity instanceof Fox) {
                ((Fox) entity).setFirstTrustedPlayer(player);
                ((Fox) entity).setRemoveWhenFarAway(false);
            } else if (entity instanceof Parrot) {
                ((Parrot) entity).setOwner(player);
                ((Parrot) entity).setRemoveWhenFarAway(false);
            }
        });
        setIslandLocation(location.toVeranyLocation());
        SkyBlock.getInstance().getIslands().add(location.toVeranyLocation());
    }

    public void deleteIsland() {
        for (Block block : islandCuboid.getBlocks())
            if (!block.getType().isAir())
                block.setType(Material.AIR);

        islandCuboid = null;
        islandLocation = null;
    }

    public Cuboid getCuboid() {
        return islandCuboid;
    }

    public int getIslandSize() {
        if (player.getPermissionObject().getGroup().equals(PermissionGroup.PLAYER))
            return 50;
        if (player.getPermissionObject().getGroup().equals(PermissionGroup.VIP))
            return 100;
        if (player.getPermissionObject().getGroup().equals(PermissionGroup.MVP))
            return 150;
        if (player.getPermissionObject().getGroup().equals(PermissionGroup.MVP_PLUS))
            return 200;
        return 250;
    }

    public VeranyLocation getIslandLocation() {
        if (islandLocation == null)
            return null;
        return islandLocation.toBukkitLocation().clone().add(.5, 0, .5).toVeranyLocation();
    }

    public void setIslandLocation(VeranyLocation islandLocation) {
        this.islandLocation = islandLocation;
        loadCuboid();
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }
}
