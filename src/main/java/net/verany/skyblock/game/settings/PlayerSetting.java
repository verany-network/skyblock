package net.verany.skyblock.game.settings;

import net.verany.settings.AbstractVeranySetting;
import org.bukkit.Material;

public class PlayerSetting<T> extends AbstractVeranySetting<T> {

    private final Material material;

    public PlayerSetting(Class<T> tClass, String key, String category, T defaultValue, Material material) {
        super(tClass, key, category, defaultValue);
        this.material = material;
    }
}
