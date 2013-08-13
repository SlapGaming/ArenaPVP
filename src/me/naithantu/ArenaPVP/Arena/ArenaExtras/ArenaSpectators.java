package me.naithantu.ArenaPVP.Arena.ArenaExtras;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ArenaSpectators {
	Arena arena;

	HashMap<Player, ArenaPlayer> spectators = new HashMap<Player, ArenaPlayer>();

	public ArenaSpectators(Arena arena) {
		this.arena = arena;
	}

	public HashMap<Player, ArenaPlayer> getSpectators() {
		return spectators;
	}

	public void addSpectator(ArenaPlayer arenaSpectator, Player spectator) {
		//Hide spectator from players.
		for (ArenaTeam team : arena.getTeams()) {
			List<ArenaPlayer> players = new ArrayList<ArenaPlayer>(team.getPlayers());
			for (ArenaPlayer arenaPlayer : players) {
				Player player = Bukkit.getPlayerExact(arenaPlayer.getPlayerName());
				player.hidePlayer(spectator);
			}
		}

		//Hide spectator from other spectators.
		for (Player player : spectators.keySet()) {
			player.hidePlayer(spectator);
		}
		
		spectators.put(spectator, arenaSpectator);
	}

	public void removeSpectator(Player spectator) {
		spectators.remove(spectator);
		
		//Show spectator to other players.
		for (ArenaTeam team : arena.getTeams()) {
			List<ArenaPlayer> players = new ArrayList<ArenaPlayer>(team.getPlayers());
			for (ArenaPlayer arenaPlayer : players) {
				Player player = Bukkit.getPlayerExact(arenaPlayer.getPlayerName());
				player.showPlayer(spectator);
			}
		}

		//Show spectator to other spectators and show other spectators to spectator.
		for (Player player : spectators.keySet()) {
			player.showPlayer(spectator);
			spectator.showPlayer(player);
		}
	}

	public void removeAllSpectators() {
		for (Player spectator : spectators.keySet()) {
			removeSpectator(spectator);
		}
	}

	public void onPlayerJoin(Player player) {
		for (Player spectator : spectators.keySet()) {
			player.hidePlayer(spectator);
		}
	}

	public void onPlayerLeave(Player player) {
		for (Player spectator : spectators.keySet()) {
			player.showPlayer(spectator);
		}
	}
}
