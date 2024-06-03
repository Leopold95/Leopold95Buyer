package me.leopold95.buyer.core;

import me.leopold95.buyer.Buyer;
import org.bukkit.NamespacedKey;

public class Keys {
    private Buyer plugin;

    public final NamespacedKey CLICKABLE_BUYER_ITEM;
    public final NamespacedKey CLICKABLE_BUYER_ITEM_BASE_COST;

    public Keys(Buyer plugin){
        this.plugin = plugin;
        CLICKABLE_BUYER_ITEM = new NamespacedKey(this.plugin, "CLICKABLE_BUYER_ITEM");
        CLICKABLE_BUYER_ITEM_BASE_COST = new NamespacedKey(this.plugin, "CLICKABLE_BUYER_ITEM_BASE_COST");
    }
}
