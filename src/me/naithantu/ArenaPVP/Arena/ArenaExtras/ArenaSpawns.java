package me.naithantu.ArenaPVP.Arena.ArenaExtras;

import java.util.List;
import java.util.Random;

import me.naithantu.ArenaPVP.Storage.YamlStorage;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Events.ArenaEvents.EventRespawn;
import me.naithantu.ArenaPVP.Util.Util;

public class ArenaSpawns {
	ArenaPVP plugin;
	ArenaManager arenaManager;
	Arena arena;
	ArenaSettings settings;
	YamlStorage arenaStorage;
    FileConfiguration arenaConfig;

	public ArenaSpawns(ArenaPVP plugin, ArenaManager arenaManager, Arena arena, ArenaSettings settings, YamlStorage arenaStorage) {
		this.plugin = plugin;
		this.arenaManager = arenaManager;
		this.arena = arena;
		this.settings = settings;
		this.arenaStorage = arenaStorage;
        arenaConfig = arenaStorage.getConfig();
	}

	public Location getRespawnLocation(Player player, ArenaPlayer arenaPlayer, SpawnType spawnType) {
		String teamID;
		if (spawnType == SpawnType.PLAYER) {
			teamID = Integer.toString(arenaPlayer.getTeam().getTeamNumber());
		} else if (spawnType == SpawnType.SPECTATOR) {
			teamID = "spectator";
		} else {
			return null;
		}

        ConfigurationSection configurationSection = arenaConfig.getConfigurationSection("spawns." + teamID);
        int numberOfSpawns = configurationSection.getKeys(false).size();

		int locationIndex = 0;
		if (numberOfSpawns > 1) {
			Random random = new Random();
			locationIndex = random.nextInt(numberOfSpawns);
		}

		Location spawnLocation = Util.getLocation(arenaStorage, "spawns." + teamID + "." + locationIndex);
		EventRespawn eventRespawn = new EventRespawn(player, spawnLocation, arenaPlayer, spawnType);
		arena.getGamemode().onPlayerArenaRespawn(eventRespawn);
		if (!eventRespawn.isCancelled()) {
			return eventRespawn.getLocation();
		}
		return null;
	}

	public enum SpawnType {
		SPECTATOR, PLAYER
	}
}
