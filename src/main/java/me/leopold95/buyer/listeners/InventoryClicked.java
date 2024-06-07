package me.leopold95.buyer.listeners;

import me.leopold95.buyer.Buyer;
import me.leopold95.buyer.core.Config;
import me.leopold95.buyer.inventories.pages.PageMain;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

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

        if(event.getCurrentItem() == null)
            return;

        //только клики в меню сундука
        if(event.getSlot() != event.getRawSlot())
            return;

        //System.out.println(plugin.buyerAdmin.getBlockedSlots());

        List<Integer> bannedSlots = plugin.buyerAdmin.getBlockedSlots();
        //блокировка действий при нажатии на кнопки интерфнйса
        if(bannedSlots.contains(event.getSlot()))
            event.setCancelled(true);

        if(event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(plugin.keys.MULTIPLIER_INFO_ITEM))
            event.setCancelled(true);

        if(event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(plugin.keys.SOLD_ADD_ITEM)){
            event.setCancelled(true);

            List<ItemStack> itemsToSell = plugin.buyerAdmin.getItemsShouldBeSold(bannedSlots, event.getInventory());

            if(itemsToSell.isEmpty()){
                player.sendMessage(Config.getMessage("cant-sold-nothing"));
                return;
            }

            double total = plugin.buyerAdmin.calculateTotalCost(itemsToSell, plugin.buyerAdmin.soldRange.forSaleItems);
            int soldItemsAmount = plugin.buyerAdmin.soldItemsAmount(itemsToSell);

            plugin.buyerAdmin.multiplierRules.add(player, soldItemsAmount);

            EconomyResponse r = plugin.economy.depositPlayer(player, total);

            if(r.transactionSuccess()) {
                String pickedMessage = Config.getMessage("item-click-sold-all-ok")
                        .replace("%cost%", String.valueOf(plugin.economy.format(r.amount)));
                player.sendMessage(pickedMessage);

            } else {
                Bukkit.getConsoleSender().sendMessage(Config.getMessage("item-click-sold-all-money-bad"));
            }

            try {
                player.playSound(player, Sound.valueOf(
                        Config.getString("sold-all-pressed-sound")),
                        Config.getInt("sold-all-pressed-volume"),
                        1f);
            }
            catch (Exception ep) {
                String message = Config.getMessage("item-click-sold-all-sound-bad")
                        .replace("%sound%", Config.getString("sold-all-pressed-sound"));
                Bukkit.getConsoleSender().sendMessage(message);
            }


            //event.getInventory().close();
        }


        //prevent only page clicks. inventory is allowed
//        if(event.getRawSlot() != event.getSlot())
//            event.setCancelled(true);
//
//        if(plugin.buyerAdmin.getBlockedSlots().contains(event.getSlot())) {
//            event.setCancelled(true);
//        }



    }
}
