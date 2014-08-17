package io.github.colbyminer.blockhunt;


import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ArenaConfig {

	JavaPlugin plugin = null;
	
	/*
	 * Cached arena configuration
	 */
	public String name;
	
	public ArenaConfig(JavaPlugin plugin, String name) {
		this.plugin = plugin;
		this.name = name;
		
		load();
	}
	
	/*
	 * Setup arena default settings in config.yml. Called from onEnable() and /bh reload.
	 */
	public static void setupDefaults(JavaPlugin plugin) {
		plugin.getConfig().addDefault("arena-defaults.minPlayers", 2);
		plugin.getConfig().addDefault("arena-defaults.maxPlayers", 12);
		
		plugin.saveDefaultConfig();
		plugin.getConfig().options().copyDefaults(true);
	}
	
	/*
	 * Load arena default settings from config.yml to arena config.
	 *   If a user removes keys from the arena config or upgrades to a newer BlockHunt that adds new keys
	 *   then this will copy the default values for those into the arena config.
	 */
	private void loadDefaults() {
		FileConfiguration config = plugin.getConfig();
		ConfigurationSection defaults = config.getDefaultSection();
		
		if (defaults != null) {
			
			ConfigurationSection arenaDefaults = defaults.getConfigurationSection("arena-defaults");
			Set<String> arenaDefaultsKeys = arenaDefaults.getKeys(false);
			
			String path = "arena." + this.name + ".";
			
			for(String key : arenaDefaultsKeys) {
				
				String defPath = path + key; 
				if (config.contains(defPath) == false) {
					config.set(defPath, arenaDefaults.get(key));
				}
			}
		} else {
			Bukkit.getConsoleSender().sendMessage("[BlockHunt] Could not copy default values!");
		}
		
		plugin.saveConfig();
	}
	
	/*
	 * Loads arena settings into cache.
	 */
	public void load() {
		loadDefaults();
	}
	
	public int getIntValue(String key) {
		return plugin.getConfig().getInt("arena." + this.name + "." + key);
	}
	
	public void setIntValue(String key, int value) {
		plugin.getConfig().set("arena." + this.name + "." + key, value);
	}
	
	public int getMinPlayers() {
		return plugin.getConfig().getInt("arena." + this.name + ".minPlayers");
	}
	
	public int getMaxPlayers() {
		return plugin.getConfig().getInt("arena." + this.name + ".maxPlayers");
	}
}