package net.verany.skyblock.commands;

import net.verany.Verany;
import net.verany.skyblock.SkyBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (commandSender instanceof ConsoleCommandSender) {
            System.out.println("Dieser Command ist nur für Spieler");
            return false;
        }
        Location spawn = Verany.WORLD_OBJECT.getWorldConfig().getWorldData("island").getWorldSpawn().toBukkitLocation();
        if (player.hasPermission("skyblock.cooldown.bypass")) {
            player.teleport(spawn);
            player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_spawn_cmd_success");
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            return false;
        }
        player.setMetadata("tpaCount", 3);
        player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_spawn_cmd_3sek");
        player.setMetadata("tpaTask", Bukkit.getScheduler().scheduleSyncRepeatingTask(SkyBlock.getInstance(), () -> {
            int count = player.getMetadata("tpaCount").get(0).asInt();
            player.sendActionBar(player.getKeyMessage("sb_cooldown_count", count));
            count--;
            player.setMetadata("tpaCount", count);
            if (count == -1) {
                player.teleport(spawn);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                Bukkit.getScheduler().cancelTask(player.getMetadata("tpaTask").get(0).asInt());
                player.removeMetadata("tpaTask");
                player.sendKeyMessageWithBefore(Verany.getPrefix("SkyBlock"), "sb_spawn_cmd_success");
            }
        }, 0, 20));


        //player.teleport(Bukkit.getWorld("island").getSpawnLocation());

        return false;
    }

}
