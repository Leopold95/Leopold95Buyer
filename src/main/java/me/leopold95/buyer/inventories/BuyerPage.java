package me.leopold95.buyer.inventories;

import me.leopold95.buyer.core.Config;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class BuyerPage implements InventoryHolder {
    private Inventory inventory;

    public BuyerPage(){
        inventory = Bukkit.createInventory(this, 54, Config.getString("name-main-page"));
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
