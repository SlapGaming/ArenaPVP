package me.naithantu.ArenaPVP.Gamemodes.Gamemodes;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.TabController;
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
	private Comparator<ArenaTeam> comp;
	
	public CTF(ArenaPVP plugin, ArenaManager arenaManager, Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns, ArenaUtil arenaUtil, YamlStorage arenaStorage, TabController tabController) {
		super(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
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

	@Override
	public void updateTabs() {
		if (tabController.hasTabAPI()) return; //Currently deactivated <- Remove this line to activate
		if (!tabController.hasTabAPI()) return;
		
	}

	@Override
	public void sortLists() {
		Collections.sort(arena.getTeams(), comp);
	}

	@Override
	protected void createComp() {
		comp = new Comparator<ArenaTeam>() {
			@Override
			public int compare(ArenaTeam o1, ArenaTeam o2) {
				if (o1.getScore() < o2.getScore()) return 1;
				if (o1.getScore() > o2.getScore()) return -1;
				return 0;
			}
		};
	}
}
