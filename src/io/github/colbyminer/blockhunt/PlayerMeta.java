package io.github.colbyminer.blockhunt;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class PlayerMeta {

	PlayerMeta() {
		
	}

	public static boolean hasRegionPoint1(Player p) {
		return (p.hasMetadata("bh.pt1"));
	}
	
	public static boolean hasRegionPoint2(Player p) {
		return (p.hasMetadata("bh.pt2"));
	}
	
	public static boolean hasRegionPoints(Player p) {
		return (hasRegionPoint1(p) && hasRegionPoint2(p));
	}
	
	public static void setRegionPoint1(Player p , Location l) {
		p.setMetadata("bh.pt1", new FixedMetadataValue(BlockHunt.plugin, l));
	}
	
	public static void setRegionPoint2(Player p, Location l) {
		p.setMetadata("bh.pt2", new FixedMetadataValue(BlockHunt.plugin, l));
	}
	
	public static Location getRegionPoint1(Player p) {		
		return (Location) p.getMetadata("bh.pt1").get(0);
	}
	
	public static Location getRegionPoint2(Player p) {		
		return (Location) p.getMetadata("bh.pt2").get(0);
	}
	
	public static void removeRegionPoints(Player p) {
		p.setMetadata("bh.pt1", null);
		p.setMetadata("bh.pt2", null);
	}
}