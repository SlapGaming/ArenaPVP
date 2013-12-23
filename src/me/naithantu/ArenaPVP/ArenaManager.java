package me.naithantu.ArenaPVP;

import java.util.HashMap;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;

public class ArenaManager {
    private ArenaPVP plugin;

    private HashMap<String, Arena> arenas = new HashMap<String, Arena>();

    //Allplayers contains players and spectators
    private HashMap<String, ArenaPlayer> allPlayers = new HashMap<String, ArenaPlayer>();

	public ArenaManager(ArenaPVP plugin) {
        this.plugin = plugin;
	}

	public HashMap<String, Arena> getArenas() {
		return arenas;
	}

    public void addArena(String arenaName, Arena arena){
        if(arenas.isEmpty()){
            plugin.registerListeners();
        }
        arenas.put(arenaName, arena);
    }

    public void removeArena(String arenaName){
        arenas.remove(arenaName);
        if(arenas.isEmpty()){
            plugin.unRegisterListeners();
        }
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
