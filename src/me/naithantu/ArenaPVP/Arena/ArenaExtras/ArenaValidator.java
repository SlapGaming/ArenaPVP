package me.naithantu.ArenaPVP.Arena.ArenaExtras;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode.Gamemodes;
import me.naithantu.ArenaPVP.Storage.YamlStorage;

@SuppressWarnings("unused")
public class ArenaValidator {

	private ArenaPVP plugin;
	private String arena;
	private Gamemodes gm;
	
	private YamlStorage storage;
	private FileConfiguration config;
	
	private boolean teamGame;
	private boolean hasError;
	private ArrayList<String> errors;	
	
	private ArrayList<ConfigTeam> teams;
	
	//General settings
	private int maxPlayers = -1;
	private int scoreLimit = -1;
	private int respawnTime = -1;
	private boolean outOfBoundsArea;
	private int outOfBoundsTime = -1;
	private int spawnProtection = -1;
	private String defaultChatChannel;
	private boolean allowItemDrop;
	private boolean autoBalance;
	private boolean allowSpectate;
	private boolean allowBlockChange;
	
	private boolean friendlyFire;
	
	public ArenaValidator(ArenaPVP plugin, String arena) {
		this.plugin = plugin;
		this.arena = arena;
		hasError = false;
	}
	
	public boolean checkOnSelect() {
		if (!getYML()) return false;
		if (!getGamemode()) return false;
		if (!isCorrectGeneralSettings()) return false;
		if (!isCorrectGamemodeSettings()) return false;
		clearSetupStep();
		return true;
	}
	
	public void checkOnSetup() {
		
	}
	
	
	private boolean getYML() {
		File file = new File(plugin.getDataFolder() + File.separator + "maps", arena + ".yml");
		if(!file.exists()){
			setError("Arena doesn't exist.");
			return false;
		}
		storage = new YamlStorage(plugin, "maps", arena);
		config = storage.getConfig();
		return true;
	}
	
	private boolean getGamemode() {
		if (!config.contains("gamemode")) {
			config.set("setupstep", 1);
			setError("No gamemode found.");
			return false;
		}
		String gamemode = config.getString("gamemode");
		switch (gamemode.toLowerCase()) {
		case "ctf":
			gm = Gamemodes.CTF;
			teamGame = true;
			break;
		case "dm":
			gm = Gamemodes.DM;
			teamGame = false;
			break;
		case "lms":
			gm = Gamemodes.LMS;
			teamGame = false;
			break;
		case "lts":
			gm = Gamemodes.LTS;
			teamGame = true;
			break;
		case "spleef":
			gm = Gamemodes.SPLEEF;
			teamGame = false;
			break;
		case "tdm":
			gm = Gamemodes.TDM;
			teamGame = true;
			break;
		default:
			setError("Not a valid gametype.");
			config.set("setupstep", 1);
			return false;
		}
		if (!gamemode.equals(gamemode.toLowerCase())) {
			config.set("gamemode", gamemode.toLowerCase());
		}
		return true;
	}
	
	private boolean isCorrectGeneralSettings() {		
		if (config.contains("respawntime")) {
			maxPlayers = config.getInt("maxplayers");
			if (maxPlayers < 0) hasError = true;
		} else hasError = true;
		
		if (config.contains("respawntime")) {
			respawnTime = config.getInt("respawntime");
			if (respawnTime < 0) hasError = true;
		} else hasError = true;
		
		if (config.contains("scorelimit")) {
			scoreLimit = config.getInt("scorelimit");
			if (scoreLimit < 0) hasError = true;
		} else hasError = true;
		
		if (config.contains("outofboundsarea")) {
			outOfBoundsArea = config.getBoolean("outofboundsarea");
			if (config.contains("outofboundstime")) {
				if (outOfBoundsArea == false) {
					config.set("outofboundstime", null);
					storage.saveConfig();
				} else {
					outOfBoundsTime = config.getInt("outofboundstime");
					if (outOfBoundsTime < 0) hasError = true;
				}
			} else hasError = true;
		} else hasError = true;
		
		if (config.contains("spawnprotection")) {
			spawnProtection = config.getInt("spawnprotection");
			if (spawnProtection < 0) hasError = true;
		} else hasError = true;
		
		if (config.contains("defaultchatchannel")) {
			defaultChatChannel = config.getString("defaultchatchannel");
			switch (defaultChatChannel.toUpperCase()) {
			case "ALL": case "NONE": break;
			case "TEAM":
				if (!teamGame) hasError = true;
				break;
			default:
				hasError = true;
				defaultChatChannel = null;
			}
		} else hasError = true;
		
		if (config.contains("allowitemdrop")) {
			allowItemDrop = config.getBoolean("allowitemdrop");
		} else hasError = true;
		
		if (config.contains("autobalance")) {
			autoBalance = config.getBoolean("autobalance");
		} else hasError = true;
		
		if (config.contains("allowspectate")) {
			allowSpectate = config.getBoolean("allowspectate");
		} else hasError = true;
		
		if (config.contains("allowblockchange")) {
			allowBlockChange = config.getBoolean("allowblockchange");
		} else hasError = true;
		
		if (config.contains("friendlyfire")) {
			if (!teamGame) config.set("friendlyfire", null);
			else friendlyFire = config.getBoolean("friendlyfire");
		} else {
			if (teamGame) hasError = true;
		}
		
		if (hasError) {
			config.set("setupstep", 2);
			setError("Problem in general settings.");
		}
		return !hasError;
	}
	
