package net.verany.skyblock;

import net.minecraft.server.v1_16_R1.DedicatedServer;
import net.minecraft.server.v1_16_R1.MinecraftServer;
import net.verany.Verany;
import net.verany.commands.minecraft.MinecraftCommand;
import net.verany.config.IngameConfig;
import net.verany.location.VeranyLocation;
import net.verany.module.AbstractVeranyModule;
import net.verany.npc.INPCObject;
import net.verany.skyblock.commands.*;
import net.verany.skyblock.game.player.GamePlayer;
import net.verany.skyblock.game.settings.IslandSetting;
import net.verany.skyblock.listeners.*;
import net.verany.skyblock.module.SkyBlockModule;
import net.verany.world.EmptyChunkGenerator;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SkyBlock extends JavaPlugin {

    private static SkyBlock instance;

    private final AbstractVeranyModule module;

    private final List<VeranyLocation> islands = new ArrayList<>();
    private final List<Location> creatingBlocks = new ArrayList<>();

    public SkyBlock() {
        module = new SkyBlockModule("SkyBlock", this);
        Verany.registerModule(module);

        IslandSetting.load();
    }

    @Override
    public void onEnable() {
        instance = this;
        System.out.println("SkyBlock by SlashKick erfolgreich gestartet!");
    }

    @Override
    public void onDisable() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            getGamePlayer(onlinePlayer).update(module);
        }
        Bukkit.getWorld("island").save();
    }

    @Override
    public void init() {

        IngameConfig.TABLIST.setValue(true);
        IngameConfig.TABLIST_CLAN.setValue(true);
        IngameConfig.AFK_DATA.getValue().setEnabled(true);
        IngameConfig.AFK_DATA.getValue().setMinutes(5);
        IngameConfig.DAVID_DATA.getValue().setNpc(true);
        IngameConfig.DAVID_DATA.getValue().setTabList(true);
        IngameConfig.DAVID_DATA.getValue().setLocation(Verany.WORLD_OBJECT.getWorldConfig().getWorldData(((DedicatedServer) MinecraftServer.getServer()).propertyManager.getProperties().levelName).getWorldSpawn().toBukkitLocation());

        Verany.COMMAND_OBJECT.registerMinecraftCommand(MinecraftCommand.TIME_COMMAND, MinecraftCommand.WEATHER_COMMAND, MinecraftCommand.SAVE_ALL_COMMAND, MinecraftCommand.KILL_COMMAND);
        //getCommandObject().unregisterVeranyCommand(FlyCommand.class);

        registerListener(Bukkit.getPluginManager());
        registerCommands();

        for (Document skyBlockPlayer : module.getDatabaseManager().getCollection("skyBlockPlayer").find())
            if (!skyBlockPlayer.getString("islandLocation").equals("a"))
                islands.add(VeranyLocation.fromString(skyBlockPlayer.getString("islandLocation")));

        Bukkit.getWorld("island").setGameRule(GameRule.DO_FIRE_TICK, false);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (INPCObject npc : Verany.NPCS) {
                Player player = npc.getPlayer();
                if (player.getWorld().equals(npc.getLocation().getWorld()))
                    if (player.getLocation().distance(npc.getLocation()) <= 20) {
                        npc.lookAtPlayer(player);

                        if (player.getLocation().distance(npc.getLocation()) <= 1.4)
                            Verany.pushAway(player, npc.getLocation());
                    }
            }
        }, 0, 2);

    }

    public void registerListener(PluginManager pluginManager) {
        new PlayerJoinEventListener((SkyBlockModule) module);
        new PlayerQuitEventListener((SkyBlockModule) module);
        new PlayerChatEvent((SkyBlockModule) module);
        //pluginManager.registerEvents(new BlockFromToEventListener(), this);
        pluginManager.registerEvents(new PlayerDeathListener(), this);
        pluginManager.registerEvents(new ProtectionListener(), this);
        pluginManager.registerEvents(new InventoryClickListener(), this);
    }

    private void registerCommands() {
        getCommand("skyblock").setExecutor(new SkyBlockCommand());
        getCommand("schematic").setExecutor(new SchematicCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("gems").setExecutor(new GemsCommand());
        getCommand("pay").setExecutor(new PayCommand());
        getCommand("fly").setExecutor(new net.verany.skyblock.commands.FlyCommand());
        getCommand("enderchest").setExecutor(new EnderchestCommand());
        getCommand("tpa").setExecutor(new TpaCommand());
        getCommand("tpaccept").setExecutor(new TpAcceptCommand());
        getCommand("tpadeny").setExecutor(new TpaDenyCommand());
        getCommand("food").setExecutor(new FoodCommand());
        getCommand("developer").setExecutor(new DeveloperCommand());
        getCommand("fixall").setExecutor(new FixallCommand());
        getCommand("fix").setExecutor(new FixCommand());
        getCommand("invsee").setExecutor(new InvseeCommand());
        getCommand("viewarmor").setExecutor(new ViewArmorCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("kits").setExecutor(new KitsCommand());
        getCommand("head").setExecutor(new HeadCommand());
        getCommand("updates").setExecutor(new UpdatesCommand());
    }

    public GamePlayer getGamePlayer(UUID uuid) {
        if (Bukkit.getPlayer(uuid) == null) {
            GamePlayer gamePlayer = new GamePlayer();
            gamePlayer.load(module, uuid);
            return gamePlayer;
        }
        return getGamePlayer(Bukkit.getPlayer(uuid));
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new EmptyChunkGenerator();
    }

    public GamePlayer getGamePlayer(Player player) {
        return player.getVeranyPlayer(GamePlayer.class);
    }

    public static SkyBlock getInstance() {
        return instance;
    }

    public List<VeranyLocation> getIslands() {
        return islands;
    }

    public VeranyLocation getNextIsland() {
        VeranyLocation toReturn = null;
        VeranyLocation possibleLocation = new Location(Bukkit.getWorld("island"), 0, 64, 0).toVeranyLocation();
        while (toReturn == null) {
            if (!islandsToString().contains(possibleLocation.toString())) {
                toReturn = possibleLocation;
            } else {
                possibleLocation.toBukkitLocation().add(250, 0, 0);
            }
        }
        return toReturn;
    }

    public List<String> islandsToString() {
        List<String> toReturn = new ArrayList<>();
        for (VeranyLocation island : islands)
            toReturn.add(island.toString());
        return toReturn;
    }

    public AbstractVeranyModule getModule() {
        return module;
    }

    public List<Location> getCreatingBlocks() {
        return creatingBlocks;
    }
}