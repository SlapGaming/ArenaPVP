package me.naithantu.ArenaPVP.Arena.ArenaExtras;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class ArenaSpectators {
    Arena arena;

    HashMap<Player, ArenaPlayer> spectators = new HashMap<Player, ArenaPlayer>();

    public ArenaSpectators(Arena arena) {
        this.arena = arena;
    }

    public HashMap<Player, ArenaPlayer> getSpectators() {
        return spectators;
    }

    public void hideSpectator(Player spectator) {
        //Hide spectator from players.
        for (ArenaTeam team : arena.getTeams()) {
            List<ArenaPlayer> players = new ArrayList<ArenaPlayer>(team.getPlayers());
            for (ArenaPlayer arenaPlayer : players) {
                Player player = Bukkit.getPlayerExact(arenaPlayer.getPlayerName());
                player.hidePlayer(spectator);
                if (arenaPlayer.getPlayerState() == ArenaPlayerState.SPECTATING) {
                    spectator.hidePlayer(player);
                }
            }
        }

        //Hide spectator from other spectators and hide other spectators from spectator.
        for (Player player : spectators.keySet()) {
            spectator.hidePlayer(player);
            player.hidePlayer(spectator);
        }
    }

    public void showSpectator(Player spectator) {
        //Show spectator to other players and show other players to spectator.
        for (ArenaTeam team : arena.getTeams()) {
            List<ArenaPlayer> players = new ArrayList<ArenaPlayer>(team.getPlayers());
            for (ArenaPlayer arenaPlayer : players) {
                Player player = Bukkit.getPlayerExact(arenaPlayer.getPlayerName());
                player.showPlayer(spectator);
                spectator.showPlayer(player);
            }
        }

        //Show spectator to other spectators and show other spectators to spectator.
        for (Player player : spectators.keySet()) {
            player.showPlayer(spectator);
            spectator.showPlayer(player);
        }
    }

    public void removeAllSpectators() {
        Set<Player> tempSpectator = new HashSet<Player>(spectators.keySet());
        for (Player spectator : tempSpectator) {
            showSpectator(spectator);
        }
    }

    public void onPlayerJoin(Player player) {
        //Check if spectating is actually enabled.
        if (arena.getSettings().isAllowSpectate()) {
            //Hide spectators
            for (Player spectator : spectators.keySet()) {
                player.hidePlayer(spectator);
            }

            //Hide hidden players.
            for (ArenaTeam team : arena.getTeams()) {
                List<ArenaPlayer> players = new ArrayList<ArenaPlayer>(team.getPlayers());
                for (ArenaPlayer arenaPlayer : players) {
                    Player pvpPlayer = Bukkit.getPlayerExact(arenaPlayer.getPlayerName());
                    if (arenaPlayer.getPlayerState() == ArenaPlayerState.SPECTATING) {
                        player.hidePlayer(pvpPlayer);
                    }
                }
            }
        }
    }
}
