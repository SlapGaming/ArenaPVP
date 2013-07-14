package me.naithantu.ArenaPVP.Events.BukkitEvents;

import me.naithantu.ArenaPVP.Objects.ArenaPlayer;

import org.bukkit.event.player.PlayerRespawnEvent;

public class ArenaPlayerRespawnEvent extends PlayerRespawnEvent{
	ArenaPlayer arenaPlayer;
	
	public ArenaPlayerRespawnEvent(PlayerRespawnEvent event, ArenaPlayer arenaPlayer){
		super(event.getPlayer(), event.getRespawnLocation(), event.isBedSpawn());
		this.arenaPlayer = arenaPlayer;
	}
	
	public ArenaPlayer getArenaPlayer(){
		return arenaPlayer;
	}
}
