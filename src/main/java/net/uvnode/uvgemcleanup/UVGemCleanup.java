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
    private void onEntityDeathEvent(InventoryOpenEvent event) {
        for(Integer material : _materials) {
            if (event.getInventory().contains(Material.getMaterial(material))) {
                Map<Integer, ItemStack> itemstacks = (Map<Integer, ItemStack>) event.getInventory().all(Material.getMaterial(material));
                for(Map.Entry<Integer, ItemStack> stack : itemstacks.entrySet()) {
                    if (stack.getValue().hasItemMeta() && stack.getValue().getItemMeta().hasDisplayName()) {
                        String name = stack.getValue().getItemMeta().getDisplayName();
                        for (String keyword : _keywords) {
                            if (name.contains(keyword)) {
                                stack.getValue().setAmount(0);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
