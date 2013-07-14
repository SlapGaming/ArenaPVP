package me.naithantu.ArenaPVP.Gamemodes;

import me.naithantu.ArenaPVP.Events.ArenaEvents.EventJoinGame;
import me.naithantu.ArenaPVP.Events.ArenaEvents.EventRespawn;
import me.naithantu.ArenaPVP.Events.BukkitEvents.ArenaPlayerDamageEvent;
import me.naithantu.ArenaPVP.Events.BukkitEvents.ArenaPlayerDeathEvent;
import me.naithantu.ArenaPVP.Events.BukkitEvents.ArenaPlayerRespawnEvent;
import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaPlayer;
import me.naithantu.ArenaPVP.Objects.ArenaTeam;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaSettings;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaSpawns;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaSpawns.SpawnType;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Gamemode {
	protected ArenaManager arenaManager;
	protected Arena arena;
	protected ArenaSettings settings;
	protected ArenaSpawns arenaSpawns;

	public Gamemode(ArenaManager arenaManager, Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns) {
		this.arenaManager = arenaManager;
		this.arena = arena;
		this.settings = settings;
		this.arenaSpawns = arenaSpawns;
	}

	public void onPlayerDeath(ArenaPlayerDeathEvent event) {
		Player player = event.getEntity();
		ArenaPlayer arenaPlayer = event.getArenaPlayer();

		// Add death and kill to players.
		arenaPlayer.getPlayerScore().addDeath();
		if (player.getKiller() != null)
			arenaManager.getPlayerByName(player.getKiller().getName()).getPlayerScore().addKill();
	}

	public void onPlayerRespawn(ArenaPlayerRespawnEvent event) {
		arenaSpawns.respawnPlayer(event.getArenaPlayer(), SpawnType.SPECTATOR);
	}

	public void onPlayerDamage(ArenaPlayerDamageEvent event) {

	}

	public void onPlayerMove(PlayerMoveEvent event) {

	}

	public void onPlayerQuit(PlayerQuitEvent event) {

	}

	// Arena made events.
	public void onPlayerJoinArena(EventJoinGame event) {
		ArenaState arenaState = arena.getArenaState();
		if (arenaState == ArenaState.WARMUP || arenaState == ArenaState.LOBBY) {
			ArenaTeam teamToJoin = event.getTeam();
			if (teamToJoin == null || settings.getMaxPlayers() > 0 && teamToJoin.getPlayers().size() >= settings.getMaxPlayers()) {
				for (ArenaTeam team : arena.getTeams()) {
					if (teamToJoin == null || team.getPlayers().size() < teamToJoin.getPlayers().size()) {
						teamToJoin = team;
					}
				}
			}
		}
		event.setCancelled(true);
	}

	public void onPlayerArenaRespawn(EventRespawn event) {

	}
}
