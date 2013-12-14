package me.naithantu.ArenaPVP.Arena.Runnables;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaUtil;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSpawns.SpawnType;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StartArena extends BukkitRunnable {
	Arena arena;
	ArenaUtil arenaUtil;
	int timeLeft = 3;

	public StartArena(Arena arena) {
		this.arena = arena;
		arenaUtil = arena.getArenatUtil();

        arena.setArenaState(ArenaState.STARTING);
        for(ArenaTeam team: arena.getTeams()){
            for(ArenaPlayer arenaPlayer: team.getPlayers()){
                Player player = Bukkit.getPlayerExact(arenaPlayer.getPlayerName());
                player.setWalkSpeed(0.0F);
            }
        }
    }

	public void run() {
		arenaUtil.sendMessageAll("The game is starting in " + timeLeft + "...");
		timeLeft--;
		if (timeLeft == 0) {
			arenaUtil.sendMessageAll("You have been teleported to your teams spawn point, let the games begin!");
			for (ArenaTeam team : arena.getTeams()) {
				for (ArenaPlayer arenaPlayer : team.getPlayers()) {
                    Player player = Bukkit.getPlayerExact(arenaPlayer.getPlayerName());
					player.teleport(arena.getArenaSpawns().getRespawnLocation(player, arenaPlayer, SpawnType.PLAYER));
                    player.setWalkSpeed(0.2F);
				}
			}
			arena.setArenaState(ArenaState.PLAYING);
			arena.getGamemode().updateTabs();
            arena.getGamemode().onArenaStart();
			this.cancel();
		}
	}
}
