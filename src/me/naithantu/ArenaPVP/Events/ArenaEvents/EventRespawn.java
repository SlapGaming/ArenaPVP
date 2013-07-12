package me.naithantu.ArenaPVP.Events.ArenaEvents;

import me.naithantu.ArenaPVP.Events.CustomEvent;
import me.naithantu.ArenaPVP.Objects.ArenaPlayer;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class EventRespawn extends CustomEvent {
	Player player;
	Location location;
	ArenaPlayer arenaPlayer;
	
	public EventRespawn(Player player, Location location, ArenaPlayer arenaPlayer){
		this.player = player;
		this.location = location;
		this.arenaPlayer = arenaPlayer;
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
}
