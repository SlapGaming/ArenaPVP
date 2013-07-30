package me.naithantu.ArenaPVP.Objects;

import org.bukkit.Bukkit;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaPlayerState;

public class ArenaPlayer {
	ArenaPVP plugin;
	
	String playerName;
	PlayerScore playerScore;
	Arena arena;
	ArenaTeam team;
	ArenaPlayerState playerState = ArenaPlayerState.PLAYING;
	
	boolean spawnProtection;

	public ArenaPlayer(ArenaPVP plugin, String playerName, Arena arena, ArenaTeam team) {
		this.plugin = plugin;
		this.playerName = playerName;
		this.arena = arena;
		this.team = team;
		this.playerScore = new PlayerScore(playerName);
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
	
	public boolean hasSpawnProtection(){
		return spawnProtection;
	}
	
	public void giveSpawnProtection(){
		if(arena.getSettings().getSpawnProtection() > 0){
			spawnProtection = true;
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					spawnProtection = false;
				}
			}, arena.getSettings().getSpawnProtection() * 20);
		}		
	}
}
