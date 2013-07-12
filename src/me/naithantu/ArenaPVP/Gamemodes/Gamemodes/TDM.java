package me.naithantu.ArenaPVP.Gamemodes.Gamemodes;

import org.bukkit.event.entity.PlayerDeathEvent;

import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;

public class TDM extends Gamemode {

	public TDM(ArenaManager arenaManager, Arena arena) {
		super(arenaManager, arena);
	}
	
	public void onPlayerDeath(PlayerDeathEvent event){
		super.onPlayerDeath(event);
		
	}
}
