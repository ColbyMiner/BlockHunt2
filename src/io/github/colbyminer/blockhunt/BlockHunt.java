package io.github.colbyminer.blockhunt;

import io.github.colbyminer.blockhunt.gui.ConfigPanel;
import io.github.colbyminer.blockhunt.gui.Panel;
import io.github.colbyminer.blockhunt.gui.SettingsPanel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockHunt extends JavaPlugin implements CommandExecutor, Listener {
	
	public static BlockHunt plugin;
	
	InventoryManager invMgr       = new InventoryManager();
	
	public Map<String, BlockHuntArena> arenas = new HashMap<String, BlockHuntArena>();
	public Map<Inventory, Panel> openDialogs = new HashMap<Inventory, Panel>();
	
	@Override
	public void onEnable() {
		plugin = this;
		
		getServer().getPluginManager().registerEvents(this, this);
		
		// Setup arena config default values.
		ArenaConfig.setupDefaults(this);
		
		loadArenas(Bukkit.getConsoleSender());
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private void loadArenas(CommandSender sender) {
		// Load all arenas from configuration file.
		ConfigurationSection section = getConfig().getConfigurationSection("arena");

		if (section != null) {
			Set<String> keys = section.getKeys(false);
			
			for(String name : keys) {
				sender.sendMessage("[BlockHunt] Loading arena: " + name);
				sender.sendMessage("[BlockHunt]    minPlayers: " + section.getInt(name + ".minPlayers"));
				sender.sendMessage("[BlockHunt]    maxPlayers: " + section.getInt(name + ".maxPlayers"));
				
				ArenaConfig config = new ArenaConfig(this, name);
				BlockHuntArena arena = new BlockHuntArena(config);

				arenas.put(name, arena);
			}
		} else {
			Bukkit.getConsoleSender().sendMessage("[BlockHunt] No arenas found in config.yml.");
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		
		Player p = null;
		
		if (sender instanceof Player) {
			p = (Player)sender;
		} else {
			sender.sendMessage("BlockHunt commands are not available from console!");
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("blockhunt") ||
			cmd.getName().equalsIgnoreCase("bh")) {
			
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("wand")) {
					InventoryManager.giveWand(p);
					return true;
				}
				
				if (args[0].equalsIgnoreCase("create")) {
					if (args.length > 1) {
						String name = args[1];

						// Need a defined region before you can create arena.
						if (PlayerMeta.hasRegionPoints(p)) {
							ArenaConfig config = new ArenaConfig(this, name);
							BlockHuntArena arena = new BlockHuntArena(config);
							
							p.sendMessage("[BlockHunt] Successfully created arena " + name + ".");
							
							arenas.put(name, arena);

							return true;
						} else {
							p.sendMessage("[BlockHunt] Need to specify two region points first with the BlockHunt wand");
						}
						
					} else {
						
					}
				}
				
				if (args[0].equalsIgnoreCase("settings")) {
					SettingsPanel panel = new SettingsPanel(this);
					panel.open(p);
					return true;
				}
				
				if (args[0].equalsIgnoreCase("config")) {
					if (args.length > 1) {
						String name = args[1];
						
						if (arenas.containsKey(name)) {
							Arena a = arenas.get(name);
							
							ConfigPanel panel = new ConfigPanel(a.config);
							panel.open(p);
							
						} else {
							p.sendMessage("[BlockHunt] Could not open config for unknown arena: " + name);
						}
					} else {
						p.sendMessage("[BlockHunt] Invalid args. /blockhunt config <arena name>");
					}
					return true;
				}
				
				if (args[0].equalsIgnoreCase("reload")) {
					p.sendMessage("[BlockHunt] Reloading config");
					this.reloadConfig();
					
					ArenaConfig.setupDefaults(this);
					
					// Update arena config objects
					Set<String> arenaSet = arenas.keySet();
					
					for(String arena : arenaSet) {							
						
						Arena a = arenas.get(arena);
						a.config.load();
						
						p.sendMessage("[BlockHunt]   Reloaded Arena: " + a.config.name);							
					}
					
					return true;
				}
				
				if (args[0].equalsIgnoreCase("debug")) {
					
					p.sendMessage("[BlockHunt] Debug...");
					
					// List all arenas from config file.
					ConfigurationSection section = getConfig().getConfigurationSection("arena");

					if (section != null) {
						Set<String> keys = section.getKeys(false);
						
						for(String arena : keys) {
							p.sendMessage("[BlockHunt] Arena: " + arena);
							p.sendMessage("[BlockHunt]    minPlayers: " + section.getInt(arena + ".minPlayers"));
							p.sendMessage("[BlockHunt]    maxPlayers: " + section.getInt(arena + ".maxPlayers"));
						}
					} else {
						p.sendMessage("[BlockHunt] Could not find arena section in config.yml");
					}
					
					Set<String> arenaSet = arenas.keySet();
					
					for(String arena : arenaSet) {							
						
						Arena a = arenas.get(arena);
						p.sendMessage("[BlockHunt] Cached Arena: " + a.config.name);							
					}
				}
			}
		}
		
		return true;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		
		if (e.hasBlock() == false) {
			return;
		}
		
		Player p = e.getPlayer();
		
		if (InventoryManager.hasWand(p)) {
			// TODO Check permissions
			Location l = e.getClickedBlock().getLocation();
			
			if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				e.setCancelled(true);
				PlayerMeta.setRegionPoint1(p, l);
				p.sendMessage("First arena region point set at " + l.toVector().toString());
			} else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				e.setCancelled(true);
				PlayerMeta.setRegionPoint2(p, l);
				p.sendMessage("Second arena region point set at " + l.toVector().toString());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryCloseEvent(InventoryCloseEvent e) {
		openDialogs.remove(e.getInventory());
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryClickEvent(InventoryClickEvent e) {
		
		// Get dialog instance associated with this inventory.
		Panel dialog = openDialogs.get(e.getInventory());
		
		if (dialog != null) {
			dialog.onInventoryClickEvent(e);
		}
	}
}
