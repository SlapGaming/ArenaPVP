package me.naithantu.ArenaPVP.Gamemodes.Gamemodes;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSpawns;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaUtil;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.Settings.ArenaSettings;
import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.TabController;
import me.naithantu.ArenaPVP.Util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.mcsg.double0negative.tabapi.TabAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FFA extends Gamemode {

	private Comparator<ArenaPlayer> comp;
	
	public FFA(Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns, ArenaUtil arenaUtil, YamlStorage arenaStorage, TabController tabController) {
		super(arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController, Gamemodes.FFA);
		createComp();
	}

	@Override
	public String getName(){
		return "Free for All";
	}
	
	@Override
	public boolean isTeamGame() {
		return false;
	}
	
	@Override
	public void onPlayerDeath(PlayerDeathEvent event, ArenaPlayer arenaPlayer){
		super.onPlayerDeath(event, arenaPlayer);
		Player killer = event.getEntity().getKiller();
		if(killer != null){
			ArenaPlayer arenaKiller = ArenaManager.getPlayerByName(killer.getName());
			if (arenaKiller.getPlayerScore().getKills() >= settings.getScoreLimit()) {
				arena.stopGame(arenaKiller);
			} else {
				sortLists();
				updateTabs();
			}
		}
	}
	
	
	@Override
	public void updateTabs() {
		if (!tabController.hasTabAPI()) return;
		String gameStatus = Util.capitalizeFirstLetter(arena.getArenaState().toString());
		String arenaName = arena.getArenaName();
		
		List<ArenaPlayer> players = arena.getTeams().get(0).getPlayers();
		String nrOfPlayers = players.size() + " Players";
		String nrOfSpectators = ChatColor.GRAY + "" + arena.getArenaSpectators().getSpectators().size() + " Spectators";
		
		String[] playerTab; int x = 0;
		if (arena.getArenaState() == ArenaState.PLAYING) {
			if (players.size() > 10) {
				playerTab = new String[11 * 3];
			} else {
				playerTab = new String[players.size() * 3];
			}
			int rank = 1; 
			List<String> kills = new ArrayList<>();
			for (ArenaPlayer player : players) {
				playerTab[x] = ChatColor.GRAY + "Rank " + rank + "  ->"; rank++; x++;
				playerTab[x] = player.getPlayerName(); x++;
				String killString = player.getPlayerScore().getKills() + " Kills";
				while (kills.contains(killString)) {
					killString = killString + " ";
				}
				kills.add(killString);
				playerTab[x] = ChatColor.RED + killString; x++;
				if (x > 33) break;
			}
		} else {
			if (players.size() > 32) {
				playerTab = new String[33];
			} else {
				playerTab = new String[players.size()];
			}
			for (ArenaPlayer player : players) {
				playerTab[x] = player.getPlayerName();
				x++;
				if (x > 33) break;
			}
		}
		
		int rank = 1;
		for (ArenaPlayer player : players) {
			Player p = Bukkit.getPlayerExact(player.getPlayerName());
			if (p != null) {
				setTabPlayer(p, gameStatus, arenaName, nrOfPlayers, nrOfSpectators, false, player.getPlayerScore().getKills() + " Kills", "Rank " + rank, playerTab);
			}
			rank++;
		}
		
		for (Player p : arena.getArenaSpectators().getSpectators().keySet()) {
			setTabPlayer(p, gameStatus, arenaName, nrOfPlayers, nrOfSpectators, true, null, null, playerTab);
		}
		TabAPI.updateAll();
	}
	
	private void setTabPlayer(Player p, String status, String arena, String players, String spectators, boolean specator, String kills, String rank, String[] playersTab) {
		int row = tabController.setTopTab(p, Gamemodes.FFA); int colom = 0;
		TabAPI.setTabString(plugin, p, 2, 1, status);
		TabAPI.setTabString(plugin, p, 3, 1, arena);
		TabAPI.setTabString(plugin, p, 4, 1, players);
		TabAPI.setTabString(plugin, p, 4, 2, spectators);
		if (specator) {
			TabAPI.setTabString(plugin, p, 5, 1, ChatColor.GRAY + "Spectator");
		} else {
			TabAPI.setTabString(plugin, p, 5, 1, ChatColor.GREEN + kills);
			TabAPI.setTabString(plugin, p, 5, 2, ChatColor.GREEN + rank);
		}
		for (String cell : playersTab) {
			if (cell != null) {
				TabAPI.setTabString(plugin, p, row, colom, cell);
			}
			colom++;
			if (colom > 2) {
				row++;
				if (row > 19) return;
				colom = 0;
			}
		}
	}

	@Override
	public void sortLists() {
		Collections.sort(arena.getTeams().get(0).getPlayers(), comp);
	}
	
	protected void createComp(){
		comp = new Comparator<ArenaPlayer>() {
			@Override
			public int compare(ArenaPlayer o1, ArenaPlayer o2) {
				int o1Kills = o1.getPlayerScore().getKills();
				int o2Kills = o2.getPlayerScore().getKills();
				if (o1Kills < o2Kills) return 1;
				if (o1Kills > o2Kills) return -1;
				return 0;
			}
		};
	}


}
