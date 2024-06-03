package me.leopold95.buyer;

import me.leopold95.buyer.commands.BuyerCommand;
import me.leopold95.buyer.commands.BuyerCommandTab;
import me.leopold95.buyer.core.BuyerAdmin;
import me.leopold95.buyer.core.Config;
import me.leopold95.buyer.core.Keys;
import me.leopold95.buyer.inventories.BuyerInventories;
import me.leopold95.buyer.listeners.InventoryClicked;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.security.Key;

public final class Buyer extends JavaPlugin {
    public BuyerAdmin buyerAdmin;
    public Keys keys;

    public  Economy economy;

    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Config.register(this);
        keys = new Keys(this);
        buyerAdmin = new BuyerAdmin(this);

        getCommand("buyer").setExecutor(new BuyerCommand(this));
        getCommand("buyer").setTabCompleter(new BuyerCommandTab());

        getServer().getPluginManager().registerEvents(new InventoryClicked(this), this);
    }

    @Override
    public void onDisable() {

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
}
