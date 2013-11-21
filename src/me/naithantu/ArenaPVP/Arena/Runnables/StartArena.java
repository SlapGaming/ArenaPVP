package me.naithantu.ArenaPVP.Arena.Runnables;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaUtil;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSpawns.SpawnType;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class StartArena extends BukkitRunnable {
	Arena arena;
	ArenaUtil arenaUtil;
	int timeLeft = 3;

	public StartArena(Arena arena) {
		this.arena = arena;
		arenaUtil = arena.getArenatUtil();
	}

	public void run() {
		arena.setArenaState(ArenaState.STARTING);
		arenaUtil.sendMessageAll("The game is starting in " + timeLeft + "...");
		timeLeft--;
		if (timeLeft == 0) {
			arenaUtil.sendMessageAll("You have been teleported to your teams spawn point, let the games begin!");
			for (ArenaTeam team : arena.getTeams()) {
				for (ArenaPlayer arenaPlayer : team.getPlayers()) {
					Bukkit.getPlayer(arenaPlayer.getPlayerName()).teleport(arena.getArenaSpawns().getRespawnLocation(Bukkit.getPlayer(arenaPlayer.getPlayerName()), arenaPlayer, SpawnType.PLAYER));
				}
			}
			arena.setArenaState(ArenaState.PLAYING);
			arena.getGamemode().updateTabs();
            arena.getGamemode().onArenaStart();
			this.cancel();
		}
	}
}
