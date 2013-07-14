package me.naithantu.ArenaPVP.Objects;

import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaPlayerState;

public class ArenaPlayer {
	String playerName;
	PlayerScore playerScore;
	Arena arena;
	ArenaTeam team;
	ArenaPlayerState playerState = ArenaPlayerState.PLAYING;

	public ArenaPlayer(String playerName, Arena arena, ArenaTeam team) {
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
}
