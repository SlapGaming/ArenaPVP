package me.naithantu.ArenaPVP.Objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaSpawns.SpawnType;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaState;

public class ArenaTeam {
	ArenaPVP plugin;
	String teamName;
	
	int score = 0;
	
	List<ArenaPlayer> players = new ArrayList<ArenaPlayer>();
	
	public ArenaTeam(ArenaPVP plugin, String teamName){
		this.plugin = plugin;
		this.teamName = teamName;
	}
	
	public String getTeamName(){
		return teamName;
	}
	
	public void setTeamName(String teamName){
		this.teamName = teamName;
	}
	
	public List<ArenaPlayer> getPlayers(){
		return players;
	}
		
	public void addScore(){
		score++;
	}
	
	public int getScore(){
		return score;
	}
	
	public void setScore(int score){
		this.score = score;
	}
	
	public void joinTeam(Player player, Arena arena){
		ArenaPlayer arenaPlayer = new ArenaPlayer(player.getName(), arena, this);	
		if(arena.getArenaState() == ArenaState.PLAYING){
			arena.getArenaSpawns().respawnPlayer(arenaPlayer, SpawnType.PLAYER);
		} else {
			arena.getArenaSpawns().respawnPlayer(arenaPlayer, SpawnType.SPECTATOR);
		}
	}
}
