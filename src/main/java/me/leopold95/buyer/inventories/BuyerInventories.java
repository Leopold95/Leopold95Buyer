package me.leopold95.buyer.inventories;

import me.leopold95.buyer.Buyer;
import me.leopold95.buyer.inventories.pages.PageMain;
import org.bukkit.inventory.Inventory;

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
