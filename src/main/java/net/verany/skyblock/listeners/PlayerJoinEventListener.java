package net.verany.skyblock.listeners;

import net.verany.Verany;
import net.verany.event.AbstractListener;
import net.verany.hotbar.AbstractHotbarItem;
import net.verany.npc.INPCObject;
import net.verany.skullchanger.SkullBuilder;
import net.verany.skyblock.game.player.GamePlayer;
import net.verany.skyblock.inventories.SkyBlockInventory;
import net.verany.skyblock.module.SkyBlockModule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventListener extends AbstractListener<SkyBlockModule> {

    private final String grassValue = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQ0OWI5MzE4ZTMzMTU4ZTY0YTQ2YWIwZGUxMjFjM2Q0MDAwMGUzMzMyYzE1NzQ5MzJiM2M4NDlkOGZhMGRjMiJ9fX0=";

    public PlayerJoinEventListener(SkyBlockModule module) {
        super(module);

        Verany.registerListener(module.getPlugin(), PlayerJoinEvent.class, event -> {
            Player player = event.getPlayer();

            /*if (Verany.WORLD_OBJECT.getWorldConfig().getWorldData("island") == null)
                Verany.WORLD_OBJECT.createWorld("island", WorldType.FLAT, World.Environment.NORMAL, true);*/

            GamePlayer gamePlayer = new GamePlayer();
            gamePlayer.load(module, player.getUniqueId());
            player.setVeranyPlayer(gamePlayer);

            event.setJoinMessage(null);

            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "player_join", player.getNameWithColor()));

            player.setHealthScale(20);

            Location locationsettings = Verany.LOCATION_MANAGER.getDatabaseLocation(module, "SKYBLOCK", "settings").toBukkitLocation().add(.5, 0, .5);
            INPCObject npcsettings = Verany.createNPC(player.getKeyMessage("sb_npc_settings"), locationsettings, true, player);
            npcsettings.setGameProfile(Verany.getPlayerInfo("SlashKick").getSkinData());
            npcsettings.spawn();
            npcsettings.setEquipment(INPCObject.ItemSlot.MAINHAND, Material.COMMAND_BLOCK);

            Location locationKits = Verany.LOCATION_MANAGER.getDatabaseLocation(module, "SKYBLOCK", "kits").toBukkitLocation().add(.5, 0, .5);
            INPCObject npcKits = Verany.createNPC(player.getKeyMessage("sb_npc_kits"), locationKits, true, player);
            npcKits.setGameProfile(Verany.getPlayerInfo("tylix").getSkinData());
            npcKits.spawn();
            npcKits.setEquipment(INPCObject.ItemSlot.MAINHAND, Material.GOLDEN_SWORD);

            player.registerNewPage("search_player", "member_list", "inv_object_interactions", "inv_member");

            player.getInventory().setItem(8, new AbstractHotbarItem(new SkullBuilder(grassValue).setDisplayName(player.getKeyMessage("sb_menu_item")), player) {
                @Override
                public void onInteract(PlayerInteractEvent event) {
                    event.setCancelled(true);
                    if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                        if(gamePlayer.getIslandLocation() == null) {
                            new SkyBlockInventory(player).setIslandTypes().load();
                            return;
                        }
                        new SkyBlockInventory(player).setMainPage().load();
                    }
                }

                @Override
                public void onDrop(PlayerDropItemEvent event) {
                    event.setCancelled(true);
                }

                @Override
                public void onClick(InventoryClickEvent event) {
                    event.setCancelled(true);
                }

                @Override
                public void onPlace(BlockPlaceEvent event) {
                    event.setCancelled(true);
                }
            });

        });

        Verany.registerListener(module.getPlugin(), PlayerInteractEvent.class, event -> {
            Player player = event.getPlayer();

            if (event.getItem() != null && event.getItem().getType().equals(Material.IRON_AXE)) {
                if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    player.setMetadata("first", event.getClickedBlock().getLocation());
                    player.sendMessage(Verany.getPrefix("VeranySchematic") + "§7First position set!");
                    event.setCancelled(true);
                } else if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                    player.setMetadata("second", event.getClickedBlock().getLocation());
                    player.sendMessage(Verany.getPrefix("VeranySchematic") + "§7Second position set!");
                    event.setCancelled(true);
                }
            }
        });
    }

}
