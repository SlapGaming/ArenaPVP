package me.naithantu.ArenaPVP.Arena;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.TabController;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaArea;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaChat;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaGamemode;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaPlayerState;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSettings;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSpawns;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSpectators;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaUtil;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSpawns.SpawnType;
import me.naithantu.ArenaPVP.Arena.Runnables.StartArena;
import me.naithantu.ArenaPVP.Events.ArenaEvents.EventJoinArena;
import me.naithantu.ArenaPVP.Events.ArenaEvents.EventLeaveArena;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.Util.Util;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;

public class Arena {
	ArenaPVP plugin;
	ArenaManager arenaManager;
	ArenaSpawns arenaSpawns;
	ArenaSettings settings;
	ArenaUtil arenaUtil;
	ArenaArea arenaArea;
	ArenaChat arenaChat;
	ArenaSpectators arenaSpectators;

	List<ArenaTeam> teams = new ArrayList<ArenaTeam>();
	List<String> offlinePlayers = new ArrayList<String>();
	String nickName;

	String arenaName;
	ArenaState arenaState;
	Gamemode gamemode;

	YamlStorage arenaStorage;
	FileConfiguration arenaConfig;

	TabController tabController;

	public Arena(ArenaPVP plugin, ArenaManager arenaManager, String arenaName) {
		this.plugin = plugin;
		this.arenaManager = arenaManager;
		this.arenaName = arenaName;
		arenaState = ArenaState.BEFORE_JOIN;
		arenaStorage = new YamlStorage(plugin, "maps", arenaName);
		arenaStorage.copyDefaultConfig();
		arenaConfig = arenaStorage.getConfig();

		nickName = arenaConfig.getString("nickname");

		settings = new ArenaSettings(arenaConfig);
		arenaSpawns = new ArenaSpawns(plugin, arenaManager, this, settings, arenaConfig);
		arenaUtil = new ArenaUtil(this);
		arenaArea = new ArenaArea(plugin, this, settings, arenaConfig);
		arenaSpectators = new ArenaSpectators(this);
		arenaChat = new ArenaChat(this);

		tabController = plugin.getTabController();

		initializeArena();
	}

	public String getNickName() {
		return nickName;
	}

	public ArenaSpawns getArenaSpawns() {
		return arenaSpawns;
	}

	public ArenaSettings getSettings() {
		return settings;
	}

	public ArenaUtil getArenatUtil() {
		return arenaUtil;
	}

	public ArenaArea getArenaArea() {
		return arenaArea;
	}

	public ArenaSpectators getArenaSpectators() {
		return arenaSpectators;
	}

	public ArenaChat getArenaChat() {
		return arenaChat;
	}

	public YamlStorage getArenaStorage() {
		return arenaStorage;
	}

	public String getArenaName() {
		return arenaName;
	}

	public Gamemode getGamemode() {
		return gamemode;
	}

	public List<String> getOfflinePlayers() {
		return offlinePlayers;
	}

	public ArenaState getArenaState() {
		return arenaState;
	}

	public void setArenaState(ArenaState arenaState) {
		this.arenaState = arenaState;
	}

	public void initializeArena() {
        // Create teams with proper names.
        for (String teamNumber : arenaConfig.getConfigurationSection("teams").getKeys(false)) {
            ArenaTeam team = new ArenaTeam(plugin, arenaConfig, Integer.parseInt(teamNumber));
            teams.add(team);
        }

		String gamemodeName = arenaConfig.getString("gamemode");

		// Create gamemode.
		gamemode = ArenaGamemode.getGamemode(plugin, arenaManager, this, settings, arenaSpawns, arenaUtil, arenaStorage, gamemodeName, tabController);
	}

	public void joinSpectate(Player player) {
		//Teleport first to avoid problems with MVInventories
		YamlStorage playerStorage = new YamlStorage(plugin, "players", player.getName());
		Configuration playerConfig = playerStorage.getConfig();
		playerConfig.set("location", Util.getStringFromLocation(player.getLocation()));
		Util.playerJoin(player, playerStorage);
		ArenaPlayer arenaPlayer = new ArenaPlayer(plugin, player, this, null);
		arenaPlayer.setPlayerState(ArenaPlayerState.SPECTATING);
		arenaManager.addPlayer(arenaPlayer);
		player.teleport(arenaSpawns.getRespawnLocation(player, arenaPlayer, SpawnType.SPECTATOR));
		arenaSpectators.addSpectator(arenaPlayer, player);
		arenaSpectators.getSpectators().put(player, arenaPlayer);
		changeToSpectate(player, arenaPlayer);
		playerStorage.saveConfig();
		gamemode.updateTabs();
	}

