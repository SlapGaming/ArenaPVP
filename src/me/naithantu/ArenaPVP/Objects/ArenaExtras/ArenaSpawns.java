package me.naithantu.ArenaPVP.Objects.ArenaExtras;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.naithantu.ArenaPVP.Events.ArenaEvents.EventRespawn;
import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaPlayer;
import me.naithantu.ArenaPVP.Util.Util;

public class ArenaSpawns {
	ArenaManager arenaManager;
	Arena arena;
	
	public ArenaSpawns(ArenaManager arenaManager, Arena arena){
		this.arenaManager = arenaManager;
		this.arena = arena;
	}
	
	public enum spawnType{
		SPECTATOR, PLAYER
	}
	
	public void respawnPlayer(ArenaPlayer arenaPlayer){
		Player player = Bukkit.getServer().getPlayer(arenaPlayer.getPlayerName());
		
		FileConfiguration config = arena.getConfig();
		List<String> stringLocations = config.getStringList("spawns." + arena.getTeams().indexOf(arenaPlayer.getTeam()));
		
		int locationIndex = 0;
		if(stringLocations.size() > 1){
			Random random = new Random();
			locationIndex = random.nextInt(stringLocations.size());
		}
		
		Location spawnLocation = Util.getLocationFromString(stringLocations.get(locationIndex));
		EventRespawn eventRespawn = new EventRespawn(player, spawnLocation, arenaPlayer);		
		arena.getGamemode().onPlayerArenaRespawn(eventRespawn);
		if(!eventRespawn.isCancelled()){
			player.teleport(eventRespawn.getLocation());
		}
	}
}
