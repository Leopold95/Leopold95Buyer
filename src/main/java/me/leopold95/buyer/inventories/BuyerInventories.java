package me.leopold95.buyer.inventories;

import me.leopold95.buyer.Buyer;
import me.leopold95.buyer.inventories.pages.PageMain;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuyerInventories {
    private Buyer plugin;

    private PageMain pageMain;

    public BuyerInventories(Buyer plugin){
        this.plugin = plugin;

        pageMain = new PageMain();

    }

    public Inventory pageMain(){
        return pageMain.getInventory();
    }


}
