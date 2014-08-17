package io.github.colbyminer.blockhunt;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class Arena {

	List<UUID> players;
	Scoreboard scoreboard;
	public ArenaConfig config;
	
	public Arena(ArenaConfig config) {
		this.config = config;
	}
	
	public void join(Player p) {
		UUID id = p.getUniqueId();
		
		if (players.contains(id) == false) {
			players.add(id);
			onJoin(p);
		} else {
			// Already joined arena
		}
	}
	
	public void leave(Player p) {
		UUID id = p.getUniqueId();
		
		if (players.contains(id) == true) {
			players.remove(id);
			onLeave(p);
		}
	}
	
	protected void tick() {
		onTick();
	}
	
	protected void onJoin(Player p) {
		
	}
	
	protected void onLeave(Player p) {
		
	}
	
	protected void onTick() {
		
	}
}
