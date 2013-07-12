package me.naithantu.ArenaPVP.Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Gamemodes.Gamemodes.TDM;

public class ArenaManager {
	List<Arena> arenas = new ArrayList<Arena>();
	HashMap<String, ArenaPlayer> allPlayers = new HashMap<String, ArenaPlayer>();
	
	public ArenaManager(){
		
	}
	
	public ArenaPlayer getPlayerByName(String playerName){
		return allPlayers.get(playerName);
	}
	
	
}
