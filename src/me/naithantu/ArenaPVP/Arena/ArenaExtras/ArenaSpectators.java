package me.naithantu.ArenaPVP.Arena.ArenaExtras;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class ArenaSpectators {
	Arena arena;

	HashMap<Player, ArenaPlayer> spectators = new HashMap<Player, ArenaPlayer>();

    Team team;

	public ArenaSpectators(Arena arena) {
		this.arena = arena;

        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        team = scoreboard.registerNewTeam("spectators");
        team.setCanSeeFriendlyInvisibles(true);
        //TODO Make spectators see other spectators as ghosts (is this possible without adding annoying potion effects?)
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

		//Hide spectator from other spectators and hide other spectators from spectator.
		for (Player player : spectators.keySet()) {
            spectator.hidePlayer(player);
			player.hidePlayer(spectator);
		}

        team.addPlayer(spectator);
 	}

	public void removeSpectator(Player spectator) {		
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

        team.removePlayer(spectator);
	}

	public void removeAllSpectators() {
		Set<Player> tempSpectator = new HashSet<Player>(spectators.keySet());
		for (Player spectator : tempSpectator) {
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
