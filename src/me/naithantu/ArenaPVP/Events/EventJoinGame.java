package me.naithantu.ArenaPVP.Events;

import me.naithantu.ArenaPVP.Objects.ArenaTeam;

import org.bukkit.entity.Player;

public class EventJoinGame extends CustomEvent {
	Player player;
	ArenaTeam team;
	
	public EventJoinGame(Player player, ArenaTeam team){
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
