package me.leopold95.buyer.core;

import me.leopold95.buyer.Buyer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuyerAdmin {
    private File bConfigFile;
    private FileConfiguration bConfig;

    private Buyer plugin;

    public BuyerAdmin(Buyer plugin){
        this.plugin = plugin;
    }

    public void addBuyerItem(ItemStack item){

    }

    public void removeBuyerItem(ItemStack item){

    }

    public List<Integer> getBlockedSlots(){
        List<Integer> slots = new ArrayList<>();

        ConfigurationSection parentSection = bConfig.getConfigurationSection("parent-section-name");

        if (parentSection != null) {
            Map<String, Object> childMap = parentSection.getValues(false); // Get key-value pairs of direct children

            for (Map.Entry<String, Object> entry : childMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                slots.add(Integer.parseInt(key));
            }
        }
        return slots;
    }

    private void register(JavaPlugin plugin) {
        createMessagesConfig("buyer-items.yml", plugin);
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
