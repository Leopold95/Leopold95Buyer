package me.leopold95.buyer.core;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class BuyerButtons {
    private Keys keys;
    public BuyerButtons(Keys keys){
        this.keys = keys;
    }

    public ItemStack createSoldAll(double totalCost){
        ItemStack item = new ItemStack(Material.REDSTONE_TORCH);
        ItemMeta meta = item.getItemMeta();

        try {
            item.setType(Material.getMaterial(Config.getString("sold-all-item-type")));
            meta.setDisplayName(Config.getString("sold-all-item-name").replace("%cost%", String.valueOf(totalCost)));
            meta.setLore(Config.getStringList("sold-all-item-description"));
        }
        catch (Exception exp){
            exp.printStackTrace();
        }

        meta.getPersistentDataContainer().set(keys.SOLD_ADD_ITEM, PersistentDataType.INTEGER, 1);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createSoldAll(){
        ItemStack item = new ItemStack(Material.REDSTONE_TORCH);
        ItemMeta meta = item.getItemMeta();

        try {
            item.setType(Material.getMaterial(Config.getString("sold-all-item-type")));
            meta.setDisplayName(Config.getString("sold-all-item-name").replace("%cost%", ""));
            meta.setLore(Config.getStringList("sold-all-item-description"));
        }
        catch (Exception exp){
            exp.printStackTrace();
        }

        meta.getPersistentDataContainer().set(keys.SOLD_ADD_ITEM, PersistentDataType.INTEGER, 1);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createMultiplierInfo(Player player){
        ItemStack item = new ItemStack(Material.REDSTONE_TORCH);
        ItemMeta meta = item.getItemMeta();

        try {
            double multValue = player.getPersistentDataContainer().get(keys.BUYER_MULTIPLAYER, PersistentDataType.DOUBLE);
            String title = Config.getString("slot-multiplayer-item-name").replace("%value%", String.valueOf(multValue));

            item.setType(Material.getMaterial(Config.getString("slot-multiplayer-item-type")));
            meta.setDisplayName(title);
            meta.setLore(Config.getStringList("slot-multiplayer-item-description"));
        }
        catch (Exception exp){
            exp.printStackTrace();
        }

        meta.getPersistentDataContainer().set(keys.MULTIPLIER_INFO_ITEM, PersistentDataType.INTEGER, 1);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createAutoSellInfo(Player player){
        ItemStack item = new ItemStack(Material.REDSTONE_TORCH);
        ItemMeta meta = item.getItemMeta();

        try {
            String state = "";

            if(player.getPersistentDataContainer().has(keys.PLAYER_AUTO_SELL_ENABLED, PersistentDataType.INTEGER)){
                state = Config.getMessage("part.enabled");
            }
            else {
                state = Config.getMessage("part.disabled");
            }

            String title = Config.getString("slot-auto-sell-item-name").replace("%value%", state);

            item.setType(Material.getMaterial(Config.getString("slot-auto-sell-item-type")));
            meta.setDisplayName(title);
            meta.setLore(Config.getStringList("slot-auto-sell-item-description"));
        }
        catch (Exception exp){
            exp.printStackTrace();
        }

        meta.getPersistentDataContainer().set(keys.ITEM_AUTO_SELL, PersistentDataType.INTEGER, 1);
        item.setItemMeta(meta);
        return item;
    }


}