	public void leaveSpectate(Player player, ArenaPlayer arenaPlayer) {
		YamlStorage playerStorage = new YamlStorage(plugin, "players", player.getName());
		arenaManager.removePlayer(arenaPlayer);
		arenaSpectators.removeSpectator(player);
		arenaSpectators.getSpectators().remove(player);
		Configuration playerConfig = playerStorage.getConfig();
		Util.playerLeave(player, playerStorage);
		player.teleport(Util.getLocationFromString(playerConfig.getString("location")));
		playerConfig.set("location", null);
		playerStorage.saveConfig();
		gamemode.clearTab(player);
		gamemode.updateTabs();
	}

	public void changeToSpectate(Player player, ArenaPlayer arenaPlayer) {
		player.setAllowFlight(true);
		player.setFlying(true);
		arenaSpectators.addSpectator(arenaPlayer, player);
	}

	public boolean joinGame(Player player, ArenaTeam teamToJoin) {
		EventJoinArena event = new EventJoinArena(player, teamToJoin);
		gamemode.onPlayerJoinArena(event);
		if (!event.isCancelled()) {
			Util.msg(player, "You joined team " + event.getTeam().getTeamName() + "!");

			//Teleport first to avoid problems with MVInventories
			YamlStorage playerStorage = new YamlStorage(plugin, "players", player.getName());
			Configuration playerConfig = playerStorage.getConfig();
			playerConfig.set("location", Util.getStringFromLocation(player.getLocation()));
			event.getTeam().joinTeam(player, arenaManager, this);

			Util.playerJoin(player, playerStorage);
			playerStorage.saveConfig();
			arenaSpectators.onPlayerJoin(player);
			return true;
		}
		return false;
	}

	public void leaveGame(final ArenaPlayer arenaPlayer) {
		//Delay in case player was killed by arrow.
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				EventLeaveArena event = new EventLeaveArena(arenaPlayer);
				gamemode.onPlayerLeaveArena(event);
				if (!event.isCancelled()) {
					Player player = Bukkit.getPlayerExact(arenaPlayer.getPlayerName());
					if (player != null) {
						arenaSpectators.onPlayerLeave(player);
						YamlStorage playerStorage = new YamlStorage(plugin, "players", player.getName());
						Configuration playerConfig = playerStorage.getConfig();
						if (player.isDead()) {
							playerConfig.set("hastoleave", true);
						} else {
							Util.playerLeave(player, playerStorage);
							player.teleport(Util.getLocationFromString(playerConfig.getString("location")));
							playerConfig.set("location", null);
						}
						playerStorage.saveConfig();
					}
					arenaPlayer.getTeam().leaveTeam(arenaManager, arenaPlayer, player);
				}
			}
		}, 1);
	}

	public void startGame() {
		StartArena startArena = new StartArena(this);
		startArena.runTaskTimer(plugin, 0, 20);
	}

	public void stopGame(ArenaPlayer winPlayer) {
		if (winPlayer != null) {
			arenaUtil.sendMessageAll(winPlayer.getPlayerName() + " has won the game!");
		}
		stopGame();
	}

	public void stopGame(ArenaTeam winTeam) {
		if (winTeam != null) {
			arenaUtil.sendMessageAll("Team " + winTeam.getTeamName() + " has won the game!");
		}
		stopGame();
	}

	public void stopGame() {
		for (ArenaTeam team : teams) {
			List<ArenaPlayer> players = new ArrayList<ArenaPlayer>(team.getPlayers());
			for (ArenaPlayer arenaPlayer : players) {
				leaveGame(arenaPlayer);
			}
		}
		
		Set<Entry<Player, ArenaPlayer>> spectators = new HashSet<>(arenaSpectators.getSpectators().entrySet());
		for (Entry<Player, ArenaPlayer> entry : spectators) {
			leaveSpectate(entry.getKey(), entry.getValue());
		}
		
		File schematic = new File(plugin.getDataFolder() + File.separator + "maps", arenaName + ".schematic");
		if (schematic.exists()) {
			EditSession editSession = new EditSession(new BukkitWorld(Bukkit.getWorld(arenaConfig.getString("schematicworld"))), 999999999);
			SchematicFormat format = SchematicFormat.getFormat(schematic);

			CuboidClipboard cuboidClipboard;
			try {
				cuboidClipboard = format.load(schematic);
			} catch (IOException | DataException e) {
				e.printStackTrace();
				return;
			}

			try {
				Vector pos = cuboidClipboard.getOrigin();
				cuboidClipboard.place(editSession, pos, false);
			} catch (MaxChangedBlocksException e) {
				e.printStackTrace();
			}
		}
		arenaManager.getArenas().remove(arenaName);
	}

	public void addTeam(ArenaTeam team) {
		teams.add(team);
	}

	public List<ArenaTeam> getTeams() {
		return teams;
	}

	public ArenaTeam getTeam(int teamNumber) {
		for (ArenaTeam team : teams) {
			if (team.getTeamNumber() == teamNumber) {
				return team;
			}
		}
		return null;
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
