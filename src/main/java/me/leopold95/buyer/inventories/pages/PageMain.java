package me.leopold95.buyer.inventories.pages;

import me.leopold95.buyer.core.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PageMain implements InventoryHolder {
    private Inventory inventory;

    public PageMain(){
        inventory = Bukkit.createInventory(this, 54, Config.getString("name-main-page"));
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
