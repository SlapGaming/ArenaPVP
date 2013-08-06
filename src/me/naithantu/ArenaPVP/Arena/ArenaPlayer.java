package me.naithantu.ArenaPVP.Arena;

import org.bukkit.entity.Player;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaPlayerState;
import me.naithantu.ArenaPVP.Arena.PlayerExtras.PlayerScore;
import me.naithantu.ArenaPVP.Arena.PlayerExtras.PlayerTimers;

public class ArenaPlayer {
	private String playerName;
	private PlayerScore playerScore;
	private PlayerTimers timers;
	private Arena arena;
	private ArenaTeam team;
	private ArenaPlayerState playerState = ArenaPlayerState.PLAYING;
		
	public ArenaPlayer(ArenaPVP plugin, Player player, Arena arena, ArenaTeam team) {
		this.playerName = player.getName();
		this.arena = arena;
		this.team = team;
		this.playerScore = new PlayerScore(playerName);
		this.timers = new PlayerTimers(plugin, arena, this, player);
	}

	public String getPlayerName() {
		return playerName;
	}

	public Arena getArena() {
		return arena;
	}

	public ArenaTeam getTeam() {
		return team;
	}

	public PlayerScore getPlayerScore() {
		return playerScore;
	}

	public ArenaPlayerState getPlayerState() {
		return playerState;
	}

	public void setPlayerState(ArenaPlayerState playerState) {
		this.playerState = playerState;
	}
	
	public PlayerTimers getTimers(){
		return timers;
	}	
}
