package net.verany.skyblock.game.settings;

import lombok.Getter;
import net.verany.settings.AbstractVeranySetting;
import net.verany.settings.VeranySettingType;
import net.verany.skyblock.game.settings.data.IslandInteractionData;
import org.bukkit.Material;

@Getter
public class IslandSetting<T> extends AbstractVeranySetting<T> {

    public static final IslandSetting<IslandInteractionData> USE_DOORS = new IslandSetting<>(IslandInteractionData.class, "USE_DOORS", "skyblock_interactions", new IslandInteractionData(), Material.OAK_DOOR);
    public static final IslandSetting<IslandInteractionData> USE_FENCE_GATES = new IslandSetting<>(IslandInteractionData.class, "USE_FENCE_GATES", "skyblock_interactions", new IslandInteractionData(), Material.OAK_FENCE_GATE);
    public static final IslandSetting<IslandInteractionData> USE_REDSTONE = new IslandSetting<>(IslandInteractionData.class, "USE_REDSTONE", "skyblock_interactions", new IslandInteractionData(), Material.REDSTONE);
    public static final IslandSetting<IslandInteractionData> USE_CRAFTING_TABLES = new IslandSetting<>(IslandInteractionData.class, "USE_CRAFTING_TABLES", "skyblock_interactions", new IslandInteractionData(), Material.CRAFTING_TABLE);
    public static final IslandSetting<IslandInteractionData> USE_FURNACES = new IslandSetting<>(IslandInteractionData.class, "USE_FURNACES", "skyblock_interactions", new IslandInteractionData(), Material.FURNACE);
    public static final IslandSetting<IslandInteractionData> USE_CHESTS = new IslandSetting<>(IslandInteractionData.class, "USE_CHESTS", "skyblock_interactions", new IslandInteractionData(), Material.CHEST);
    public static final IslandSetting<IslandInteractionData> USE_ENDER_CHESTS = new IslandSetting<>(IslandInteractionData.class, "USE_ENDER_CHESTS", "skyblock_interactions", new IslandInteractionData(), Material.ENDER_CHEST);
    public static final IslandSetting<IslandInteractionData> USE_ENCHANTING_TABLES = new IslandSetting<>(IslandInteractionData.class, "USE_ENCHANTING_TABLES", "skyblock_interactions", new IslandInteractionData(), Material.ENCHANTING_TABLE);
    public static final IslandSetting<IslandInteractionData> USE_ANVILS = new IslandSetting<>(IslandInteractionData.class, "USE_ANVILS", "skyblock_interactions", new IslandInteractionData(), Material.ANVIL);
    public static final IslandSetting<IslandInteractionData> USE_BEDS = new IslandSetting<>(IslandInteractionData.class, "USE_BEDS", "skyblock_interactions", new IslandInteractionData(), Material.RED_BED);
    public static final IslandSetting<IslandInteractionData> USE_MINECRATS = new IslandSetting<>(IslandInteractionData.class, "USE_MINECARTS", "skyblock_interactions", new IslandInteractionData(), Material.MINECART);
    public static final IslandSetting<IslandInteractionData> USE_BOATS = new IslandSetting<>(IslandInteractionData.class, "USE_BOATS", "skyblock_interactions", new IslandInteractionData(), Material.OAK_BOAT);

    private final Material material;

    public IslandSetting(Class<T> tClass, String key, String category, T defaultValue, Material material) {
        super(tClass, key, category, defaultValue);
        this.material = material;
        values().add(this);
    }

    public static IslandSetting<?> getSettingByMaterial(Material material) {
        for (AbstractVeranySetting<?> setting : VeranySettingType.values())
            if (setting.getCategory().startsWith("skyblock_"))
                if (((IslandSetting<?>) setting).getMaterial().equals(material))
                    return (IslandSetting<?>) setting;
        return null;
    }

    public static void load() {}
}
