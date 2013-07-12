package me.naithantu.ArenaPVP.Gamemodes.Gamemodes;

import org.bukkit.entity.Player;

import me.naithantu.ArenaPVP.Events.BukkitEvents.ArenaPlayerDeathEvent;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaTeam;

public class TDM extends Gamemode {
	public TDM(ArenaManager arenaManager, Arena arena) {
		super(arenaManager, arena);
	}
	
	public void onPlayerDeath(ArenaPlayerDeathEvent event){
		super.onPlayerDeath(event);
		Player killer = event.getEntity().getKiller();
		if(killer != null){
			ArenaTeam team = arenaManager.getPlayerByName(killer.getName()).getTeam();
			team.addScore();
			if(team.getScore() == 10){ //Get max score out of config.
				//arena.endGame(team); End game
			}
		}
	}
}
