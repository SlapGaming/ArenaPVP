package me.naithantu.ArenaPVP.Objects.ArenaExtras;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Events.ArenaEvents.EventRespawn;
import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaPlayer;
import me.naithantu.ArenaPVP.Util.Util;

public class ArenaSpawns {
	ArenaPVP plugin;
	ArenaManager arenaManager;
	Arena arena;
	ArenaSettings settings;
	FileConfiguration config;

	HashMap<String, Integer> respawnTimers = new HashMap<String, Integer>();

	BukkitScheduler scheduler = Bukkit.getScheduler();

	public ArenaSpawns(ArenaPVP plugin, ArenaManager arenaManager, Arena arena, ArenaSettings settings, FileConfiguration config) {
		this.plugin = plugin;
		this.arenaManager = arenaManager;
		this.arena = arena;
		this.settings = settings;
		this.config = config;
	}

	public void respawnPlayer(ArenaPlayer arenaPlayer, SpawnType spawnType) {
		Player player = Bukkit.getServer().getPlayer(arenaPlayer.getPlayerName());

		String teamID;
		if (spawnType == SpawnType.PLAYER) {
			teamID = Integer.toString(arena.getTeams().indexOf(arenaPlayer.getTeam()));
		} else if (spawnType == SpawnType.SPECTATOR) {
			teamID = "spectator";
		} else {
			return;
		}

		List<String> stringLocations = config.getStringList("spawns." + teamID);

		int locationIndex = 0;
		if (stringLocations.size() > 1) {
			Random random = new Random();
			locationIndex = random.nextInt(stringLocations.size());
		}

		Location spawnLocation = Util.getLocationFromString(stringLocations.get(locationIndex));
		EventRespawn eventRespawn = new EventRespawn(player, spawnLocation, arenaPlayer);
		arena.getGamemode().onPlayerArenaRespawn(eventRespawn);
		if (!eventRespawn.isCancelled()) {
			player.teleport(eventRespawn.getLocation());
		}
	}

	public void addRespawnTimer(final ArenaPlayer arenaPlayer, final SpawnType spawnType){
		String playerName = arenaPlayer.getPlayerName();
		if(respawnTimers.containsKey(playerName)){
			scheduler.cancelTask(respawnTimers.get(playerName));
		}
		
		if(settings.getRespawnTime() == 0){
			respawnPlayer(arenaPlayer, spawnType);
		} else {
			int taskID = scheduler.scheduleSyncDelayedTask(plugin, new Runnable(){
				@Override
				public void run() {
					respawnTimers.remove(arenaPlayer);
					arenaPlayer.setPlayerState(ArenaPlayerState.PLAYING);
					respawnPlayer(arenaPlayer, spawnType);
				}
			}, settings.getRespawnTime());
			respawnTimers.put(playerName, taskID);
			arenaPlayer.setPlayerState(ArenaPlayerState.RESPAWNING);
		}
		
		
	}

	public enum SpawnType {
		SPECTATOR, PLAYER
	}
}
