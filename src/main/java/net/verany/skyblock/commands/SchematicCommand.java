package net.verany.skyblock.commands;

import net.verany.Verany;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SchematicCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (!player.hasPermission("verany.command.schematics")) {

            return false;
        }

        if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase("save")) {
                String name = strings[1];
                Location first = (Location) player.getMetadata("first").get(0).value();
                Location seconds = (Location) player.getMetadata("second").get(0).value();
                List<String> list = Verany.WORLD_OBJECT.fromBlockToString(Verany.WORLD_OBJECT.getBlocks(first, seconds), player.getLocation());
                if (Verany.WORLD_OBJECT.saveSchematic(name, list)) {
                    player.sendMessage(Verany.getPrefix("VeranySchematic") + "§7Saving schematic §b" + name + " §asuccess§8!");
                } else {
                    player.sendMessage(Verany.getPrefix("VeranySchematic") + "§7Saving schematic §b" + name + " §cfailed§8!");
                }
            }/* else if (strings[0].equalsIgnoreCase("paste")) {
                String name = strings[1];
                player.sendMessage(Verany.getPrefix("VeranySchematic") + "§7Creating island§8...");
                Bukkit.getScheduler().scheduleSyncDelayedTask(SkyBlock.getInstance(), () -> {
                    Location lastLocation = (SkyBlock.getInstance().getIslands().isEmpty() ? new VeranyLocation(0, 64, 0, 0, 0, "island") : SkyBlock.getInstance().getIslands().get(SkyBlock.getInstance().getIslands().size() - 1)).toBukkitLocation();
                    Location location = lastLocation.clone().add(100, 0, 0);
                    location.getWorld().refreshChunk(location.getBlockX(), location.getBlockZ());
                    location.getWorld().loadChunk(location.getChunk());

                    Verany.WORLD_OBJECT.loadSchematics(name, player);
                    Verany.WORLD_OBJECT.pasteSchematic(player, location, Material.PINK_WOOL);
                    player.teleport(location);
                    SkyBlock.getInstance().getGamePlayer(player).setIslandLocation(location.toVeranyLocation());
                    SkyBlock.getInstance().getIslands().add(location.toVeranyLocation());
                }, 2);
            }*/
        }

        return false;
    }

    public String getUsage() {
        return "null";
    }
}
