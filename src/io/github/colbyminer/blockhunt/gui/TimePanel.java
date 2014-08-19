package io.github.colbyminer.blockhunt.gui;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.colbyminer.blockhunt.ArenaConfig;
import io.github.colbyminer.blockhunt.BlockHunt;

public class TimePanel extends Panel {

	BlockHunt plugin = null;
	ArenaConfig config = null;
	String displayName = null;
	String configPath = null;
	
	public class HourButton extends PanelButton {
		
		ArenaConfig config = null;
		
		HourButton(ArenaConfig config, String path) {
			super(Material.GLASS_BOTTLE, 0, "Hours", path);
			this.config = config;
		}
		
		public void onUpdate() {
			int timeInSecs = config.getIntValue(this.configName);
			int hours = timeInSecs / 3600;
			
			ItemStack item = getItemStack();
			item.setAmount(hours);
			ItemMeta meta = item.getItemMeta();
			
			if (hours > 0) {
				item.setType(Material.POTION);
			} else {
				item.setType(Material.GLASS_BOTTLE);
			}
			
			item.setItemMeta(meta);
		}
		
		public void onClick(final InventoryClickEvent e) {
			e.setCancelled(true);
			
			int timeInSecs = config.getIntValue(this.configName);
			int hours = timeInSecs / 3600;
			int remainderSecs = timeInSecs - (hours * 3600);
			
			if (e.isLeftClick()) {
				if (e.isShiftClick()) {
					hours += 10;
				} else {
					hours++;
				}
			} else if (e.isRightClick()) {
				if (e.isShiftClick()) {
					hours -= 10;
				} else {
					hours--;
				}
			}
			
			// clamp amount to range.
			hours = Math.max(0, hours);
			hours = Math.min(64, hours);
			
			this.config.setIntValue(this.configName, remainderSecs + (hours*3600));
						
			ItemStack item = e.getInventory().getItem(e.getRawSlot());
			
			if (hours > 0) {
				item.setType(Material.POTION);
			} else {
				item.setType(Material.GLASS_BOTTLE);
			}
			
			item.setAmount(hours);
		}
	}
	
	public class MinuteButton extends PanelButton {
		
		ArenaConfig config = null;
		
		MinuteButton(ArenaConfig config, String path) {
			super(Material.GLASS_BOTTLE, 0, "Minutes", path);
			this.config = config;
		}
		
		public void onUpdate() {
			int timeInSecs = config.getIntValue(this.configName);
			int minutes = (timeInSecs % 3600) / 60;
			
			ItemStack item = getItemStack();
			item.setAmount(minutes);
			ItemMeta meta = item.getItemMeta();
			
			if (minutes > 0) {
				item.setType(Material.POTION);
			} else {
				item.setType(Material.GLASS_BOTTLE);
			}
			
			item.setAmount(1); // Transitions from a number greater than 1 to 0 causes issues. So force a 1 to 0 transition.
			item.setItemMeta(meta);
		}
		
		public void onClick(final InventoryClickEvent e) {
			e.setCancelled(true);
			
			int timeInSecs = config.getIntValue(this.configName);
			int minutes = (timeInSecs % 3600) / 60;
			int remainderSecs = timeInSecs - (minutes * 60);
			
			if (e.isLeftClick()) {
				if (e.isShiftClick()) {
					minutes += 10;
				} else {
					minutes++;
				}
			} else if (e.isRightClick()) {
				if (e.isShiftClick()) {
					minutes -= 10;
				} else {
					minutes--;
				}
			}
			
			// clamp amount to range.
			minutes = Math.max(0, minutes);
			minutes = Math.min(59, minutes);
			
			this.config.setIntValue(this.configName, remainderSecs + (minutes*60));

			ItemStack item = e.getInventory().getItem(e.getRawSlot());
			
			if (minutes > 0) {
				item.setType(Material.POTION);
			} else {
				item.setType(Material.GLASS_BOTTLE);
			}
			
			item.setAmount(1);
			item.setAmount(minutes);
		}
	}
	
	public class SecondButton extends PanelButton {
		
		ArenaConfig config = null;
		
		SecondButton(ArenaConfig config, String path) {
			super(Material.GLASS_BOTTLE, 0, "Seconds", path);
			this.config = config;
		}
		
		public void onUpdate() {
			int timeInSecs = config.getIntValue(this.configName);
			int seconds = timeInSecs % 60;
			
			ItemStack item = getItemStack();
			item.setAmount(seconds);
			ItemMeta meta = item.getItemMeta();
			if (seconds > 0) {
				item.setType(Material.POTION);
			} else {
				item.setType(Material.GLASS_BOTTLE);
			}
			item.setItemMeta(meta);
		}
		
		public void onClick(final InventoryClickEvent e) {
			e.setCancelled(true);
			
			int timeInSecs = config.getIntValue(this.configName);
			int seconds = timeInSecs % 60;
			int remainderSecs = timeInSecs - seconds;
			
			if (e.isLeftClick()) {
				if (e.isShiftClick()) {
					seconds += 10;
				} else {
					seconds++;
				}
			} else if (e.isRightClick()) {
				if (e.isShiftClick()) {
					seconds -= 10;
				} else {
					seconds--;
				}
			}
			
			// clamp amount to range.
			seconds = Math.max(0, seconds);
			seconds = Math.min(59, seconds);
			
			this.config.setIntValue(this.configName, remainderSecs + seconds);		
			
			ItemStack item = e.getInventory().getItem(e.getRawSlot());
			
			if (seconds > 0) {
				item.setType(Material.POTION);
			} else {
				item.setType(Material.GLASS_BOTTLE);
			}

			item.setAmount(1);
			item.setAmount(seconds);
		}
	}
	
	public TimePanel(ArenaConfig config, String displayName, String configPath) {
		this.plugin = BlockHunt.plugin;
		this.config = config;
		this.displayName = displayName;
		this.configPath = configPath;
		
		buttons.put(0, new PanelButton(Material.PAPER, this.displayName));
		buttons.put(1,  new HourButton(this.config, configPath));
		buttons.put(2,  new MinuteButton(this.config, configPath));
		buttons.put(3,  new SecondButton(this.config, configPath));
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
			button.onUpdate();
			ItemStack item = button.getItemStack();
			// int amount = this.config.getIntValue(button.configName);

			// item.setAmount(amount);

			panel.setItem(slot, item);
		}

		BlockHunt.plugin.openDialogs.put(panel, this);
		p.openInventory(panel);
	}
}
