package net.verany.skyblock.listeners;

import net.minecraft.server.v1_16_R1.PacketPlayInUseEntity;
import net.verany.Verany;
import net.verany.cuboid.Cuboid;
import net.verany.event.events.NPCInteractEvent;
import net.verany.npc.INPCObject;
import net.verany.skyblock.SkyBlock;
import net.verany.skyblock.game.player.GamePlayer;
import net.verany.skyblock.inventories.KitsInventory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class ProtectionListener implements Listener {

    @EventHandler
    public void handlePhysics(BlockPhysicsEvent event) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasMetadata("newIsland")) {
                Location newLocation = (Location) onlinePlayer.getMetadata("newIsland").get(0).value();
                if(event.getBlock().getLocation().distance(newLocation) <= 100) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void handlePhysics(BlockFromToEvent event) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasMetadata("newIsland")) {
                Location newLocation = (Location) onlinePlayer.getMetadata("newIsland").get(0).value();
                if(event.getBlock().getLocation().distance(newLocation) <= 100) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equals("island")) return;
        if (player.hasPermission("skyblock.build.bypass")) return;

        if (event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.CHEST)) {
            if (getRegionPlayer(event.getClickedBlock().getLocation()) != null && getRegionPlayer(event.getClickedBlock().getLocation()).getMemberByUUID(player.getUniqueId()) != null) {
                event.setCancelled(false);
                return;
            }
            Cuboid cuboid = SkyBlock.getInstance().getGamePlayer(player).getCuboid();
            if (cuboid == null) {
                event.setCancelled(true);
                return;
            }
            event.setCancelled(!cuboid.isInside(event.getClickedBlock().getLocation()));
        }
    }

    @EventHandler
    public void handleBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equals("island")) return;
        if (player.hasPermission("skyblock.build.bypass")) return;

        if (getRegionPlayer(event.getBlock().getLocation()) != null && getRegionPlayer(event.getBlock().getLocation()).getMemberByUUID(player.getUniqueId()) != null) {
            event.setCancelled(false);
            return;
        }
        Cuboid cuboid = SkyBlock.getInstance().getGamePlayer(player).getCuboid();
        if (cuboid == null) {
            event.setCancelled(true);
            return;
        }
        event.setCancelled(!cuboid.isInside(event.getBlock().getLocation()));
    }

    @EventHandler
    public void handleMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.hasMetadata("freeze")) {
            int movX = event.getFrom().getBlockX() - event.getTo().getBlockX();
            int movZ = event.getFrom().getBlockZ() - event.getTo().getBlockZ();
            if ((Math.abs(movX) > 0.5) || (Math.abs(movZ) > 0.5))
                player.teleport(event.getFrom());
            return;
        }
    }

    @EventHandler
    public void handlePlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equals("island")) return;
        if (player.hasPermission("skyblock.build.bypass")) return;

        if (getRegionPlayer(event.getBlock().getLocation()) != null && getRegionPlayer(event.getBlock().getLocation()).getMemberByUUID(player.getUniqueId()) != null) {
            event.setCancelled(false);
            return;
        }
        Cuboid cuboid = SkyBlock.getInstance().getGamePlayer(player).getCuboid();
        if (cuboid == null) {
            event.setCancelled(true);
            return;
        }
        event.setCancelled(!cuboid.isInside(event.getBlock().getLocation()));
    }

    @EventHandler
    public void handleNpcInteract(NPCInteractEvent event) {
        Player player = event.getPlayer();
        INPCObject npc = event.getNpc();

        if (!player.getItemInHand().getType().isAir()) return;
        if (!event.getInteractType().equals(PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT)) return;

        if (player.hasMetadata("npc_clicked")) {
            long time = player.getMetadata("npc_clicked").get(0).asLong();
            if (time >= System.currentTimeMillis())
                return;
        }
        player.setMetadata("npc_clicked", System.currentTimeMillis() + 300);

        if (npc.getName().equals(player.getKeyMessage("sb_npc_settings"))) {
            //new SkyBlockInventory(player).setIslandSettings().load();
        } else if (npc.getName().equals(player.getKeyMessage("sb_npc_kits"))) {
            new KitsInventory(player).setKits().load();
        }


    }

    @EventHandler
    public void handleDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    private GamePlayer getRegionPlayer(Location location) {
        for (GamePlayer gamePlayer : Verany.getVeranyPlayerList(GamePlayer.class))
            if (gamePlayer.getCuboid() != null && gamePlayer.getCuboid().isInside(location))
                return gamePlayer;
        return null;
    }

}
