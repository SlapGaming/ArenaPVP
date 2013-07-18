package me.naithantu.ArenaPVP.Gamemodes;

import me.naithantu.ArenaPVP.Commands.AbstractCommand;
import me.naithantu.ArenaPVP.Events.ArenaEvents.EventJoinArena;
import me.naithantu.ArenaPVP.Events.ArenaEvents.EventLeaveArena;
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
import me.naithantu.ArenaPVP.Util.Util;

import org.bukkit.command.CommandSender;
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
	
	public void sendScore(CommandSender sender, ArenaPlayer arenaPlayer){
		Util.msg(sender, "Score:");
		for(ArenaTeam team: arena.getTeams()){
			Util.msg(sender, "Team " + team.getTeamName() + ":" + team.getScore());
		}
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
		ArenaPlayer arenaPlayer = event.getArenaPlayer();
		if(settings.getRespawnTime() == 0){
			if(arenaPlayer.getArena().getArenaState() != ArenaState.PLAYING){
				event.setRespawnLocation(arenaSpawns.getRespawnLocation(arenaPlayer, SpawnType.SPECTATOR));
			} else {
				event.setRespawnLocation(arenaSpawns.getRespawnLocation(arenaPlayer, SpawnType.PLAYER));
			}
		} else {
			if(arenaPlayer.getArena().getArenaState() != ArenaState.PLAYING){
				arenaSpawns.addRespawnTimer(arenaPlayer, SpawnType.SPECTATOR);
			} else {
				arenaSpawns.addRespawnTimer(arenaPlayer, SpawnType.PLAYER);
			}
		}
	}

	public void onPlayerDamage(ArenaPlayerDamageEvent event) {

	}

	public void onPlayerMove(PlayerMoveEvent event) {

	}

	public void onPlayerQuit(PlayerQuitEvent event) {

	}

	// Arena made events.
	public void onPlayerJoinArena(EventJoinArena event) {
		ArenaState arenaState = arena.getArenaState();
		if (arenaState == ArenaState.WARMUP || arenaState == ArenaState.LOBBY) {
			ArenaTeam teamToJoin = event.getTeam();
			if (teamToJoin == null || settings.getMaxPlayers() > 0 && teamToJoin.getPlayers().size() >= settings.getMaxPlayers()) {
				for (ArenaTeam team : arena.getTeams()) {
					if (teamToJoin == null || team.getPlayers().size() < teamToJoin.getPlayers().size()) {
						teamToJoin = team;
					}
					event.setTeam(teamToJoin);
					return;
				}
			}
		}
		event.setCancelled(true);
	}
	
	public void onPlayerLeaveArena(EventLeaveArena event){
		
	}

	public void onPlayerArenaRespawn(EventRespawn event) {

	}

	public AbstractCommand executeCommand(String command) {
		return null;
	}
}
