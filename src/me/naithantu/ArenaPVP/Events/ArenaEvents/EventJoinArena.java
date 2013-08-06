package me.naithantu.ArenaPVP.Events.ArenaEvents;

import me.naithantu.ArenaPVP.Arena.ArenaTeam;
import me.naithantu.ArenaPVP.Events.CustomEvent;

import org.bukkit.entity.Player;

public class EventJoinArena extends CustomEvent {
	Player player;
	ArenaTeam team;
	
	public EventJoinArena(Player player, ArenaTeam team){
		this.player = player;
		this.team = team;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public ArenaTeam getTeam(){
		return team;
	}
	
	public void setTeam(ArenaTeam team){
		this.team = team;
	}
}
