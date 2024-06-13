package me.leopold95.buyer.listeners;

import me.leopold95.buyer.Buyer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

public class PlayerJoin implements Listener {
    private Buyer buyer;
    public PlayerJoin(Buyer buyer){
        this.buyer = buyer;
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event){
        if(!event.getPlayer().getPersistentDataContainer().has(buyer.keys.BUYER_MULTIPLAYER, PersistentDataType.INTEGER))
            event.getPlayer().getPersistentDataContainer().set(buyer.keys.BUYER_MULTIPLAYER, PersistentDataType.DOUBLE, 0.0);
    }
}
