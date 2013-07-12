package me.naithantu.ArenaPVP.Objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.naithantu.ArenaPVP.ArenaPVP;

public class ArenaTeam {
	ArenaPVP plugin;
	String teamName;
	
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
	
	public void joinTeam(Player player){
		
	}
}
