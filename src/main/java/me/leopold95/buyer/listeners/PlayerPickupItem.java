package me.leopold95.buyer.listeners;

import me.leopold95.buyer.Buyer;
import me.leopold95.buyer.core.Keys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import javax.swing.text.html.parser.Entity;

public class PlayerPickupItem implements Listener {

    private Buyer plugin;

    public PlayerPickupItem(Buyer keys){
        this.plugin = keys;
    }

    @EventHandler
    private void onPlayerPickUpItem(EntityPickupItemEvent event){
        if(!(event instanceof Player player))
            return;

        if(!player.getPersistentDataContainer().has(plugin.keys.PLAYER_AUTO_SELL_ENABLED))
            return;

        if(!plugin.buyerAdmin.soldRange.forSaleItemsTypes.contains(event.getItem().getType()))
            return;


        //plugin.buyerAdmin.soldRange.
    }
}
