package me.naithantu.ArenaPVP.Events.BukkitEvents;

import me.naithantu.ArenaPVP.Objects.ArenaPlayer;

import org.bukkit.event.entity.PlayerDeathEvent;

public class ArenaPlayerDeathEvent extends PlayerDeathEvent{
	ArenaPlayer arenaPlayer;
	
	public ArenaPlayerDeathEvent(PlayerDeathEvent event, ArenaPlayer arenaPlayer){
		super(event.getEntity(), event.getDrops(), event.getDroppedExp(), event.getNewExp(), event.getNewTotalExp(), event.getNewLevel(), event.getDeathMessage());
		this.arenaPlayer = arenaPlayer;
	}
	
	public ArenaPlayer getArenaPlayer(){
		return arenaPlayer;
	}
}
