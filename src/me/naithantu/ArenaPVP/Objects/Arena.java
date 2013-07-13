package me.naithantu.ArenaPVP.Objects;

import java.util.ArrayList;
import java.util.List;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Events.ArenaEvents.EventJoinGame;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaGamemode;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.Storage.YamlStorage;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Arena {
	ArenaPVP plugin;
	ArenaManager arenaManager;

	List<ArenaTeam> teams = new ArrayList<ArenaTeam>();

	String arenaName;
	ArenaState arenaState;
	Gamemode gamemode;

	YamlStorage arenaStorage;
	FileConfiguration arenaConfig;

	public Arena(ArenaPVP plugin, ArenaManager arenaManager, String arenaName, String gamemodeName) {
		this.plugin = plugin;
		this.arenaName = arenaName;
		arenaState = ArenaState.BEFORE_JOIN;
		arenaStorage = new YamlStorage(plugin, "maps", arenaName);
		arenaConfig = arenaStorage.getConfig();
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

	public void initializeArena(String gamemodeName) {
		// Create gamemode.
		gamemode = ArenaGamemode.getGamemode(arenaManager, this, gamemodeName);

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
		//TODO Don't do this here, do this in the basegamemode instead. Gamemodes might want to change this.
		if (arenaState != ArenaState.BEFORE_JOIN) {
			if (teamToJoin == null) {
				for (ArenaTeam team : teams) {
					if (teamToJoin == null || team.getPlayers().size() < teamToJoin.getPlayers().size()) {
						teamToJoin = team;
					}
				}
			}

			EventJoinGame event = new EventJoinGame(player, teamToJoin);
			Bukkit.getServer().getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				event.getTeam().joinTeam(player);
				return true;
			}
		}
		return false;
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
