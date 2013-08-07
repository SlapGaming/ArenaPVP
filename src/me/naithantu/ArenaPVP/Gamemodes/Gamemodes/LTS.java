package me.naithantu.ArenaPVP.Gamemodes.Gamemodes;

import org.bukkit.event.entity.PlayerDeathEvent;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaPlayerState;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSettings;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSpawns;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaUtil;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Storage.YamlStorage;

public class LTS extends Gamemode {

	public LTS(ArenaPVP plugin, ArenaManager arenaManager, Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns, ArenaUtil arenaUtil, YamlStorage arenaStorage) {
		super(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage);
	}
	
	@Override
	public String getName(){
		return "LTS";
	}

	@Override
	public void onPlayerDeath(PlayerDeathEvent event, ArenaPlayer arenaPlayer) {
		super.onPlayerDeath(event, arenaPlayer);
		if(arena.getArenaState() == ArenaState.PLAYING && arenaPlayer.getPlayerState() == ArenaPlayerState.PLAYING){
			arenaPlayer.setPlayerState(ArenaPlayerState.OUTOFGAME);
			if(checkRemainingTeams() == 1){
				arena.stopGame(getWinningTeam());
			}
		}
	}
	
	private int checkRemainingTeams(){
		int remainingTeams = 0;
		for(ArenaTeam team: arena.getTeams()){
			for(ArenaPlayer arenaPlayer: team.getPlayers()){
				if(arenaPlayer.getPlayerState() != ArenaPlayerState.OUTOFGAME){
					remainingTeams++;
					break;
				}
			}
		}
		return remainingTeams;
	}
	
	private ArenaTeam getWinningTeam(){
		for(ArenaTeam team: arena.getTeams()){
			for(ArenaPlayer arenaPlayer: team.getPlayers()){
				if(arenaPlayer.getPlayerState() != ArenaPlayerState.OUTOFGAME){
					return team;
				}
			}
		}
		return null;
	}
}
