package me.naithantu.ArenaPVP.Gamemodes;

import me.naithantu.ArenaPVP.Events.ArenaEvents.EventRespawn;
import me.naithantu.ArenaPVP.Events.BukkitEvents.ArenaPlayerDamageEvent;
import me.naithantu.ArenaPVP.Events.BukkitEvents.ArenaPlayerDeathEvent;
import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Gamemode {
	protected ArenaManager arenaManager;
	protected Arena arena;
	
	public Gamemode(ArenaManager arenaManager, Arena arena){
		this.arenaManager = arenaManager;
		this.arena = arena;
	}
	
	public void onPlayerDeath(ArenaPlayerDeathEvent event){
		Player player = event.getEntity();
		ArenaPlayer arenaPlayer = event.getArenaPlayer();
		
		//Add death and kill to players.
		arenaPlayer.getPlayerScore().addDeath();
		if (player.getKiller() != null)
			arenaManager.getPlayerByName(player.getKiller().getName()).getPlayerScore().addKill();
	}
	
	public void onPlayerRespawn(PlayerRespawnEvent event){
		
	}
	
	public void onPlayerDamage(ArenaPlayerDamageEvent event){
		
	}
	
	public void onPlayerMove(PlayerMoveEvent event){
		
	}
	
	public void onPlayerQuit(PlayerQuitEvent event){
		
	}
	
	
	//Arena made events.
	
	public void onPlayerArenaRespawn(EventRespawn event){
		
	}
}
