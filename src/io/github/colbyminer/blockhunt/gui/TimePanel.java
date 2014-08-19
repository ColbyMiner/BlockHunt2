package io.github.colbyminer.blockhunt.gui;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.colbyminer.blockhunt.BlockHunt;

public class TimePanel extends Panel {

	BlockHunt plugin = null;
	String displayName = null;
	String configPath = null;
	
	public TimePanel(String displayName, String configPath) {
		this.plugin = BlockHunt.plugin;
		this.displayName = displayName;
		this.configPath = configPath;
		
		buttons.put(0, new PanelButton(Material.PAPER, this.displayName));
		buttons.put(1, new PanelButton(Material.GLASS_BOTTLE, 0, "Hours", "hours"));
		buttons.put(2, new PanelButton(Material.POTION, 0, "Minutes", "minutes"));
		buttons.put(3, new PanelButton(Material.POTION, 0, "Seconds", "seconds"));
	}
	
	/*
	 * Opens a new configuration dialog for the specified player.
	 */
	public void open(Player p) {

		int length = Math.min(this.displayName.length(), 28);
		String title = "§7§l" + this.displayName.substring(0, length);

		Inventory panel = Bukkit.createInventory(null, 54, title);

		for (Entry<Integer, PanelButton> entry : buttons.entrySet()) {
			Integer slot = entry.getKey();
			PanelButton button = entry.getValue();

			ItemStack item = button.getItemStack();
			// int amount = this.config.getIntValue(button.configName);

			// item.setAmount(amount);

			panel.setItem(slot, item);
		}

		BlockHunt.plugin.openDialogs.put(panel, this);
		p.openInventory(panel);
	}
}
