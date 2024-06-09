package me.leopold95.buyer.core;

import me.leopold95.buyer.Buyer;
import me.leopold95.buyer.utils.ItemCostPair;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для работы с ассортиментом доступных на продаже вещей у скупщика
 */
public class BuyerSoldRange {
    public Map<ItemStack, Double> forSaleItems;
    public List<Material> forSaleItemsTypes;

    private final String ITEMS_LIST = "items-range-list";
    private final String ITEMS_COSTS = "items-cost-list";

    private File filee;
    private FileConfiguration config;

    private  Buyer plugin;
    public BuyerSoldRange(Buyer plugin){
        this.plugin = plugin;
        createItemsConfig("avaliable-items.yml", this.plugin);
        forSaleItems = getBuyerItemsForSale();
    }

    /**
     * Добавление премета продажи в список
     * @param item
     * @param cost
     * @param admin
     * @throws IOException
     */
    public void addItem(ItemStack item, double cost, Player admin) throws IOException {
        List<ItemStack> items = getItems();

        if(items.contains(item)){
            admin.sendMessage(Config.getMessage("add-item-exists"));
            return;
        }

        if(item.getType().equals(Material.AIR) || item == null){
            admin.sendMessage(Config.getMessage("add-item-air"));
            return;
        }

        List<Double> itemPercents = getCosts();

        items.add(item);
        itemPercents.add(cost);

        saveItems(items, itemPercents);

        String saveStatusMessage = Config.getMessage("add-item-saved")
                .replace("%itemcost%", String.valueOf(cost));

        admin.sendMessage(saveStatusMessage);
    }

    /**
     * Удаление промета продажи со списка
     * @param item
     * @param admin
     * @throws IOException
     */
    public void removeItem(ItemStack item, Player admin) throws IOException {
        List<ItemStack> items = getItems();

        if(item.getType().equals(Material.AIR) || item == null){
            admin.sendMessage(Config.getMessage("remove-event-item-air"));
            return;
        }

        if(items.contains(item)){
            List<Double> itemCosts = getCosts();

            List<ItemStack> itemsCopy = new ArrayList<>();
            List<Double> costsCopy = new ArrayList<>();

            for(int i = 0; i < items.size(); i++){
                //add item to temp updated list if
                if(!items.get(i).equals(item)){
                    itemsCopy.add(items.get(i));
                    costsCopy.add(itemCosts.get(i));
                }
            }

            //save items config and save percents map section
            saveItems(itemsCopy, costsCopy);

            admin.sendMessage(Config.getMessage("item-removed"));
        }
        else {
            admin.sendMessage(Config.getMessage("remove-item-bad-contains"));
        }
    }

    private Map<ItemStack, Double> getBuyerItemsForSale(){
        Map<ItemStack, Double> pairs = new HashMap<>();

        List<ItemStack> items = getItems();
        List<Double> costs = getCosts();

        forSaleItemsTypes = new ArrayList<>();

        for (int i = 0; i < items.size(); i++){
            pairs.put(items.get(i), costs.get(i));
            forSaleItemsTypes.add(items.get(i).getType());
        }


        return pairs;
    }

    /**
     * Получить список предметов скупщика для продажи и цен из конфига
     */
//    private List<ItemCostPair> getBuyerItemsForSale(){
//        List<ItemCostPair> itemsCosts = new ArrayList<>();
//
//        List<ItemStack> items = getItems();
//        List<Double> costs = getCosts();
//
//        for (int i = 0; i < items.size(); i++){
//            itemsCosts.add(new ItemCostPair(items.get(i), costs.get(i)));
//        }
//
//        return itemsCosts;
//    }

    /**
     * Получить список предметов для продажи скупщика из конфига
     * @return
     */
    private List<ItemStack> getItems() {
        ConfigurationSection section = config.getConfigurationSection(ITEMS_LIST);
        List<ItemStack> items = new ArrayList<>();

        if (section == null) {
            return items; // Return an empty list if no items were saved
        }

        for (String key : section.getKeys(false)) {
            ItemStack item = section.getItemStack(key);
            if (item != null) {
                items.add(item);
            }
        }

        return items;
    }


    /**
     * Список всех цен на предметы из конфига
     * @return
     */
    private List<Double> getCosts() {
        ConfigurationSection section = config.getConfigurationSection(ITEMS_COSTS);
        List<Double> costs = new ArrayList<>();

        if (section == null)
            return costs;

        for (String key : section.getKeys(false))
            costs.add(section.getDouble(key));

        return costs;
    }

    /**
     * Сохранить указанные педметы и цены в конфиг
     * @param items
     * @param costs
     * @throws IOException
     */
    private void saveItems(List<ItemStack> items, List<Double> costs) throws IOException {
        ConfigurationSection section = config.createSection(ITEMS_LIST);
        ConfigurationSection section2 = config.createSection(ITEMS_COSTS);

        for (int i = 0; i < items.size(); i++) {
            section.set(String.valueOf(i), items.get(i));
            section2.set(String.valueOf(i), costs.get(i));
        }

        config.save(filee);
    }

    /**
     * Сохранить указанные педметы и цены в конфиг
     */
    private void saveItems(List<ItemCostPair> items) throws IOException {
        ConfigurationSection section = config.createSection(ITEMS_LIST);
        ConfigurationSection section2 = config.createSection(ITEMS_COSTS);

        for (int i = 0; i < items.size(); i++) {
            section.set(String.valueOf(i), items.get(i).item);
            section2.set(String.valueOf(i), items.get(i).cost);
        }

        config.save(filee);
    }


    /**
     * Инил=циализация конфига предметов скупщика
     * @param file
     * @param plugin
     */
    private void createItemsConfig(String file, JavaPlugin plugin) {
        filee = new File(plugin.getDataFolder(), file);
        if (!filee.exists()) {
            filee.getParentFile().mkdirs();
            plugin.saveResource(file, false);
        }
        config = YamlConfiguration.loadConfiguration(filee);
        try {
            config.save(filee);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}