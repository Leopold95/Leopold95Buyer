package me.leopold95.buyer.utils;

import org.bukkit.inventory.ItemStack;

public class ItemCostPair {
    public ItemStack item;
    public double cost;
    public ItemCostPair(ItemStack item, double cost){
        this.item = item;
        this.cost = cost;
    }
}
