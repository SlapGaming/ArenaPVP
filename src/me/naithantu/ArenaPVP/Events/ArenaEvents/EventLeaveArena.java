package me.naithantu.ArenaPVP.Events.ArenaEvents;

import me.naithantu.ArenaPVP.Events.CustomEvent;
import me.naithantu.ArenaPVP.Objects.ArenaPlayer;

public class EventLeaveArena extends CustomEvent {
	ArenaPlayer arenaPlayer;
	
	public EventLeaveArena(ArenaPlayer arenaPlayer){
		this.arenaPlayer = arenaPlayer;
	}
	
	public ArenaPlayer getArenaPlayer(){
		return arenaPlayer;
	}
}
