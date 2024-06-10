package me.leopold95.buyer.core;

import me.leopold95.buyer.Buyer;
import me.leopold95.buyer.enums.PermissionsList;
import me.leopold95.buyer.inventories.BuyerInventories;
import me.leopold95.buyer.inventories.pages.PageMain;
import me.leopold95.buyer.utils.ItemCostPair;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
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
    public MultiplierRules multiplierRules;

    public BuyerAdmin(Buyer plugin){
        this.plugin = plugin;
        createMessagesConfig("buyer-design.yml", this.plugin);
        //inventories = new BuyerInventories(this.plugin);
        //pageMain = new PageMain();
        buyerItemsManager = new BuyerItemsManager(this.plugin.keys);
        soldRange = new BuyerSoldRange(this.plugin);
        multiplierRules = new MultiplierRules(this.plugin.keys);
        //crateDesign();

        //pageMain.getInventory().setItem(30, buyerItemsManager.createItem(Material.ACACIA_DOOR));
    }

    /**
     * Удаляет проданные предметы из окна магазина
     * @param inv
     */
    public void removeSoldItems(Inventory inv, List<Integer> bannedSlots){
        List<Material> sellingItemsMaterials = new ArrayList<>();

        for(int i = 0; i < inv.getSize() - 1; i++){
            if(inv.getItem(i) == null)
                continue;

            if(bannedSlots.contains(i))
                continue;

            if(soldRange.forSaleItemsTypes.contains(inv.getItem(i).getType())){
                inv.setItem(i, null);
            }
        }
    }

    public void returnUnsoldItemsToPlayer(Inventory inv, Player player, List<Integer> bannedSlots){
        for(int i = 0; i < inv.getSize() - 1; i++){
            if(inv.getItem(i) == null)
                continue;

            if(bannedSlots.contains(i))
                continue;

            if(!player.getInventory().isEmpty())
                player.getInventory().addItem(inv.getItem(i));
            else
                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), inv.getItem(i));

            inv.setItem(i, null);
        }
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

        try{
            player.playSound(player, Sound.valueOf(Config.getString("buyer-open-sound")), 1, Config.getInt("buyer-open-sound-volume"));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        player.openInventory(page);

    }

    /**
     * Получить главную страницу скупщика
     * @return
     */
    public Inventory getPageMain(Player player){
        Inventory inv =  new PageMain().getInventory();
        crateDesign(inv);
        createButtons(inv, player);
        return inv;
    }

    /**
     * Посчитать суммарную стоимость всех предметов
     */
    public double calculateTotalCost(List<ItemStack> itemsToSell, Map<ItemStack, Double> costs){
        double totalCost = 0.0;

        // Iterate through itemsToSell and sum up the costs if the item exists in the map
        for (ItemStack item : itemsToSell) {
            if(item == null)
                continue;

            //TODO make this shit normal
            ItemStack searchableCopy = item.clone();
            searchableCopy.setAmount(1);

            if (costs.containsKey(searchableCopy)) {
                totalCost += costs.get(searchableCopy) * item.getAmount();
            }
        }

        return totalCost;
    }

    /**
     * Считает общее количево всех предметов для продажи
     * @param itemsToSell
     * @return
     */
    public int soldItemsAmount(List<ItemStack> itemsToSell){
        int sum = 0;

        for (ItemStack item : itemsToSell) {
            if(item == null)
                continue;

            sum += item.getAmount();
        }

        return sum;
    }

    /**
     * Список предметов, которые лежат в НЕ заблокированных слотах инвентаря (предметы на продажу)
     */
    public List<ItemStack> getItemsShouldBeSold(List<Integer> blockedSlots, Inventory inv){
        List<ItemStack> itemsCosts = new ArrayList<>();

        for(int i = 0; i < inv.getSize() - 1; i++){
            if(blockedSlots.contains(i))
                continue;

            itemsCosts.add(inv.getItem(i));
        }

        return itemsCosts;
    }

    /**
     * Список заблокированных для различных дестйвий слотов инвентаря скупщика
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

            slots.add(bConfig.getInt("sold-all-slot"));
            slots.add(bConfig.getInt("slot-multiplayer-info"));

        }
        return slots;
    }

    public Map<String, Object> getDesignSlots() {
        Map<String, Object> childMap = new HashMap<String, Object>();
        ConfigurationSection parentSection = bConfig.getConfigurationSection("main-page-design");

        childMap = parentSection.getValues(true);

        return childMap;


    }

    public void playSoldSound(Player player){
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
    }

    public void depositMoney(Player player, double amount){
        EconomyResponse r = plugin.economy.depositPlayer(player, amount);

        if(r.transactionSuccess()) {
            String pickedMessage = Config.getMessage("item-click-sold-all-ok")
                    .replace("%cost%", String.valueOf(plugin.economy.format(r.amount)));
            player.sendMessage(pickedMessage);

        } else {
            Bukkit.getConsoleSender().sendMessage(Config.getMessage("item-click-sold-all-money-bad"));
        }
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
    private void  createButtons(Inventory inv, Player player){
        inv.setItem(bConfig.getInt("sold-all-slot"), buyerItemsManager.createSoldAll(player));
        inv.setItem(bConfig.getInt("slot-multiplayer-info"), buyerItemsManager.createMultiplierInfo(player));
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
