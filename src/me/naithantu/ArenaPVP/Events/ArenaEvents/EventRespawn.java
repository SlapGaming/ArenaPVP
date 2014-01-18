package me.naithantu.ArenaPVP.Events.ArenaEvents;

import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSpawns.SpawnType;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Events.CustomEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class EventRespawn extends CustomEvent {
	Player player;
	Location location;
	ArenaPlayer arenaPlayer;
	SpawnType spawnType;
	
	public EventRespawn(Player player, Location location, ArenaPlayer arenaPlayer, SpawnType spawnType){
		this.player = player;
		this.location = location;
		this.arenaPlayer = arenaPlayer;
		this.spawnType = spawnType;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public Location getLocation(){
		return location;
	}
	
	public ArenaPlayer getArenaPlayer(){
		return arenaPlayer;
	}
	
	public SpawnType getSpawnType(){
		return spawnType;
	}
}
