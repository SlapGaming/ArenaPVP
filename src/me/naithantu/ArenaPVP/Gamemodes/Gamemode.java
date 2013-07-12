package me.naithantu.ArenaPVP.Gamemodes;

import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;

import org.bukkit.event.entity.PlayerDeathEvent;

public class Gamemode {
	ArenaManager arenaManager;
	Arena arena;
	
	public Gamemode(ArenaManager arenaManager, Arena arena){
		this.arenaManager = arenaManager;
		this.arena = arena;
	}
	
	public void onPlayerDeath(PlayerDeathEvent event){
		
	}
}
