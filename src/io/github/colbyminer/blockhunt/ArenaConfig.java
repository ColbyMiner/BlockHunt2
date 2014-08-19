package io.github.colbyminer.blockhunt;


import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * ArenaConfig manages the per-arena configuration.
 */
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
		plugin.getConfig().addDefault("arena-defaults.gameTimeSecs", 300);
		plugin.getConfig().addDefault("arena-defaults.seekerStartTimeSecs", 30);
		plugin.getConfig().addDefault("arena-defaults.hidersSwordTimeSecs", 60);
		plugin.getConfig().addDefault("arena-defaults.seekerRespawnTimeSecs",  10);
		
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
	
	/*
	 * getIntValue returns the integer pointed to by config path.
	 */
	public int getIntValue(String key) {
		return plugin.getConfig().getInt("arena." + this.name + "." + key);
	}
	
	/*
	 * getTimeValue builds a formatted string containing the time given seconds pointed to by config path.
	 */
	public String getTimeValue(String key) {
		int totalSecs = plugin.getConfig().getInt(
				"arena." + this.name + "." + key);

		int hours = totalSecs / 3600;
		int minutes = (totalSecs % 3600) / 60;
		int seconds = totalSecs % 60;

		String time = "";

		if (hours > 0) {
			time += (String.format("%d hour", hours) + ((hours > 1) ? "s " : " "));
		}

		if (minutes > 0) {
			time += (String.format("%d min", minutes) + ((minutes > 1) ? "s " : " "));
		}

		if (seconds > 0) {
			time += (String.format("%d sec", seconds) + ((seconds > 1) ? "s" : ""));
		}

		if (totalSecs == 0) {
			time = "Not set";
		}

		return time;
	}

	public void setIntValue(String key, int value) {
		plugin.getConfig().set("arena." + this.name + "." + key, value);
		
		plugin.saveConfig();
	}
}
