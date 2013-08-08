package me.naithantu.ArenaPVP.Gamemodes.Gamemodes;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaPlayerState;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSettings;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSpawns;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaUtil;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.Util.Util;

public class CTF extends Gamemode {
	HashMap<String, ArenaTeam> flags = new HashMap<String, ArenaTeam>();

	public CTF(ArenaPVP plugin, ArenaManager arenaManager, Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns, ArenaUtil arenaUtil, YamlStorage arenaStorage) {
		super(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage);
	}

	@Override
	public String getName() {
		return "CTF";
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent event, ArenaPlayer arenaPlayer) {
		super.onPlayerMove(event, arenaPlayer);
		if (arena.getArenaState() == ArenaState.PLAYING && arenaPlayer.getPlayerState() == ArenaPlayerState.PLAYING) {
			Player player = event.getPlayer();
			String playerName = player.getName();
			//If player has flag, check if he can capture it.
			if (flags.containsKey(playerName)) {
				ArenaTeam team = arenaPlayer.getTeam();
				Location playerLocation = player.getLocation();
				Location flagLocation = Util.getLocationFromString(arenaStorage.getConfig().getString("gamemodes.ctf." + team.getTeamNumber()));
				if (playerLocation.distanceSquared(flagLocation) < 1) {
					if(getFlagCarrier(team) == null){
						flags.remove(playerName);
						arenaUtil.sendMessageAll(playerName + " has captured the " + team.getTeamName() + " flag!");
						team.addScore();
						if(team.getScore() >= settings.getScoreLimit()){
							arena.stopGame(team);
						}
						super.sendScoreAll();
					} else {
						Util.msg(player, "You can not capture a flag when the enemy has your flag!");
					}
				}
			} else {
				for (ArenaTeam arenaTeam : arena.getTeams()) {
					// Take enemy flag (can't take own flag & flag can't already be taken).
					if (!arenaPlayer.getTeam().equals(arenaTeam) && getFlagCarrier(arenaTeam) == null) {
						Location playerLocation = player.getLocation();
						Location flagLocation = Util.getLocationFromString(arenaStorage.getConfig().getString("gamemodes.ctf." + arenaTeam.getTeamNumber()));
						if (playerLocation.distanceSquared(flagLocation) < 1) {
							flags.put(player.getName(), arenaTeam);
							arenaUtil.sendMessageAll(playerName + " has taken the " + arenaTeam.getTeamName() + " flag!");
						}
					}
				}
			}
		}
	}

	@Override
	public void onPlayerDeath(PlayerDeathEvent event, ArenaPlayer arenaPlayer) {
		super.onPlayerDeath(event, arenaPlayer);
		String playerName = event.getEntity().getName();
		if (flags.containsKey(playerName)) {
			flags.remove(playerName);
			arenaUtil.sendMessageAll("The " + arenaPlayer.getTeam().getTeamName() + " flag has been returned!");
		}
	}
	
	@Override
	public void onPlayerQuit(PlayerQuitEvent event, ArenaPlayer arenaPlayer){
		super.onPlayerQuit(event, arenaPlayer);
		String playerName = event.getPlayer().getName();
		if (flags.containsKey(playerName)) {
			flags.remove(playerName);
			arenaUtil.sendMessageAll("The " + arenaPlayer.getTeam().getTeamName() + " flag has been returned!");
		}
	}

	public String getFlagCarrier(ArenaTeam team) {
		for (Entry<String, ArenaTeam> entry : flags.entrySet()) {
			if (entry.getValue().equals(team)) {
				return entry.getKey();
			}
		}
		return null;
	}
}
