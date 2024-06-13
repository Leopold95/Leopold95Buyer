package me.leopold95.buyer.listeners;

import me.leopold95.buyer.Buyer;
import me.leopold95.buyer.core.BuyerAdmin;
import me.leopold95.buyer.inventories.BuyerPage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InventoryClose implements Listener {
    private Buyer plugin;
    private BuyerAdmin buyerAdmin;

    public InventoryClose(Buyer plugin){
        this.plugin = plugin;
        buyerAdmin = this.plugin.buyerAdmin;
    }

    @EventHandler
    private void onInvClosed(InventoryCloseEvent event){
        if(!(event.getInventory().getHolder() instanceof BuyerPage))
            return;

        for(int i = 0; i < event.getInventory().getSize() - 1; i++){
            if(event.getInventory().getItem(i) == null)
                continue;

            if(buyerAdmin.bannedSlots.contains(i))
                continue;

            event.getPlayer().getInventory().addItem(event.getInventory().getItem(i));
        }
    }
}
