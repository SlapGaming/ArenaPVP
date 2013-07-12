package me.naithantu.ArenaPVP.Objects;

public class ArenaPlayer {
	String playerName;
	PlayerScore playerScore;
	Arena arena;
	ArenaTeam team;
	
	public ArenaPlayer(String playerName, Arena arena, ArenaTeam team){
		this.playerName = playerName;
		this.arena = arena;
		this.team = team;
		this.playerScore = new PlayerScore(playerName);
	}
	
	public String getPlayerName(){
		return playerName;
	}
	
	public Arena getArena(){
		return arena;
	}
	
	public ArenaTeam getTeam(){
		return team;
	}
	
	public PlayerScore getPlayerScore(){
		return playerScore;
	}
}