	@SuppressWarnings("incomplete-switch")
	private boolean isCorrectGamemodeSettings() {
		if (!config.contains("teams")) {
			setError("No teams found.");
			return false;
		}
		ConfigurationSection teamConfig = config.getConfigurationSection("teams");
		int nrOfTeams = teamConfig.getKeys(false).size();
		boolean teamsCorrect = false;
		switch(gm) {
		case CTF: case LTS:	case TDM:
			if (nrOfTeams(nrOfTeams, 2, 9001)) teamsCorrect = true;
			break;
		case DM: case LMS: case SPLEEF:
			if (nrOfTeams(nrOfTeams, 1, 1)) teamsCorrect = true;
			break;
		}
		
		if (teamsCorrect) {
			teams = new ArrayList<>();
			for (String key : teamConfig.getKeys(false)) {
				try {
					int teamNr = Integer.parseInt(key);
					ConfigTeam tempTeam = new ConfigTeam(teamNr);
					if (tempTeam.hasError) {
						setError(tempTeam.getError());
						break;
					}
				} catch(NumberFormatException e) {setError("Not a valid team number."); break;}
			}
		}
		
		switch(gm) {
		case CTF:
			if (config.contains("flags")) {
				for (ConfigTeam team : teams) {
					if (!config.contains("flags." + team.teamNr)) {
						setError("Flag team " + team.teamNr + " not found.");
					}
				}
			} else setError("No flag positions found.");
			break;
		case LTS: case LMS:
			//check for toSpectate after death
			break;
		}
		
		if (hasError) {
			config.set("setupstep", 3);
		}
		
		return !hasError;
	}
	
	private boolean nrOfTeams(int teams, int minTeams, int maxTeams) {
		boolean returnBool = false;
		if (teams < 0) setError("No teams found");
		else if (teams < minTeams) setError("Not enough teams.");
		else if (teams > maxTeams) setError("Too many teams.");
		else returnBool = true;
		return returnBool;
	}
	
	private void setError(String err) {
		errors.add(ChatColor.RED + "-" + err);
		hasError = true;
	}
	
	public String[] getErrors() {
		return (String[]) errors.toArray();
	}
	
	
	private void clearSetupStep() {
		config.set("setupstep", null);
	}
	
	protected class ConfigTeam {
		
		int teamNr;
		String teamName;
		ChatColor teamColor;
		
		boolean hasError;
		String error;
		
		public ConfigTeam(int teamNr) {
			this.teamNr = teamNr;
			teamName = config.getString("teams." + teamNr);			
			if (config.contains("teamcolors." + teamNr)) {
				this.teamColor = ChatColor.getByChar(config.getString("teamcolors." + teamNr));
			} else {
				hasError = true;
				error = "Incorrect teamcolor, team " + teamNr + ".";
			}
			if (!config.contains("classes." + teamNr + ".inventory")) {
				hasError = true;
				error = "No inventory found, team " + teamNr + ".";
			}
			if (!config.contains("classes." + teamNr + ".armor")) {
				hasError = true;
				error = "No armor found, team " + teamNr + ".";
			}
			if (config.contains("spawns." + teamNr)) {
				List<String> spawns = config.getStringList("spawns." + teamNr);
				if (spawns.size() < 1) {
					hasError = true;
					error = "No spawns found, team " + teamNr + ".";
				}
			} else {
				hasError = true;
				error = "No spawns found, team " + teamNr + ".";
			}
		}
		
		public boolean hasError() {
			return hasError;
		}
		
		public String getError() {
			return error;
		}
		
	}
	
}
