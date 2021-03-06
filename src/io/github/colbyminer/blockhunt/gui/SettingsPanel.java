package io.github.colbyminer.blockhunt.gui;

import io.github.colbyminer.blockhunt.Arena;
import io.github.colbyminer.blockhunt.ArenaConfig;
import io.github.colbyminer.blockhunt.BlockHunt;
import io.github.colbyminer.blockhunt.BlockHuntArena;

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

public class SettingsPanel extends Panel {
	
	BlockHunt plugin = null;
	
	public class ArenaButton extends PanelButton {
		
		ArenaButton(String arenaName) {
			super(Material.CHEST, 0, arenaName, arenaName);
			
		    BlockHuntArena arena = plugin.arenas.get(arenaName);
		    ArenaConfig config = arena.config;
		    
		    ItemStack item = getItemStack();
			ItemMeta  meta = item.getItemMeta();
			
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.translateAlternateColorCodes('&', "&7Arena: &6" + config.name));
			lore.add(ChatColor.translateAlternateColorCodes('&', "&7Min Players: &6" + config.getIntValue("minPlayers")));
			lore.add(ChatColor.translateAlternateColorCodes('&', "&7Max Players: &6" + config.getIntValue("maxPlayers")));
			lore.add("");
			lore.add(ChatColor.translateAlternateColorCodes('&', "&eClick for arena config"));
			
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
		
		public void onClick(final InventoryClickEvent e) {			
			e.setCancelled(true);
			
			final String arenaName = this.configName;
			
			// Run a delayed task to close out this panel and open up the arena config.
			Bukkit.getScheduler().scheduleSyncDelayedTask(BlockHunt.plugin, new Runnable(){
				@Override
				public void run() {
					e.getView().close();  // close out current inventory view
					
					// Get the arena using button's configName which has the arena name.
					Arena a = BlockHunt.plugin.arenas.get(arenaName);
					
					// Open up the arena config panel given the arena config object.
					ConfigPanel panel = new ConfigPanel(a.config);
					panel.open((Player) e.getWhoClicked());
				}

			}, 0);
		}
	}
	
	/*
	 * Setup buttons for settings panel. Create a chest button for each arena.
	 */
	public SettingsPanel(BlockHunt plugin) {
		this.plugin = plugin;

		int slot = 0;
		
		for (Entry<String, BlockHuntArena> entry : plugin.arenas.entrySet()) {
			buttons.put(slot,  new ArenaButton(entry.getKey()));
		    slot++;
		}
	}
	
	/*
	 * Opens a new configuration dialog for the specified player.
	 */
	public void open(Player p) {

		String title = "§7§lBlockHunt Settings";
		
		Inventory panel = Bukkit.createInventory(null, 54, title);
		
		for (Entry<Integer, PanelButton> entry : buttons.entrySet()) {
		    Integer slot = entry.getKey();
		    PanelButton button = entry.getValue();
		    
		    button.onUpdate();

		    ItemStack item = button.getItemStack();
		    panel.setItem(slot, item);
		}
		
		BlockHunt.plugin.openDialogs.put(panel, this);
		
		p.openInventory(panel);
	}
}
