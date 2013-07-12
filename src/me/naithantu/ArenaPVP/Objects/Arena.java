package me.naithantu.ArenaPVP.Objects;

import java.util.ArrayList;
import java.util.List;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Events.EventJoinGame;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.Storage.YamlStorage;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Arena {
	ArenaManager arenaManager;
	
	List<ArenaTeam> teams = new ArrayList<ArenaTeam>();
	
	String arenaName;
	ArenaState arenaState;
	Gamemode gamemode;
	
	FileConfiguration arenaConfig;
	
	public Arena(ArenaPVP arenaPVP, ArenaManager arenaManager, String arenaName){
		this.arenaName = arenaName;
		arenaState = ArenaState.BEFORE_JOIN;
		YamlStorage arenaStorage = new YamlStorage(arenaPVP, "maps", arenaName);
		arenaConfig = arenaStorage.getConfig();
		initializeGamemode();
	}
	
	public void initializeGamemode(){
		 gamemode = new Gamemode(arenaManager, this);
	}
	
	public Gamemode getGamemode(){
		return gamemode;
	}
	
	public boolean joinGame(Player player){
		return joinGame(player, null);
	}
	
	public boolean joinGame(Player player, String teamName){
		ArenaTeam teamToJoin = null;
		if(arenaState != ArenaState.BEFORE_JOIN){
			if(teamName != null && getTeam(teamName) != null){
				teamToJoin = getTeam(teamName);
			} else {
				for(ArenaTeam team: teams){
					if(teamToJoin == null || team.getPlayers().size() < teamToJoin.getPlayers().size()){
						teamToJoin = team;
					}
				}
			}
			
			EventJoinGame event = new EventJoinGame(player, teamToJoin);
			Bukkit.getServer().getPluginManager().callEvent(event);
			if(!event.isCancelled()){
				event.getTeam().joinTeam(player);
				return true;
			}
		}
		return false;
	}
	
	public void addTeam(ArenaTeam team){
		teams.add(team);
	}
	
	public List<ArenaTeam> getTeams(){
		return teams;
	}
	
	public ArenaTeam getTeam(String teamName){
		for(ArenaTeam team: teams){
			if(team.getTeamName().equals(teamName)){
				return team;
			}
		}
		return null;
	}
}
