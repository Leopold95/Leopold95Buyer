package me.leopold95.buyer;

import me.leopold95.buyer.commands.BuyerCommand;
import me.leopold95.buyer.commands.BuyerCommandTab;
import me.leopold95.buyer.core.BuyerAdmin;
import me.leopold95.buyer.core.Config;
import me.leopold95.buyer.inventories.BuyerInventories;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Buyer extends JavaPlugin {
    public BuyerAdmin buyerAdmin;

    public Economy economy;

    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Config.register(this);

        buyerAdmin = new BuyerAdmin(this);

        getCommand("buyer").setExecutor(new BuyerCommand(this));
        getCommand("buyer").setTabCompleter(new BuyerCommandTab());
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
