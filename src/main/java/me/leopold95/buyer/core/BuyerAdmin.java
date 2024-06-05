package me.leopold95.buyer.core;

import me.leopold95.buyer.Buyer;
import me.leopold95.buyer.enums.PermissionsList;
import me.leopold95.buyer.inventories.BuyerInventories;
import me.leopold95.buyer.inventories.pages.PageMain;
import me.leopold95.buyer.utils.ItemCostPair;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyerAdmin {
    //public final PageMain pageMain;

    private File bConfigFile;
    private FileConfiguration bConfig;

    private Buyer plugin;
    private BuyerInventories inventories;
    private BuyerItemsManager buyerItemsManager;
    public BuyerSoldRange soldRange;

    public BuyerAdmin(Buyer plugin){
        this.plugin = plugin;
        createMessagesConfig("buyer-design.yml", this.plugin);
        //inventories = new BuyerInventories(this.plugin);
        //pageMain = new PageMain();
        buyerItemsManager = new BuyerItemsManager(this.plugin.keys);
        soldRange = new BuyerSoldRange(this.plugin);
        //crateDesign();

        //pageMain.getInventory().setItem(30, buyerItemsManager.createItem(Material.ACACIA_DOOR));
    }

    /**
     * Отскрыть странцу инвентаря у игрока
     * @param player
     * @param page
     */
    public void openPage(Player player, Inventory page){
        if(!player.hasPermission(PermissionsList.BUYER_OPEN)){
            player.sendMessage(Config.getMessage("commands-permissions-bad"));
            return;
        }

        player.openInventory(page);

    }

    /**
     * Получить главную страницу скупщика
     * @return
     */
    public Inventory getPageMain(){
        Inventory inv =  new PageMain().getInventory();
        crateDesign(inv);
        createButtons(inv);
        return inv;
    }

    /**
     * Посчитать суммарную стоимость всех предметов
     * @param info
     * @return
     */
    public double calculateTotalCost(List<ItemCostPair> info){
        double total = 0;

        for(ItemCostPair pair: info){
            total += pair.cost;
        }

        return total;
    }

    /**
     * Посчитать суммарную стоимость всех предметов
     * @return
     */
    public double calculateTotalCost(List<ItemStack> itemsToSell, Map<ItemStack, Double> costs){
        double totalCost = 0.0;

        // Iterate through itemsToSell and sum up the costs if the item exists in the map
        for (ItemStack item : itemsToSell) {
            if (costs.containsKey(item)) {
                totalCost += costs.get(item);
            }
        }

        return totalCost;
    }

    /**
     * Список предметов, которые лежат в НЕ заблокированных слотах инвентаря
     * @param blockedSlots
     * @param inv
     * @return
     */
    public List<ItemStack> getItemsShouldBeSold(List<Integer> blockedSlots, Inventory inv){
        List<ItemStack> itemsCosts = new ArrayList<>();

        for(int i =  0; i < inv.getSize(); i++){
            if(blockedSlots.contains(i))
                continue;

            itemsCosts.add(inv.getItem(i));
        }

        return itemsCosts;
    }

    /**
     * Список заблокированных для оазличных дестйвий слотов инвентаря скупщика(не личного)
     * @return
     */
    public List<Integer> getBlockedSlots(){
        List<Integer> slots = new ArrayList<>();

        ConfigurationSection parentSection = bConfig.getConfigurationSection("main-page-design");

        if (parentSection != null) {
            Map<String, Object> childMap = parentSection.getValues(false); // Get key-value pairs of direct children

            for (Map.Entry<String, Object> entry : childMap.entrySet()) {
                String key = entry.getKey();
                //Object value = entry.getValue();
                slots.add(Integer.parseInt(key));
            }
        }
        return slots;
    }

    public Map<String, Object> getDesignSlots() {
        Map<String, Object> childMap = new HashMap<String, Object>();
        ConfigurationSection parentSection = bConfig.getConfigurationSection("main-page-design");

        childMap = parentSection.getValues(true);

        return childMap;


    }

    /**
     * создает не кликабельный дизайн для инвентаря продажи
     * @param inv
     */
    private void crateDesign(Inventory inv){
        for(Map.Entry<String, Object> items : getDesignSlots().entrySet()){
            try {
                inv.setItem(Integer.parseInt(items.getKey()), new ItemStack(Material.getMaterial(String.valueOf(items.getValue())), 1));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * создает кнопки управления
     * @param inv
     */
    private void  createButtons(Inventory inv){
        int soldAllSlot = bConfig.getInt("sold-all-slot");
        inv.setItem(soldAllSlot, buyerItemsManager.createSoldAll());
    }

    private void createMessagesConfig(String file, JavaPlugin plugin) {
        bConfigFile = new File(plugin.getDataFolder(), file);
        if (!bConfigFile.exists()) {
            bConfigFile.getParentFile().mkdirs();
            plugin.saveResource(file, false);
        }
        bConfig = YamlConfiguration.loadConfiguration( bConfigFile);
        try {
            bConfig.save(bConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
