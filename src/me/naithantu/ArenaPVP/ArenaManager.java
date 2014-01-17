package me.naithantu.ArenaPVP;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;

import java.util.HashMap;

public class ArenaManager {
    private static ArenaPVP plugin = ArenaPVP.getInstance();

    private static HashMap<String, Arena> arenas = new HashMap<String, Arena>();

    //Allplayers contains players and spectators
    private static HashMap<String, ArenaPlayer> allPlayers = new HashMap<String, ArenaPlayer>();

	private ArenaManager() {
	}

	public static HashMap<String, Arena> getArenas() {
		return arenas;
	}

    public static Arena getArena(String arenaName){
        for(Arena arena : arenas.values()){
            if(arena.getArenaName().equalsIgnoreCase(arenaName)){
                return arena;
            }
        }
        return null;
    }

    public static void addArena(String arenaName, Arena arena){
        if(arenas.isEmpty()){
            plugin.registerListeners();
        }
        arenas.put(arenaName, arena);
    }

    public static void removeArena(String arenaName){
        arenas.remove(arenaName);
        if(arenas.isEmpty()){
            plugin.unRegisterListeners();
        }
    }

	public static Arena getFirstArena(){
		return arenas.entrySet().iterator().next().getValue();
	}

	public static ArenaPlayer getPlayerByName(String playerName) {
		return allPlayers.get(playerName);
	}

	public static void addPlayer(ArenaPlayer arenaPlayer) {
		allPlayers.put(arenaPlayer.getPlayerName(), arenaPlayer);
	}

	public static void removePlayer(ArenaPlayer arenaPlayer) {
		allPlayers.remove(arenaPlayer.getPlayerName());
	}

	public static HashMap<String, ArenaPlayer> getAllPlayers() {
		return allPlayers;
	}
}
