package io.github.colbyminer.blockhunt.gui;

import io.github.colbyminer.blockhunt.ArenaConfig;
import io.github.colbyminer.blockhunt.BlockHunt;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/*
 * Configuration panel used for setting up the arena configuration.
 *   The panel consist of buttons, that when pressed, directly modify the config state.
 */
public class ConfigPanel extends Panel {

	ArenaConfig config = null;
	
	public ConfigPanel(ArenaConfig config) {
		this.config = config;
		buttons.put(0, new PanelButton(Material.PAPER, "Arena: " + config.name));
		buttons.put(1, new PanelButton(Material.SKULL_ITEM, 3, "Min Players", "minPlayers", 0, 64));
		buttons.put(2, new PanelButton(Material.SKULL_ITEM, 3, "Max Players", "maxPlayers", 0, 64));
	}
	
	/*
	 * Opens a new configuration dialog for the specified player.
	 */
	public void open(Player p) {
		
        int length = Math.min(config.name.length(), 28);
		String title = "§6§l" + config.name.substring(0, length);
		
		Inventory dialog = Bukkit.createInventory(null, 54, title);
		
		for (Entry<Integer, PanelButton> entry : buttons.entrySet()) {
		    Integer slot = entry.getKey();
		    PanelButton button = entry.getValue();
		    
		    ItemStack item = button.getItemStack();
		    int amount = this.config.getIntValue(button.configName);
		    
		    item.setAmount(amount);
		    
		    dialog.setItem(slot, item);
		}
		
		BlockHunt.plugin.openDialogs.put(dialog, this);
		p.openInventory(dialog);
	}
	
	public void onInventoryClickEvent(final InventoryClickEvent e) {
		// Does this slot contain a config button?
		if (buttons.containsKey(e.getRawSlot())) {
			
			e.setCancelled(true);
			
			PanelButton button = buttons.get(e.getRawSlot());
			
			int amount = this.config.getIntValue(button.configName);
			
			if (e.isLeftClick()) {
				if (e.isShiftClick()) {
					amount += 10;
				} else {
					amount++;
				}
			} else if (e.isRightClick()) {
				if (e.isShiftClick()) {
					amount -= 10;
				} else {
					amount--;
				}
			}
			
			// clamp amount to range.
			amount = Math.max(button.min, amount);
			amount = Math.min(button.max, amount);
			
			this.config.setIntValue(button.configName, amount);
			
			ItemStack item = e.getInventory().getItem(e.getRawSlot());
			item.setAmount(amount);
			
		}
	}
}
