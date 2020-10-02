package net.verany.skyblock.game.player.settings;

import net.verany.settings.AbstractVeranySetting;

public class SkyBlockSettings<T> extends AbstractVeranySetting<T> {

    public final static AbstractVeranySetting<Boolean> OTHER_ISLAND_TELEPORT = new SkyBlockSettings<>(Boolean.class, "OTHER_ISLAND_TELEPORT", "SKYBLOCK", true);

    public SkyBlockSettings(Class<T> tClass, String key, String category, T defaultValue) {
        super(tClass, key, category, defaultValue);
        values().add(this);
    }
}
