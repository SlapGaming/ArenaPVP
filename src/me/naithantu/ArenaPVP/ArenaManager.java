package me.naithantu.ArenaPVP;

import java.util.HashMap;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;

public class ArenaManager {
	HashMap<String, Arena> arenas = new HashMap<String, Arena>();

    //Allplayers contains players and spectators
    HashMap<String, ArenaPlayer> allPlayers = new HashMap<String, ArenaPlayer>();

	public ArenaManager() {

	}

	public HashMap<String, Arena> getArenas() {
		return arenas;
	}
	
	public Arena getFirstArena(){
		return arenas.entrySet().iterator().next().getValue();
	}

	public ArenaPlayer getPlayerByName(String playerName) {
		return allPlayers.get(playerName);
	}

	public void addPlayer(ArenaPlayer arenaPlayer) {
		allPlayers.put(arenaPlayer.getPlayerName(), arenaPlayer);
	}

	public void removePlayer(ArenaPlayer arenaPlayer) {
		allPlayers.remove(arenaPlayer.getPlayerName());
	}

	public HashMap<String, ArenaPlayer> getAllPlayers() {
		return allPlayers;
	}
}
