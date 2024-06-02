package me.leopold95.buyer;

import me.leopold95.buyer.core.Config;
import org.bukkit.plugin.java.JavaPlugin;

public final class Buyer extends JavaPlugin {
    @Override
    public void onEnable() {
        Config.register(this);
    }

    @Override
    public void onDisable() {

    }
}
