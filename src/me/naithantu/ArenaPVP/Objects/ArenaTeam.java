package me.naithantu.ArenaPVP.Objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaSpawns.SpawnType;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.Util.Util;

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
	
	public void joinTeam(Player player, ArenaManager arenaManager, Arena arena){
		ArenaPlayer arenaPlayer = new ArenaPlayer(player.getName(), arena, this);
		arenaManager.addPlayer(arenaPlayer);
		players.add(arenaPlayer);
		if(arena.getArenaState() == ArenaState.PLAYING){
			player.teleport(arena.getArenaSpawns().getRespawnLocation(player, arenaPlayer, SpawnType.PLAYER));
		} else {
			player.teleport(arena.getArenaSpawns().getRespawnLocation(player, arenaPlayer, SpawnType.SPECTATOR));
		}
	}
	
	public void leaveTeam(ArenaManager arenaManager, ArenaPlayer arenaPlayer, Player player){
		arenaManager.removePlayer(arenaPlayer);
		players.remove(arenaPlayer);
		Location spawnLocation = Util.getLocationFromString(plugin.getConfig().getString("spawnlocation"));
		player.teleport(spawnLocation);
	}
}
