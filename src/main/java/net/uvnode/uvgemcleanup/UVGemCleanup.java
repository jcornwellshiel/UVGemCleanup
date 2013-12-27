/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.uvnode.uvgemcleanup;

import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author jcornwell
 */
public final class UVGemCleanup extends JavaPlugin implements Listener {
    List<Integer> _materials;
    List<String> _keywords;

    @Override
    public void onEnable() {
        saveDefaultConfig(); // Write the default configs to file if it doesn't exist.
        getServer().getPluginManager().registerEvents(this, this); // Register for events.
        _materials = getConfig().getIntegerList("materialIds"); // Load the material list
        _keywords = getConfig().getStringList("nameKeywords"); // Load the keyword list
        
    }
    
    @Override
    public void onDisable() {
        
    }
    
    @EventHandler
    private void onInventoryOpenEvent(InventoryOpenEvent event) {
        checkInventory(event.getInventory());
        
    }

    private void checkInventory(Inventory inventory) {
        for(Integer materialId : _materials) {
            Material material = Material.getMaterial(materialId);
//            getLogger().info("Checking for " + material.name());
            if (inventory.contains(material)) {
//                getLogger().info("Found " + material.name());
                Map<Integer, ItemStack> itemstacks = (Map<Integer, ItemStack>) inventory.all(material);
                for(Map.Entry<Integer, ItemStack> stack : itemstacks.entrySet()) {
//                    getLogger().info("Checking slot " + stack.getKey().toString());
                    if (stack.getValue().hasItemMeta() && stack.getValue().getItemMeta().hasDisplayName()) {
                        String name = stack.getValue().getItemMeta().getDisplayName();
//                        getLogger().info("Has a name: " + name);
                        for (String keyword : _keywords) {
//                            getLogger().info("Checking name for keyword " + keyword);
                            if (name.contains(keyword)) {
//                                getLogger().info("Found " + keyword);
                                inventory.remove(stack.getValue());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
