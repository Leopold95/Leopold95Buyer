package me.leopold95.buyer.core;

import me.leopold95.buyer.Buyer;
import me.leopold95.buyer.enums.PermissionsList;
import me.leopold95.buyer.inventories.BuyerInventories;
import me.leopold95.buyer.inventories.pages.PageMain;
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
    public final PageMain pageMain;

    private File bConfigFile;
    private FileConfiguration bConfig;

    private Buyer plugin;
    private BuyerInventories inventories;

    private BuyerItemsManager buyerItemsManager;

    public BuyerAdmin(Buyer plugin){
        this.plugin = plugin;
        createMessagesConfig("buyer-items.yml", this.plugin);
        //inventories = new BuyerInventories(this.plugin);
        pageMain = new PageMain();
        buyerItemsManager = new BuyerItemsManager(this.plugin.keys);
        crateDesign();

        pageMain.getInventory().setItem(30, buyerItemsManager.createItem(Material.ACACIA_DOOR));
    }

    public void openPage(Player player, Inventory page){
        if(!player.hasPermission(PermissionsList.BUYER_OPEN)){
            player.sendMessage(Config.getMessage("commands-permissions-bad"));
            return;
        }

        player.openInventory(page);
    }

    private void crateDesign(){
        for(Map.Entry<String, Object> items : getDesignSlots().entrySet()){
            try {
                pageMain.getInventory().setItem(Integer.parseInt(items.getKey()), new ItemStack(Material.getMaterial(String.valueOf(items.getValue())), 1));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void addBuyerItem(ItemStack item){

    }

    public void removeBuyerItem(ItemStack item){

    }

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


    public Map<String, Object> getDesignSlots(){
        Map<String, Object> childMap = new HashMap<String, Object>();
        ConfigurationSection parentSection = bConfig.getConfigurationSection("main-page-design");

        childMap = parentSection.getValues(true);

        return childMap;
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
