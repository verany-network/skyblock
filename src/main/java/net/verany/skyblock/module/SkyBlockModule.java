package net.verany.skyblock.module;

import net.verany.Verany;
import net.verany.module.AbstractVeranyModule;
import org.bukkit.plugin.Plugin;

public class SkyBlockModule extends AbstractVeranyModule {

    public SkyBlockModule(String name, Plugin plugin) {
        super(name, plugin, Verany.generate(10), Verany.register("root", "verany.net", "bgsCScxQtC4CYQyM", "veranyNetwork"));
    }
}
