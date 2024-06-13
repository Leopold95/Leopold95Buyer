package me.leopold95.buyer.core;

import me.leopold95.buyer.Buyer;
import me.leopold95.buyer.abstraction.RepitingTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoBuyerUpdater extends RepitingTask {
    private Buyer plugin;
    private BuyerAdmin buyerAdmin;

    public AutoBuyerUpdater(Buyer plugin) {
        super(plugin, Config.getInt("auto-buyer-update-ticks"));
        this.plugin = plugin;
        buyerAdmin = this.plugin.buyerAdmin;
    }

    @Override
    public void run() {
        for(Player player: Bukkit.getOnlinePlayers()){
            if(!player.getPersistentDataContainer().has(plugin.keys.PLAYER_AUTO_SELL_ENABLED, PersistentDataType.INTEGER))
                return;

            for(int slot = 0; slot < player.getInventory().getSize(); slot++){
                if(player.getInventory().getItem(slot) == null)
                    continue;

                ItemStack item = player.getInventory().getItem(slot);

                if(buyerAdmin.soldRange.forSaleItemsTypes.contains(item.getType())){
                    double cost = buyerAdmin.calculateTotalCost(item, buyerAdmin.soldRange.forSaleItems);
                    buyerAdmin.multiplierRules.add(player, item.getAmount());
                    buyerAdmin.depositMoney(player, cost);
                    player.getInventory().setItem(slot, null);
                }
            }

//            for (ItemStack item: player.getInventory()){
//                if(item == null)
//                    continue;
//
//
//                if(buyerAdmin.soldRange.forSaleItemsTypes.contains(item.getType())){
//                    double cost = buyerAdmin.calculateTotalCost(item, buyerAdmin.soldRange.forSaleItems);
//                    buyerAdmin.multiplierRules.add(player, item.getAmount());
//                    buyerAdmin.depositMoney(player, cost);
//                    player.getInventory().setItem();
//                }
//            }
        }
    }
}
