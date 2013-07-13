package me.naithantu.ArenaPVP.Objects.ArenaExtras;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.naithantu.ArenaPVP.Events.ArenaEvents.EventRespawn;
import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaPlayer;
import me.naithantu.ArenaPVP.Util.Util;

public class ArenaSpawns {
	ArenaManager arenaManager;
	Arena arena;
	FileConfiguration config;

	public ArenaSpawns(ArenaManager arenaManager, Arena arena,
			FileConfiguration config) {
		this.arenaManager = arenaManager;
		this.arena = arena;
		this.config = config;
	}

	public void respawnPlayer(ArenaPlayer arenaPlayer, SpawnType spawnType) {
		Player player = Bukkit.getServer().getPlayer(
				arenaPlayer.getPlayerName());

		String teamID;
		if (spawnType == SpawnType.PLAYER) {
			teamID = Integer.toString(arena.getTeams().indexOf(
					arenaPlayer.getTeam()));
		} else if (spawnType == SpawnType.SPECTATOR) {
			teamID = "spectator";
		} else {
			//TODO Something went wrong, stop the game.
			return;
		}

		List<String> stringLocations = config.getStringList("spawns." + teamID);

		int locationIndex = 0;
		if (stringLocations.size() > 1) {
			Random random = new Random();
			locationIndex = random.nextInt(stringLocations.size());
		}

		Location spawnLocation = Util.getLocationFromString(stringLocations
				.get(locationIndex));
		EventRespawn eventRespawn = new EventRespawn(player, spawnLocation,
				arenaPlayer);
		arena.getGamemode().onPlayerArenaRespawn(eventRespawn);
		if (!eventRespawn.isCancelled()) {
			player.teleport(eventRespawn.getLocation());
		}
	}

	public enum SpawnType {
		SPECTATOR, PLAYER
	}
}
