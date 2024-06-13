package me.leopold95.buyer.listeners;

import me.leopold95.buyer.Buyer;
import me.leopold95.buyer.core.BuyerAdmin;
import me.leopold95.buyer.core.Config;
import me.leopold95.buyer.core.SoundPlayer;
import me.leopold95.buyer.enums.PermissionsList;
import me.leopold95.buyer.inventories.BuyerPage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class InventoryClicked implements Listener {
    private Buyer plugin;
    private BuyerAdmin buyerAdmin;

    public InventoryClicked(Buyer plugin){
        this.plugin = plugin;
        buyerAdmin = this.plugin.buyerAdmin;
    }

    @EventHandler
    private void onMainPageClicked(InventoryClickEvent event){
        if(!(event.getInventory().getHolder() instanceof BuyerPage))
            return;

        if(!(event.getWhoClicked() instanceof Player))
            return;

        if(event.getCurrentItem() == null)
            return;

        //только клики в меню сундука
        if(event.getSlot() != event.getRawSlot())
            return;

        Player player = (Player) event.getWhoClicked();

        //System.out.println(plugin.buyerAdmin.getBlockedSlots());

        //блокировка действий при нажатии на кнопки интерфнйса
        if(buyerAdmin.bannedSlots.contains(event.getSlot()))
            event.setCancelled(true);

        if(event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(plugin.keys.MULTIPLIER_INFO_ITEM, PersistentDataType.INTEGER))
            event.setCancelled(true);

        if(event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(plugin.keys.ITEM_AUTO_SELL, PersistentDataType.INTEGER)){
            event.setCancelled(true);

            SoundPlayer.tryPlaySound(player, "slot-auto-sell-pressed-sound", "slot-auto-sell-pressed-volume");
            //buyerAdmin.playAutoSellSound(player);

            if(!player.getPersistentDataContainer().has(plugin.keys.PLAYER_AUTO_SELL_ENABLED, PersistentDataType.INTEGER)){
                player.getPersistentDataContainer().set(plugin.keys.PLAYER_AUTO_SELL_ENABLED, PersistentDataType.INTEGER, 1);
                event.getInventory().setItem(event.getRawSlot(), plugin.buyerAdmin.buyerItemsManager.createAutoSellInfo(player));
            }
            else {
                player.getPersistentDataContainer().remove(plugin.keys.PLAYER_AUTO_SELL_ENABLED);
                event.getInventory().setItem(event.getRawSlot(), buyerAdmin.buyerItemsManager.createAutoSellInfo(player));
            }
        }


        if(event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(plugin.keys.SOLD_ADD_ITEM, PersistentDataType.INTEGER)){
            event.setCancelled(true);

            if(!player.hasPermission(PermissionsList.BUYER_SELL)){
                player.sendMessage(Config.getMessage("sell-permission"));
                return;
            }

            Bukkit.getScheduler().runTask(plugin, () -> {
                //получает список преметов, в слотах которые нужно продать
                List<ItemStack> itemsToSell = buyerAdmin.getItemsShouldBeSold(buyerAdmin.bannedSlots, event.getInventory());

                //глобальная цена всех предметов, которые можно продать
                double totalCost = buyerAdmin.calculateTotalCost(itemsToSell, buyerAdmin.soldRange.forSaleItems);

                if(totalCost == 0){
                    player.sendMessage(Config.getMessage("cant-sold-nothing"));
                    buyerAdmin.returnUnsoldItemsToPlayer(event.getInventory(), player, buyerAdmin.bannedSlots);
                    return;
                }

                //общее количесво предметов для продажи
                int soldItemsAmount = buyerAdmin.soldItemsAmount(itemsToSell);

                //добавить игроку множитель продажи основываясь на колчестве проданных предметов
                buyerAdmin.multiplierRules.add(player, soldItemsAmount);

                //попвтаться начилить игроку деньги, вырученные с продажи
                buyerAdmin.depositMoney(player, totalCost);

                SoundPlayer.tryPlaySound(player, "sold-all-pressed-sound", "sold-all-pressed-volume");
                //buyerAdmin.playSoldSound(player);

                //удалить проданные вещи
                buyerAdmin.removeSoldItems(event.getInventory(), buyerAdmin.bannedSlots);

                buyerAdmin.returnUnsoldItemsToPlayer(event.getInventory(), player, buyerAdmin.bannedSlots);
            });
        }
    }
}
