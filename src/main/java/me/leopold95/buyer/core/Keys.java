package me.leopold95.buyer.core;

import me.leopold95.buyer.Buyer;
import org.bukkit.NamespacedKey;

public class Keys {
    private Buyer plugin;

    public final NamespacedKey CLICKABLE_BUYER_ITEM;
    public final NamespacedKey CLICKABLE_BUYER_ITEM_BASE_COST;

    public final NamespacedKey SOLD_ADD_ITEM;
    public final NamespacedKey MULTIPLIER_INFO_ITEM;

    public final NamespacedKey BUYER_MULTIPLAYER;

    public final NamespacedKey PLAYER_AUTO_SELL_ENABLED;

    public Keys(Buyer plugin){
        this.plugin = plugin;
        CLICKABLE_BUYER_ITEM = new NamespacedKey(this.plugin, "CLICKABLE_BUYER_ITEM");
        CLICKABLE_BUYER_ITEM_BASE_COST = new NamespacedKey(this.plugin, "CLICKABLE_BUYER_ITEM_BASE_COST");

        SOLD_ADD_ITEM = new NamespacedKey(this.plugin, "SOLD_ADD_ITEM");
        MULTIPLIER_INFO_ITEM = new NamespacedKey(this.plugin, "MULTIPLIER_INFO_ITEM");

        BUYER_MULTIPLAYER = new NamespacedKey(this.plugin, "BUYER_MULTIPLAYER");

        PLAYER_AUTO_SELL_ENABLED = new NamespacedKey(this.plugin, "PLAYER_AUTO_SELL_ENABLED");
    }
}
