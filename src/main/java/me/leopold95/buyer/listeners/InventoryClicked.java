package me.leopold95.buyer.listeners;

import me.leopold95.buyer.Buyer;
import me.leopold95.buyer.core.Config;
import me.leopold95.buyer.inventories.pages.PageMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClicked implements Listener {
    private Buyer plugin;

    public InventoryClicked(Buyer plugin){
        this.plugin = plugin;
    }

    @EventHandler
    private void onMainPageClicked(InventoryClickEvent event){
        if(!(event.getInventory().getHolder() instanceof PageMain))
            return;

        if(plugin.buyerAdmin.getBlockedSlots().contains(event.getSlot()))
            return;


    }
}
