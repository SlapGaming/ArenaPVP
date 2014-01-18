package me.naithantu.ArenaPVP.Arena.ArenaExtras;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;
import me.naithantu.ArenaPVP.Util.Util;
import org.bukkit.Bukkit;

public class ArenaUtil {
	Arena arena;

	public ArenaUtil(Arena arena) {
		this.arena = arena;
	}

    public void sendMessageAllExcept(String message, String exceptPlayer){
        for(ArenaTeam team: arena.getTeams())
            sendMessageTeamExcept(message, team, exceptPlayer);
        sendMessageSpectatorsExcept(message, exceptPlayer);
    }

    public void sendMessageTeamExcept(String message, ArenaTeam team, String exceptPlayer){
        for(ArenaPlayer arenaPlayer : team.getPlayers()){
            if(!arenaPlayer.getPlayerName().equals(exceptPlayer))
                Util.msg(Bukkit.getPlayerExact(arenaPlayer.getPlayerName()), message);
        }
    }

    public void sendMessageSpectatorsExcept(String message, String exceptPlayer) {
        for(ArenaPlayer arenaPlayer : arena.getArenaSpectators().getSpectators().values())
            if(!arenaPlayer.getPlayerName().equals(exceptPlayer))
                Util.msg(Bukkit.getPlayerExact(arenaPlayer.getPlayerName()), message);
    }

	public void sendMessageAll(String message) {
		for (ArenaTeam team : arena.getTeams())
			sendMessageTeam(message, team);
        sendMessageSpectators(message);
	}

	public void sendMessageTeam(String message, ArenaTeam team) {
		for (ArenaPlayer arenaPlayer : team.getPlayers())
			Util.msg(Bukkit.getPlayerExact(arenaPlayer.getPlayerName()), message);
	}

    public void sendMessageSpectators(String message){
        for(ArenaPlayer arenaPlayer : arena.getArenaSpectators().getSpectators().values())
            Util.msg(Bukkit.getPlayerExact(arenaPlayer.getPlayerName()), message);
    }

	public void sendNoPrefixMessageAll(String message) {
		for (ArenaTeam team : arena.getTeams())
			sendNoPrefixMessageTeam(message, team);
        sendNoPrefixMessageSpectators(message);
	}

	public void sendNoPrefixMessageTeam(String message, ArenaTeam team) {
		for (ArenaPlayer arenaPlayer : team.getPlayers())
			Bukkit.getPlayerExact(arenaPlayer.getPlayerName()).sendMessage(message);
	}

    public void sendNoPrefixMessageSpectators(String message){
        for(ArenaPlayer arenaPlayer : arena.getArenaSpectators().getSpectators().values())
            Bukkit.getPlayerExact(arenaPlayer.getPlayerName()).sendMessage(message);
    }
}
