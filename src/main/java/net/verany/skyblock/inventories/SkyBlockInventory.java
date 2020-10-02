package net.verany.skyblock.inventories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.Verany;
import net.verany.enumhelper.EnumHelper;
import net.verany.enumhelper.VeranyEnum;
import net.verany.inventory.AbstractVeranyInventory;
import net.verany.itembuilder.ItemBuilder;
import net.verany.player.IPlayerInfo;
import net.verany.settings.AbstractVeranySetting;
import net.verany.skullchanger.SkullBuilder;
import net.verany.skullchanger.VeranySkullList;
import net.verany.skyblock.game.island.IIslandObject;
import net.verany.skyblock.game.player.GamePlayer;
import net.verany.skyblock.game.player.data.member.IslandMember;
import net.verany.skyblock.game.settings.IslandSetting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;

import java.util.Arrays;
import java.util.List;

public class SkyBlockInventory {

    private final Player player;
    private final Integer[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
    private AbstractVeranyInventory inventory;

    public SkyBlockInventory(Player player) {
        this.player = player;
    }

    public SkyBlockInventory setMainPage() {
        inventory = Bukkit.createInventory(null, 9 * 6, player.getKeyMessage("sb_island_inv"), event -> {
            event.setCancelled(true);

            MainCategory category = EnumHelper.INSTANCE.valueOf(event.getCurrentItem().getType(), MainCategory.values());
            if (category != null) {
                setCategoryItems(category).load();
            }
        });

        inventory.fillCycle(Verany.getPlaceholder(Material.BLUE_STAINED_GLASS_PANE));
        inventory.fillInventory(Verany.getPlaceholder(Material.LIGHT_BLUE_STAINED_GLASS_PANE));

        inventory.setItem(4, new ItemBuilder(Material.GRASS_BLOCK).setDisplayName(player.getKeyMessage("sb_inventory")).build());

        int[] slots = {20, 24, 30, 32};
        for (int i = 0; i < slots.length; i++) {
            MainCategory category = MainCategory.values()[i];

            inventory.setItem(slots[i], new ItemBuilder(category.getMaterial()).setDisplayName(player.getKeyMessage("sb_category_" + category.name().toLowerCase())).addItemFlag(ItemFlag.values()).build());
        }

        return this;
    }

    public SkyBlockInventory setCategoryItems(MainCategory category) {
        player.setMetadata("main_category", category);

        inventory = Bukkit.createInventory(null, 9 * 6, player.getKeyMessage("sb_category_title_" + category.name().toLowerCase()), event -> {
            event.setCancelled(true);

            if (event.getCurrentItem().getType().equals(Material.CLAY_BALL)) {
                setMainPage().load();
                return;
            }

            MainCategory.SettingCategory settingCategory = EnumHelper.INSTANCE.valueOf(event.getCurrentItem().getType(), MainCategory.SettingCategory.values());
            if (settingCategory != null) {
                setSettingItems(settingCategory).load();
            }
        });

        inventory.fillCycle(Verany.getPlaceholder(Material.BLUE_STAINED_GLASS_PANE));
        inventory.fillInventory(Verany.getPlaceholder(Material.LIGHT_BLUE_STAINED_GLASS_PANE));

        inventory.setItem(4, new ItemBuilder(category.getMaterial()).setDisplayName(player.getKeyMessage("sb_inventory_" + category.name().toLowerCase())).build());

        switch (category) {
            case ISLAND_SETTINGS: {
                int[] slots = {20, 24, 30, 32};
                for (int i = 0; i < MainCategory.SettingCategory.values().length; i++) {
                    MainCategory.SettingCategory settingCategory = MainCategory.SettingCategory.values()[i];
                    inventory.setItem(slots[i], new ItemBuilder(settingCategory.getMaterial()).setDisplayName(player.getKeyMessage("sb_setting_category_" + settingCategory.name().toLowerCase())).build());
                }
                break;
            }
        }

        inventory.setItem(inventory.getSize() - 9, new ItemBuilder(Material.CLAY_BALL).setDisplayName(player.getKeyMessage("back")).build());

        return this;
    }

    public SkyBlockInventory setSettingItems(MainCategory.SettingCategory category) {
        player.setMetadata("setting_category", category);

        inventory = Bukkit.createInventory(null, 9 * 6, player.getKeyMessage("sb_category_title_" + category.name().toLowerCase()), event -> {
            event.setCancelled(true);

            if (event.getCurrentItem().getType().equals(Material.CLAY_BALL)) {
                setCategoryItems((MainCategory) player.getMetadata("main_category").get(0).value()).load();
                return;
            }

            IslandSetting<?> setting = IslandSetting.getSettingByMaterial(event.getCurrentItem().getType());
            if (setting != null)
                setSettingRankItems(setting).load();

        });

        inventory.fillCycle(Verany.getPlaceholder(Material.BLUE_STAINED_GLASS_PANE));
        inventory.fillInventory(Verany.getPlaceholder(Material.LIGHT_BLUE_STAINED_GLASS_PANE));

        inventory.setItem(4, new ItemBuilder(MainCategory.ISLAND_SETTINGS.getMaterial()).setDisplayName(player.getKeyMessage("sb_inventory_" + MainCategory.ISLAND_SETTINGS.name().toLowerCase())).build());

        switch (category) {
            case OBJECT_INTERACTIONS: {

                int currentPage = player.getPage("inv_object_interactions");
                List<AbstractVeranySetting<?>> settingList = Verany.getPageList(currentPage, slots.length, IslandSetting.getSettingsByCategory("skyblock_interactions"));
                for (int i = 0; i < settingList.size(); i++) {
                    IslandSetting<?> setting = (IslandSetting<?>) settingList.get(i);

                    inventory.setItem(slots[i], new ItemBuilder(setting.getMaterial()).build());
                }

                int maxPage = Verany.getMaxPage(IslandSetting.getSettingsByCategory("skyblock_interactions"), Arrays.asList(slots));

                inventory.setItem(49, new ItemBuilder(Material.PAPER).setDisplayName(player.getKeyMessage("current_page", currentPage, maxPage)).build());
                inventory.setItem(50, VeranySkullList.FORWARD.clone().setDisplayName(player.getKeyMessage("next_page")).build());

                break;
            }
            case MEMBERS: {
                int currentPage = player.getPage("inv_member");

                List<IslandMember> members = Verany.getPageList(currentPage, slots.length, player.getVeranyPlayer(GamePlayer.class).getMembers());
                for (int i = 0; i < members.size(); i++) {
                    IslandMember member = members.get(i);
                    IPlayerInfo playerInfo = Verany.getPlayerInfo(member.getUuid());
                    inventory.setItem(slots[i], new SkullBuilder(playerInfo.getSkinData()).setDisplayName(playerInfo.getNameWithColor()).build());
                }
                break;
            }
        }

        inventory.setItem(inventory.getSize() - 9, new ItemBuilder(Material.CLAY_BALL).setDisplayName(player.getKeyMessage("back")).build());

        return this;
    }

    public SkyBlockInventory setSettingRankItems(IslandSetting<?> setting) {
        inventory = Bukkit.createInventory(null, 9 * 6, player.getKeyMessage("sb_setting_title"), event -> {
            event.setCancelled(true);

            if (event.getCurrentItem().getType().equals(Material.CLAY_BALL)) {
                setSettingItems((MainCategory.SettingCategory) player.getMetadata("setting_category").get(0).value()).load();
                return;
            }


        });

        inventory.fillCycle(Verany.getPlaceholder(Material.BLUE_STAINED_GLASS_PANE));
        inventory.fillInventory(Verany.getPlaceholder(Material.LIGHT_BLUE_STAINED_GLASS_PANE));


        inventory.setItem(4, new ItemBuilder(setting.getMaterial()).setDisplayName(player.getKeyMessage("sb_inventory_" + setting.getKey().toLowerCase())).build());

        inventory.setItem(inventory.getSize() - 9, new ItemBuilder(Material.CLAY_BALL).setDisplayName(player.getKeyMessage("back")).build());

        return this;
    }

    public SkyBlockInventory setIslandTypes() {
        this.inventory = Bukkit.createInventory(player, InventoryType.HOPPER, player.getKeyMessage("sb_startisland_type_inv"), event -> {
            event.setCancelled(true);

            IIslandObject.IslandType islandType = IIslandObject.IslandType.getTypeByMaterial(event.getCurrentItem().getType());
            if (islandType != null) {
                player.closeInventory();
                player.getVeranyPlayer(GamePlayer.class).getIslandObject().createIsland(islandType);
            }
        });

        for (IIslandObject.IslandType value : IIslandObject.IslandType.values())
            inventory.addItem(new ItemBuilder(value.getMaterial()).setDisplayName(player.getKeyMessage("sb_islandtype_" + value.name().toLowerCase())).addLoreArray(player.getKeyMessageArray("sb_islandtype_description_" + value.name().toLowerCase(), "~")).build());

        return this;
    }

    public SkyBlockInventory setViewArmor(Player target) {
        inventory = Bukkit.createInventory(null, InventoryType.HOPPER, "§bViewArmor", event -> {
            event.setCancelled(true);
        });

        inventory.setItem(0, new SkullBuilder(target.getSkinData()).setDisplayName("§7Armor from " + target.getNameWithColor()).build());

        if (target.getInventory().getHelmet() != null)
            inventory.setItem(1, target.getInventory().getHelmet());
        if (target.getInventory().getChestplate() != null)
            inventory.setItem(2, target.getInventory().getChestplate());
        if (target.getInventory().getLeggings() != null)
            inventory.setItem(3, target.getInventory().getLeggings());
        if (target.getInventory().getBoots() != null)
            inventory.setItem(4, target.getInventory().getBoots());

        return this;
    }


    public void load() {
        player.openInventory(inventory);
    }

    @AllArgsConstructor
    @Getter
    public enum MainCategory implements VeranyEnum {
        ISLAND_SETTINGS(Material.COMPARATOR),
        TOP_ISLANDS(Material.SPRUCE_SIGN),
        ISLAND_LEVEL(Material.EXPERIENCE_BOTTLE),
        ISLAND_LOOT(Material.CHEST_MINECART);

        private final Material material;

        @AllArgsConstructor
        @Getter
        public enum SettingCategory implements VeranyEnum {

            OBJECT_INTERACTIONS(Material.OAK_DOOR),
            MEMBERS(Material.PLAYER_HEAD),
            MINIONS(Material.ZOMBIE_HEAD),
            ITEM_SHOPS(Material.ENDER_CHEST);

            private final Material material;
        }
    }

    /*private final Player player;
    private AbstractVeranyInventory inventory;

    public SkyBlockInventory(Player player) {
        this.player = player;
    }

    public SkyBlockInventory setIslandMainPage(Player player) {
        this.inventory = Bukkit.createInventory(player, 9 * 4, player.getKeyMessage("sb_island_inv"), event -> {
            event.setCancelled(true);
            if (event.getCurrentItem().getType().equals(Material.LAVA_BUCKET)) {
                setStartIslandType().load();
            } else if (event.getCurrentItem().getType().equals(Material.REDSTONE_TORCH)) {
                setIslandSettings().load();
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(player.getKeyMessage("close"))) {
                player.closeInventory();
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(player.getKeyMessage("sb_island_inv_mitarbeiter"))) {
                setIslandMember().load();
            } else if (event.getCurrentItem().getType().equals(Material.SPRUCE_SAPLING)) {
                setIslandBiome().load();
            } else if (event.getCurrentItem().getType().equals(Material.PISTON)) {
                setIslandUpgrades().load();
            }
        });

        inventory.fillCycle(Verany.getPlaceholder(Material.LIGHT_BLUE_STAINED_GLASS_PANE));

        if (SkyBlock.getInstance().getGamePlayer(player).getIslandLocation() == null)
            inventory.setItem(13, new ItemBuilder(Material.LAVA_BUCKET).setDisplayName(player.getKeyMessage("sb_screate_island_lava_bucket")).build());
        else {

            inventory.setItem(4, new ItemBuilder(Material.GRASS_BLOCK).setDisplayName(player.getKeyMessage("sb_inv_grassblock")).build());
            inventory.setItem(11, new ItemBuilder(Material.REDSTONE_TORCH).setDisplayName(player.getKeyMessage("sb_island_inv_settings")).build());
            inventory.setItem(15, new SkullBuilder(player.getSkinData()).setDisplayName(player.getKeyMessage("sb_island_inv_mitarbeiter")).build());
            inventory.setItem(21, new ItemBuilder(Material.SPRUCE_SAPLING).setDisplayName(player.getKeyMessage("sb_island_inv_biome")).build());
            inventory.setItem(23, new ItemBuilder(Material.PISTON).setDisplayName(player.getKeyMessage("sb_island_inv_upgrades")).build());

        }

        inventory.setItem(inventory.getSize() - 9, new ItemBuilder(Material.CLAY_BALL).setDisplayName(player.getKeyMessage("close")).build());

        return this;
    }

    public SkyBlockInventory setIslandSettings() {
        this.inventory = Bukkit.createInventory(player, 9 * 4, player.getKeyMessage("sb_island_settings_inv"), event -> {
            event.setCancelled(true);
            if (event.getCurrentItem().getType().equals(Material.LAVA_BUCKET)) {
                setStartIslandType().load();
            } else if (event.getCurrentItem().getType().equals(Material.ENDER_PEARL)) {
                player.teleport(player.getVeranyPlayer(GamePlayer.class).getIslandLocation());
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(player.getKeyMessage("back"))) {
                setIslandMainPage(player).load();
            } else if (event.getCurrentItem().getType().equals(Material.TNT)) {
                player.performCommand("spawn");
                for (Block block : player.getVeranyPlayer(GamePlayer.class).getCuboid().getBlocks()) {
                    block.setType(Material.AIR);
                }
            }
        });
        inventory.fillCycle(Verany.getPlaceholder(Material.LIGHT_BLUE_STAINED_GLASS_PANE));

        inventory.setItem(10, new ItemBuilder(Material.ENDER_PEARL).setDisplayName(player.getKeyMessage("sb_teleport_to_own_island")).build());
        inventory.setItem(11, new ItemBuilder(Material.TNT).setDisplayName(player.getKeyMessage("sb_reset_island")).build());

        inventory.setItem(inventory.getSize() - 9, new ItemBuilder(Material.CLAY_BALL).setDisplayName(player.getKeyMessage("back")).build());

        return this;
    }

    public SkyBlockInventory setIslandMember() {
        int currentPage = player.getPage("member_list");

        this.inventory = Bukkit.createInventory(player, 9 * 4, player.getKeyMessage("sb_island_mitarbeiter_inv"), event -> {
            event.setCancelled(true);
            if (event.getCurrentItem().getItemMeta().getDisplayName().equals(player.getKeyMessage("back"))) {
                setIslandMainPage(player).load();
                return;
            }
            if (event.getCurrentItem().getType().equals(Material.WRITABLE_BOOK)) {
                setSearchInventory().load();
                return;
            }
            if (event.getCurrentItem().getType().equals(Material.COMPASS)) {
                if (player.hasMetadata("filtered_player")) {
                    player.removeMetadata("filtered_player");
                    setIslandMember().load();
                    return;
                }
                player.closeInventory();
                player.setMetadata("filter_player", true);
                return;
            }
            if (event.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
                if (event.getCurrentItem().getItemMeta().getDisplayName().equals(player.getKeyMessage("back"))) {
                    if (currentPage == 1) return;
                    player.previousPage("member_list");
                    setIslandMember().load();
                    return;
                }
                if (event.getCurrentItem().getItemMeta().getDisplayName().equals(player.getKeyMessage("forward"))) {
                    player.nextPage("member_list");
                    setIslandMember().load();
                    return;
                }

                String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                player.getVeranyPlayer(GamePlayer.class).getMembers().remove(player.getVeranyPlayer(GamePlayer.class).getMemberByUUID(Verany.getPlayerUUIDByName(name)));
                setIslandMember().load();
                return;
            }
        });
        inventory.fillCycle(Verany.getPlaceholder(Material.LIGHT_BLUE_STAINED_GLASS_PANE));
        List<Integer> freeSlots = new ArrayList<>();
        for (int i = 0; i < inventory.getSize(); i++)
            if (inventory.getItem(i) == null)
                freeSlots.add(i);

        List<IslandMember> members = Verany.getPageList(currentPage, freeSlots.size(), player.getVeranyPlayer(GamePlayer.class).getMembers());

        if (player.hasMetadata("filtered_player")) {
            String searchedPlayer = player.getMetadata("filtered_player").get(0).asString();
            members.removeIf(islandMember -> !Verany.getPlayerNameByUUID(islandMember.getUuid()).toLowerCase().contains(searchedPlayer.toLowerCase()));
        }

        for (int i = 0; i < members.size(); i++) {
            IslandMember member = members.get(i);
            IPlayerInfo playerInfo = Verany.getPlayerInfo(member.getUuid());
            inventory.setItem(freeSlots.get(i), new SkullBuilder(playerInfo.getSkinData()).setDisplayName(playerInfo.getNameWithColor()).build());
        }

        inventory.setItem(29, new ItemBuilder(Material.WRITABLE_BOOK).setDisplayName("neu").build());
        inventory.setItem(30, VeranySkullList.BACK.clone().setDisplayName(player.getKeyMessage("back")).build());
        inventory.setItem(31, new ItemBuilder(Material.COMPASS).setDisplayName("search player").build());
        inventory.setItem(32, VeranySkullList.FORWARD.clone().setDisplayName(player.getKeyMessage("forward")).build());

        inventory.setItem(inventory.getSize() - 9, new ItemBuilder(Material.CLAY_BALL).setDisplayName(player.getKeyMessage("back")).build());

        return this;
    }

    public SkyBlockInventory setSearchInventory() {
        int currentPage = player.getPage("search_player");

        inventory = Bukkit.createInventory(null, 9 * 6, "alle spieler", event -> {
            event.setCancelled(true);

            if (event.getCurrentItem().getType().equals(Material.COMPASS)) {
                if (player.hasMetadata("searched_player")) {
                    player.removeMetadata("searched_player");
                    setSearchInventory().load();
                    return;
                }
                player.closeInventory();
                player.sendMessage("suche nach spieler");
                player.setMetadata("search_player", true);
                return;
            }
            if (event.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
                if (event.getCurrentItem().getItemMeta().getDisplayName().equals(player.getKeyMessage("back"))) {
                    if (currentPage == 1) return;
                    player.previousPage("search_player");
                    setSearchInventory().load();
                    return;
                }
                if (event.getCurrentItem().getItemMeta().getDisplayName().equals(player.getKeyMessage("forward"))) {
                    player.nextPage("search_player");
                    setSearchInventory().load();
                    return;
                }

                String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                player.getVeranyPlayer(GamePlayer.class).getMembers().add(new IslandMember(Verany.getPlayerUUIDByName(name), IslandRank.MEMBER));
                setIslandMember().load();
            }
        });

        inventory.fillCycle(Verany.getPlaceholder(Material.GRAY_STAINED_GLASS_PANE));

        List<Integer> freeSlots = new ArrayList<>();
        for (int i = 0; i < inventory.getSize(); i++)
            if (inventory.getItem(i) == null)
                freeSlots.add(i);

        List<String> unsorted = new CopyOnWriteArrayList<>();
        for (IPlayerInfo registeredPlayer : Verany.REGISTERED_PLAYERS)
            if (registeredPlayer.getSkinData() != null)
                unsorted.add(registeredPlayer.getName());
        Collections.sort(unsorted);
        List<IPlayerInfo> playerList = new CopyOnWriteArrayList<>();
        for (String s : unsorted)
            playerList.add(Verany.getPlayerInfo(s));
        List<IPlayerInfo> players = Verany.getPageList(currentPage, freeSlots.size(), playerList);

        players.removeIf(playerInfo -> player.getVeranyPlayer(GamePlayer.class).getMemberByUUID(playerInfo.getUniqueId()) != null);

        if (player.hasMetadata("searched_player")) {
            String searchedPlayer = player.getMetadata("searched_player").get(0).asString();
            players.removeIf(playerInfo -> !playerInfo.getName().toLowerCase().contains(searchedPlayer.toLowerCase()));
        }

        for (int i = 0; i < players.size(); i++) {
            IPlayerInfo playerInfo = players.get(i);
            inventory.setItem(freeSlots.get(i), new SkullBuilder(playerInfo.getSkinData()).setDisplayName(playerInfo.getNameWithColor()).addLoreArray(player.getKeyMessageArray("sb_search_player_lore", "~")).build());
        }

        inventory.setItem(45, new ItemBuilder(Material.CLAY_BALL).setDisplayName(player.getKeyMessage("back")).build());
        inventory.setItem(48, VeranySkullList.BACK.clone().setDisplayName(player.getKeyMessage("back")).build());
        inventory.setItem(49, new ItemBuilder(Material.COMPASS).setDisplayName("search player").build());
        inventory.setItem(50, VeranySkullList.FORWARD.clone().setDisplayName(player.getKeyMessage("forward")).build());

        return this;
    }

    public SkyBlockInventory setIslandBiome() {
        this.inventory = Bukkit.createInventory(player, 9 * 4, player.getKeyMessage("sb_island_biome_inv"), event -> {
            event.setCancelled(true);
            if (event.getCurrentItem().getItemMeta().getDisplayName().equals(player.getKeyMessage("back"))) {
                setIslandMainPage(player).load();
            }
        });
        inventory.fillCycle(Verany.getPlaceholder(Material.LIGHT_BLUE_STAINED_GLASS_PANE));


        inventory.setItem(inventory.getSize() - 9, new ItemBuilder(Material.CLAY_BALL).setDisplayName(player.getKeyMessage("back")).build());

        return this;
    }

    public SkyBlockInventory setIslandUpgrades() {
        this.inventory = Bukkit.createInventory(player, 9 * 4, player.getKeyMessage("sb_island_upgrades_inv"), event -> {
            event.setCancelled(true);
            if (event.getCurrentItem().getItemMeta().getDisplayName().equals(player.getKeyMessage("back"))) {
                setIslandMainPage(player).load();
            }
        });
        inventory.fillCycle(Verany.getPlaceholder(Material.LIGHT_BLUE_STAINED_GLASS_PANE));


        inventory.setItem(inventory.getSize() - 9, new ItemBuilder(Material.CLAY_BALL).setDisplayName(player.getKeyMessage("back")).build());

        return this;
    }

    public SkyBlockInventory setStartIslandType() {
        this.inventory = Bukkit.createInventory(player, InventoryType.HOPPER, player.getKeyMessage("sb_startisland_type_inv"), event -> {
            event.setCancelled(true);

            IIslandObject.IslandType islandType = IIslandObject.IslandType.getTypeByMaterial(event.getCurrentItem().getType());
            if (islandType != null) {
                player.closeInventory();
                player.getVeranyPlayer(GamePlayer.class).getIslandObject().createIsland(islandType);
            }
        });

        for (IIslandObject.IslandType value : IIslandObject.IslandType.values())
            inventory.addItem(new ItemBuilder(value.getMaterial()).build());

        return this;
    }


    public SkyBlockInventory setViewArmor(Player target) {
        inventory = Bukkit.createInventory(null, InventoryType.HOPPER, "§bViewArmor", event -> {

        });

        inventory.setItem(0, new SkullBuilder(target.getSkinData()).setDisplayName("§7Armor from " + target.getNameWithColor()).build());

        if (target.getInventory().getHelmet() != null)
            inventory.setItem(1, target.getInventory().getHelmet());
        if (target.getInventory().getChestplate() != null)
            inventory.setItem(2, target.getInventory().getChestplate());
        if (target.getInventory().getLeggings() != null)
            inventory.setItem(3, target.getInventory().getLeggings());
        if (target.getInventory().getBoots() != null)
            inventory.setItem(4, target.getInventory().getBoots());

        return this;
    }


    public void load() {
        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1.7F);
    }*/
}

