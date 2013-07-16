package me.naithantu.ArenaPVP.Objects;

import java.util.ArrayList;
import java.util.List;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Events.ArenaEvents.EventJoinArena;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaGamemode;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaSettings;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaSpawns;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaSpawns.SpawnType;
import me.naithantu.ArenaPVP.Storage.YamlStorage;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Arena {
	ArenaPVP plugin;
	ArenaManager arenaManager;
	ArenaSpawns arenaSpawns;
	ArenaSettings settings;
	
	List<ArenaTeam> teams = new ArrayList<ArenaTeam>();

	String arenaName;
	ArenaState arenaState;
	Gamemode gamemode;

	YamlStorage arenaStorage;
	FileConfiguration arenaConfig;

	public Arena(ArenaPVP plugin, ArenaManager arenaManager, String arenaName, String gamemodeName) {
		this.plugin = plugin;
		this.arenaManager = arenaManager;
		this.arenaName = arenaName;
		arenaState = ArenaState.BEFORE_JOIN;
		arenaStorage = new YamlStorage(plugin, "maps", arenaName);
		arenaStorage.copyDefaultConfig();
		arenaConfig = arenaStorage.getConfig();
		
		arenaSpawns = new ArenaSpawns(plugin, arenaManager, this, settings, arenaConfig);
		settings = new ArenaSettings(arenaConfig);
		
		initializeArena(gamemodeName);
	}

	public ArenaSpawns getArenaSpawns() {
		return arenaSpawns;
	}

	public String getArenaName() {
		return arenaName;
	}

	public Gamemode getGamemode() {
		return gamemode;
	}

	public ArenaState getArenaState() {
		return arenaState;
	}

	public void setArenaState(ArenaState arenaState){
		this.arenaState = arenaState;
	}
	
	public void initializeArena(String gamemodeName) {
		// Create gamemode.
		gamemode = ArenaGamemode.getGamemode(arenaManager, this, settings, arenaSpawns, gamemodeName);

		// Create teams with proper names.
		for (String teamNumber : arenaConfig.getConfigurationSection("teams").getKeys(false)) {
			ArenaTeam team = new ArenaTeam(plugin, arenaConfig.getString("teams." + teamNumber));
			teams.add(team);
		}
	}

	public boolean joinGame(Player player) {
		return joinGame(player, null);
	}

	public boolean joinGame(Player player, ArenaTeam teamToJoin) {
		EventJoinArena event = new EventJoinArena(player, teamToJoin);
		gamemode.onPlayerJoinArena(event);
		if (!event.isCancelled()) {
			event.getTeam().joinTeam(player, arenaManager, this);
			return true;
		}

		return false;
	}
	
	public void startGame(){
		System.out.println("starting game.");
		for(ArenaTeam team: teams){
			System.out.println("going through team: " + team.getTeamName());
			for(ArenaPlayer arenaPlayer: team.getPlayers()){
				System.out.println("Teleporting: " + arenaPlayer.getPlayerName());
				Bukkit.getPlayer(arenaPlayer.getPlayerName()).teleport(arenaSpawns.getRespawnLocation(arenaPlayer, SpawnType.PLAYER));
			}
		}
	}

	public void addTeam(ArenaTeam team) {
		teams.add(team);
	}

	public List<ArenaTeam> getTeams() {
		return teams;
	}

	public ArenaTeam getTeam(String teamName) {
		for (ArenaTeam team : teams) {
			if (team.getTeamName().equals(teamName)) {
				return team;
			}
		}
		return null;
	}
}
