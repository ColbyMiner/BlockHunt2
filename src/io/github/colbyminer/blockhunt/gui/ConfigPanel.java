package io.github.colbyminer.blockhunt.gui;

import io.github.colbyminer.blockhunt.ArenaConfig;
import io.github.colbyminer.blockhunt.BlockHunt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/*
 * Configuration panel used for setting up the arena configuration.
 *   The panel consist of buttons, that when pressed, directly modify the config state.
 */
public class ConfigPanel extends Panel {

	ArenaConfig config = null;
	
	/*
	 * TimeButton is used for timer settings such as game-time, seeker-start-time, etc.
	 */
	public class TimeButton extends PanelButton {
		
		ArenaConfig config = null;
		
		TimeButton(ArenaConfig config, String displayName, String configName) {
			super(Material.COMPASS, 0, displayName, configName);
			this.config = config;
			
			ItemStack item = getItemStack();
			String time = this.config.getTimeValue(configName);
			ItemMeta meta = item.getItemMeta();

			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.translateAlternateColorCodes('&', "&7Time: &6" + time));
			lore.add("");
			lore.add(ChatColor.translateAlternateColorCodes('&', "&eClick to change time."));
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
		
		public void onClick(final InventoryClickEvent e) {			
			e.setCancelled(true);
			
			final ArenaConfig config = this.config;
			final String name = this.displayName;
			final String path = this.configName;
			
			// Run a delayed task to close out this panel and open up the arena config.
			Bukkit.getScheduler().scheduleSyncDelayedTask(BlockHunt.plugin, new Runnable(){
				@Override
				public void run() {
					e.getView().close();  // close out current inventory view

					// Open up the arena config panel given the arena config object.
					TimePanel panel = new TimePanel(config, name, path);
					panel.open((Player) e.getWhoClicked());
				}

			}, 0);
		}
	}
	
	/*
	 * AmountButton is used to modify integer values from the configuration.
	 */
	public class AmountButton extends PanelButton {
		
		ArenaConfig config = null;
		int min;
		int max;
		
		AmountButton(ArenaConfig c, Material m, int materialData, String displayName, String configName, int min, int max) {
			super(m, materialData, displayName, configName);
			this.config = c;
			this.min = min;
			this.max = max;
			
			ItemStack item = getItemStack();
			int amount = this.config.getIntValue(this.configName);
			item.setAmount(amount);
			ItemMeta meta = item.getItemMeta();
			item.setItemMeta(meta);
		}
		
		public void onClick(final InventoryClickEvent e) {
			e.setCancelled(true);
			
			int amount = this.config.getIntValue(this.configName);
			
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
			amount = Math.max(this.min, amount);
			amount = Math.min(this.max, amount);
			
			this.config.setIntValue(this.configName, amount);
			
			ItemStack item = e.getInventory().getItem(e.getRawSlot());
			item.setAmount(amount);
		}
		
	}
	
	/*
	 * BackButton is used to go back to previous panel. The Settings panel is the previou for the ConfigPanel.
	 */
	public class BackButton extends PanelButton {
		
		BackButton() {
			super(Material.CHEST, 0, "Back to Settings", "back");
		}
		
		public void onClick(final InventoryClickEvent e) {
			e.setCancelled(true);
			
			// Run a delayed task to close out this panel and open up the arena config.
			Bukkit.getScheduler().scheduleSyncDelayedTask(BlockHunt.plugin, new Runnable(){
				@Override
				public void run() {
					e.getView().close();  // close out current inventory view

					// Open up the arena config panel given the arena config object.
					SettingsPanel panel = new SettingsPanel(BlockHunt.plugin);
					panel.open((Player) e.getWhoClicked());
				}

			}, 0);
		}
	}
	
	/*
	 * ConfigPanel constructor which initializes the panel layout.
	 */
	public ConfigPanel(ArenaConfig config) {
		this.config = config;
		buttons.put(0, new PanelButton(Material.PAPER, "Arena: " + config.name));
		buttons.put(1, new AmountButton(config, Material.SKULL_ITEM, 3, "Min Players", "minPlayers", 0, 64));
		buttons.put(2, new AmountButton(config, Material.SKULL_ITEM, 3, "Max Players", "maxPlayers", 0, 64));
		buttons.put(5, new TimeButton(config, "Total Time", "gameTimeSecs"));
		buttons.put(49, new BackButton());
	}
	
	/*
	 * Opens a new configuration dialog for the specified player.
	 */
	public void open(Player p) {

		int length = Math.min(config.name.length(), 28);
		String title = "§6§l" + config.name.substring(0, length);

		Inventory panel = Bukkit.createInventory(null, 54, title);

		for (Entry<Integer, PanelButton> entry : buttons.entrySet()) {
			Integer slot = entry.getKey();
			PanelButton button = entry.getValue();
			button.onUpdate();
			panel.setItem(slot, button.getItemStack());
		}

		BlockHunt.plugin.openDialogs.put(panel, this);
		p.openInventory(panel);
	}
}
