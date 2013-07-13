package me.naithantu.ArenaPVP.Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArenaManager {
	List<Arena> arenas = new ArrayList<Arena>();
	HashMap<String, ArenaPlayer> allPlayers = new HashMap<String, ArenaPlayer>();

	public ArenaManager() {

	}

	public List<Arena> getArenas() {
		return arenas;
	}

	public ArenaPlayer getPlayerByName(String playerName) {
		return allPlayers.get(playerName);
	}

}
