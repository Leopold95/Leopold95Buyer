package me.leopold95.buyer.listeners;

import me.leopold95.buyer.Buyer;
import me.leopold95.buyer.core.Config;
import me.leopold95.buyer.inventories.pages.PageMain;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryClicked implements Listener {
    private Buyer plugin;

    public InventoryClicked(Buyer plugin){
        this.plugin = plugin;
    }

    @EventHandler
    private void onMainPageClicked(InventoryClickEvent event){
        if(!(event.getInventory().getHolder() instanceof PageMain))
            return;

        if(!(event.getWhoClicked() instanceof Player player))
            return;

        event.setCancelled(true);

        if(event.getCurrentItem() == null)
            return;

        ItemMeta clickedMeta = event.getCurrentItem().getItemMeta();
        if(clickedMeta.getPersistentDataContainer().has(plugin.keys.CLICKABLE_BUYER_ITEM)){
            System.out.println("it can be sold");
        }

        System.out.println(event.getCurrentItem());


        //prevent only page clicks. inventory is allowed
//        if(event.getRawSlot() != event.getSlot())
//            event.setCancelled(true);
//
//        if(plugin.buyerAdmin.getBlockedSlots().contains(event.getSlot())) {
//            event.setCancelled(true);
//        }



    }
}
