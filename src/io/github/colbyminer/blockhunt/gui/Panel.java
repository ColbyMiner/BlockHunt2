package io.github.colbyminer.blockhunt.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Panel {

	Map<Integer, PanelButton> buttons = new HashMap<Integer, PanelButton>();
	
	/*
	 * PanelIcon contains state needed to display an icon in the inventory control.
	 */
	public class PanelIcon {
		public Material material;      // ItemStack material for dialog button.
		public Integer  materialData;  // Datavalue for material. TODO This is deprecated feature.
		public String   displayName;   // Display name for button.
		
		List<String> lore = new ArrayList<String>();
		
		public PanelIcon(Material material, Integer materialData, String displayName) {
			this.material = material;
			this.materialData = materialData;
			this.displayName = displayName;
		}
		
		public ItemStack getItemStack() {
			
			ItemStack item = new ItemStack(this.material, 1, (byte)this.materialData.intValue());
			ItemMeta meta = Bukkit.getItemFactory().getItemMeta(this.material);
			
			if (meta != null) {
				meta.setDisplayName(this.displayName);
				item.setItemMeta(meta);
			}

            return item;
		}
	}
	
	/*
	 * PanelButton contains the state needed to represent a button in the panel.
	 */
	public class PanelButton extends PanelIcon {
		
		public String configName;
		public int min;
		public int max;
		
		public boolean isButton = true;
		public boolean isTime = false;
		
		public PanelButton(Material m, String displayName) {
			super(m, 0, displayName);
		}
		
		public PanelButton(Material material, String displayName, String configName, int minAmount, int maxAmount) {
			super(material, 0, displayName);
			this.configName = configName;
			this.min = minAmount;
			this.max = maxAmount;
		}
		
		public PanelButton(Material material, Integer materialData, String displayName, String configName, int minAmount, int maxAmount) {
			super(material, materialData, displayName);
			this.configName = configName;
			this.min = minAmount;
			this.max = maxAmount;
		}
		
		public PanelButton(Material material, Integer materialData, String displayName, String configName, boolean isTime) {
			super(material, materialData, displayName);
			this.configName = configName;
			this.isTime = isTime;
			this.min = 0;
			this.max = 0;
		}
		
		public void onClick(final InventoryClickEvent e) {
		}
	}
	
	public Panel() { }
	
	/*
	 * onInventoryClickEvent is called by PanelEventDispatcher.
	 *     Method is implemented in the derived classes.
	 */
	public void onInventoryClickEvent(final InventoryClickEvent e) {}
}
