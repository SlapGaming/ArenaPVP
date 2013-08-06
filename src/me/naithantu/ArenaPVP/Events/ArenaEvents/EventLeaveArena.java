package me.naithantu.ArenaPVP.Events.ArenaEvents;

import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Events.CustomEvent;

public class EventLeaveArena extends CustomEvent {
	ArenaPlayer arenaPlayer;
	
	public EventLeaveArena(ArenaPlayer arenaPlayer){
		this.arenaPlayer = arenaPlayer;
	}
	
	public ArenaPlayer getArenaPlayer(){
		return arenaPlayer;
	}
}
