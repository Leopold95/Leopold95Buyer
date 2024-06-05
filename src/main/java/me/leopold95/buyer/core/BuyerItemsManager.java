package me.leopold95.buyer.core;

import org.bukkit.Material;
import org.bukkit.entity.Player;
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

    public ItemStack createSoldAll(Player player){
        ItemStack item = new ItemStack(Material.REDSTONE_TORCH);
        ItemMeta meta = item.getItemMeta();

        try {
            item.setType(Material.getMaterial(Config.getString("sold-all-item-type")));
            meta.setDisplayName(Config.getString("sold-all-item-name"));
            meta.setLore(Config.getStringList("sold-all-item-description"));
        }
        catch (Exception exp){
            exp.printStackTrace();
        }

        meta.getPersistentDataContainer().set(keys.SOLD_ADD_ITEM, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createMultiplierInfo(Player player){
        ItemStack item = new ItemStack(Material.REDSTONE_TORCH);
        ItemMeta meta = item.getItemMeta();

        try {
            double multValue = player.getPersistentDataContainer().get(keys.MULTIPLIER_INFO_ITEM, PersistentDataType.DOUBLE);
            String title = Config.getString("slot-multiplayer-item-name").replace("%value%", String.valueOf(multValue));

            item.setType(Material.getMaterial(Config.getString("slot-multiplayer-item-type")));
            meta.setDisplayName(title);
            meta.setLore(Config.getStringList("slot-multiplayer-item-description"));
        }
        catch (Exception exp){
            exp.printStackTrace();
        }

        meta.getPersistentDataContainer().set(keys.MULTIPLIER_INFO_ITEM, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        return item;
    }


}
