package me.leopold95.buyer.core;

import me.leopold95.buyer.Buyer;
import me.leopold95.buyer.abstraction.RepitingTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoBuyerUpdater extends RepitingTask {
    private Buyer plugin;

    public AutoBuyerUpdater(Buyer plugin) {
        super(plugin, Config.getInt("auto-buyer-update-ticks"));
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for(Player player: Bukkit.getOnlinePlayers()){
            if(!player.getPersistentDataContainer().has(plugin.keys.PLAYER_AUTO_SELL_ENABLED))
                return;


        }
    }
}
