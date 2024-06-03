package me.leopold95.buyer.core;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class BuyerItemsManager {
    private Keys keys;
    public BuyerItemsManager(Keys keys){
        this.keys = keys;
    }

    public ArrayList<ItemStack> getSellableItems(){
        return new ArrayList<>();
    }

    public ItemStack createItem(Material type){
        ItemStack item =  new ItemStack(type);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(keys.CLICKABLE_BUYER_ITEM, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        return item;
    }
}
