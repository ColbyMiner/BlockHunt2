package io.github.colbyminer.blockhunt;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryManager {

	/*
	 * Give 
	 */
	public static void giveWand(Player p) {
		ItemStack wand = new ItemStack(Material.STICK);
		ItemMeta  meta = wand.getItemMeta();
		meta.setDisplayName("BlockHunt Selection Wand");
		
		List<String> lore = new ArrayList<String>();
		lore.add("Selection tool used to set arena region");
		lore.add("  Left-click to select first point of region.");
		lore.add("  Right-click to select second point of region.");
		lore.add("  Once region is defined then create arena.");
		lore.add("  /blockhunt help for more information");
		
		meta.setLore(lore);
		wand.setItemMeta(meta);
		
		p.getInventory().addItem(wand);
		p.playSound(p.getLocation(), Sound.ORB_PICKUP, 5, 0);
		p.sendMessage("You now have a BlockHunt selection wand!");
	}
	
	public static boolean hasWand(Player p) {
		ItemStack item = p.getItemInHand();
		
		if (item.getType() != Material.AIR) {
			ItemMeta meta = item.getItemMeta();
			
			if (meta.hasDisplayName()) {
				if (meta.getDisplayName().equals("BlockHunt Selection Wand")) {
					return true;
				}
			}
		}
		return false;
	}
}
