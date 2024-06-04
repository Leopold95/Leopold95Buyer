package me.leopold95.buyer.core;

import me.leopold95.buyer.Buyer;
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
import java.util.List;

public class BuyerSoldRange {
    private final String ITEMS_LIST = "items-range-list";
    private final String ITEMS_COSTS = "items-cost-list";

    private File filee;
    private FileConfiguration config;

    private  Buyer plugin;
    public BuyerSoldRange(Buyer plugin){
        this.plugin = plugin;
        createItemsConfig("avaliable-items.yml", this.plugin);
    }

    public void addItem(ItemStack item, double cost, Player admin) throws IOException {
        List<ItemStack> items = loadItems();

        String saveStatusMessage = Config.getMessage("add-item-saved-result");

        if(items.contains(item)){
            admin.sendMessage(Config.getMessage("add-item-exists"));
            return;
        }

        if(item.getType().equals(Material.AIR) || item == null){
            admin.sendMessage(Config.getMessage("add-item-air"));
            return;
        }

        List<Double> itemPercents = loadCosts();

        items.add(item);
        itemPercents.add(cost);

        saveItems(items, itemPercents);

        admin.sendMessage(Config.getMessage("add-item-saved").replace("%itemcost%", String.valueOf(cost)));
    }

    public void removeItem(ItemStack item, Player admin) throws IOException {
        List<ItemStack> items = loadItems();

        if(item.getType().equals(Material.AIR) || item == null){
            admin.sendMessage(Config.getMessage("remove-event-item-air"));
            return;
        }

        if(items.contains(item)){
            List<Double> itemCosts = loadCosts();

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

            admin.sendMessage(Config.getMessage("event-item-removed"));
        }
        else {
            admin.sendMessage(Config.getMessage("remove-event-item-bad"));
        }
    }

    public int itemsCount(){
        return loadItems().size();
    }

    private List<ItemStack> loadItems() {
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

    private List<Double> loadCosts() {
        ConfigurationSection section = config.getConfigurationSection(ITEMS_COSTS);
        List<Double> costs = new ArrayList<>();

        if (section == null)
            return costs;

        for (String key : section.getKeys(false))
            costs.add(section.getDouble(key));

        return costs;
    }

    private void saveItems(List<ItemStack> items, List<Double> costs) throws IOException {
        ConfigurationSection section = config.createSection(ITEMS_LIST);
        ConfigurationSection section2 = config.createSection(ITEMS_COSTS);

        for (int i = 0; i < items.size(); i++) {
            section.set(String.valueOf(i), items.get(i));
            section2.set(String.valueOf(i), costs.get(i));
        }

        config.save(filee);
    }


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