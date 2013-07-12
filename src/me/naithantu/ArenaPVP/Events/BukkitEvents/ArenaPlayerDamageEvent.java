package me.naithantu.ArenaPVP.Events.BukkitEvents;

import me.naithantu.ArenaPVP.Objects.ArenaPlayer;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ArenaPlayerDamageEvent extends EntityDamageByEntityEvent{
	ArenaPlayer arenaPlayer;
	
	public ArenaPlayerDamageEvent(EntityDamageByEntityEvent event, ArenaPlayer arenaPlayer){
		super(event.getDamager(), event.getEntity(), event.getCause(), event.getDamage());
		this.arenaPlayer = arenaPlayer;
	}
	
	public ArenaPlayer getArenaPlayer(){
		return arenaPlayer;
	}
}
