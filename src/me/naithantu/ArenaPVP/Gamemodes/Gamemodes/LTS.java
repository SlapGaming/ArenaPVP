package me.naithantu.ArenaPVP.Gamemodes.Gamemodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.mcsg.double0negative.tabapi.TabAPI;

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
import me.naithantu.ArenaPVP.TabController.Gamemodes;
import me.naithantu.ArenaPVP.Util.Util;

public class LTS extends Gamemode {

	private Comparator<ArenaPlayer> comp;

	public LTS(ArenaPVP plugin, ArenaManager arenaManager, Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns, ArenaUtil arenaUtil, YamlStorage arenaStorage, TabController tabController) {
		super(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
	}

	@Override
	public String getName() {
		return "LTS";
	}

	@Override
	public void onPlayerDeath(PlayerDeathEvent event, ArenaPlayer arenaPlayer) {
		super.onPlayerDeath(event, arenaPlayer);
		if (arena.getArenaState() == ArenaState.PLAYING && arenaPlayer.getPlayerState() == ArenaPlayerState.PLAYING) {
			arenaPlayer.setPlayerState(ArenaPlayerState.SPECTATING);
			if (checkRemainingTeams() == 1) {
				arena.stopGame(getWinningTeam());
			}
		}
		if (arena.getArenaStorage().getConfig().getBoolean("allowspectate"))
			arena.changeToSpectate(event.getEntity(), arenaPlayer);
		sortLists();
		updateTabs();
	}

	private int checkRemainingTeams() {
		int remainingTeams = 0;
		for (ArenaTeam team : arena.getTeams()) {
			for (ArenaPlayer arenaPlayer : team.getPlayers()) {
				if (arenaPlayer.getPlayerState() != ArenaPlayerState.SPECTATING) {
					remainingTeams++;
					break;
				}
			}
		}
		return remainingTeams;
	}

	private ArenaTeam getWinningTeam() {
		for (ArenaTeam team : arena.getTeams()) {
			for (ArenaPlayer arenaPlayer : team.getPlayers()) {
				if (arenaPlayer.getPlayerState() != ArenaPlayerState.SPECTATING) {
					return team;
				}
			}
		}
		return null;
	}

	@Override
	public void updateTabs() {
		if (!tabController.hasTabAPI())
			return;

		String status = Util.capaltizeFirstLetter(arena.getArenaState().toString());
		String arenaName = arena.getArenaName();
		int nrOfPlayers = 0;

		List<String[]> teamStrings = new ArrayList<>();
		List<ArenaTeam> teams = arena.getTeams();
		for (ArenaTeam team : teams) {
			int x = 0;
			String[] teamString = new String[team.getPlayers().size() + 1];
			teamString[x] = team.getTeamColor() + "-" + team.getTeamName() + "-";
			for (ArenaPlayer teamPlayer : team.getPlayers()) {
				x++;
				if (teamPlayer.getPlayerState() == ArenaPlayerState.PLAYING) {
					teamString[x] = teamPlayer.getPlayerName();
				} else {
					teamString[x] = ChatColor.STRIKETHROUGH + teamPlayer.getPlayerName();
				}
				nrOfPlayers++;
			}
			teamStrings.add(teamString);
		}

		String playersString = nrOfPlayers + " Players";
		String spectators = ChatColor.GRAY + "" + arena.getArenaSpectators().getSpectators().size() + " Spectators";

		for (ArenaTeam team : teams) {
			for (ArenaPlayer player : team.getPlayers()) {
				Player p = Bukkit.getPlayerExact(player.getPlayerName());
				if (p != null) {
					setTabPlayer(p, status, arenaName, playersString, spectators, teamStrings);
				}
			}
		}
		for (Player p : arena.getArenaSpectators().getSpectators().keySet()) {
			setTabPlayer(p, status, arenaName, playersString, spectators, teamStrings);
		}
		TabAPI.updateAll();
	}

	private void setTabPlayer(Player p, String status, String arena, String players, String spectators, List<String[]> teamStrings) {
		int row = tabController.setTopTab(p, Gamemodes.LTS);
		TabAPI.setTabString(plugin, p, 2, 1, status);
		TabAPI.setTabString(plugin, p, 3, 1, arena);
		TabAPI.setTabString(plugin, p, 4, 1, players);
		TabAPI.setTabString(plugin, p, 4, 2, spectators);

		setTabPlayerTeam(p, row, 0, teamStrings.get(0));
		setTabPlayerTeam(p, row, 2, teamStrings.get(1));

		switch (teamStrings.size()) {
		case 5:
			setTabPlayerTeam(p, 10, 1, teamStrings.get(4));
		case 4:
			setTabPlayerTeam(p, 13, 0, teamStrings.get(2));
			setTabPlayerTeam(p, 13, 2, teamStrings.get(3));
			break;
		case 3:
			setTabPlayerTeam(p, 13, 1, teamStrings.get(2));
			break;
		}

	}

	private void setTabPlayerTeam(Player p, int row, int colom, String[] team) {
		int x = 1;
		for (String cell : team) {
			TabAPI.setTabString(plugin, p, row, colom, cell);
			row++;
			x++;
			if (x > 6)
				return;
		}
	}

	@Override
	public void sortLists() {
		for (ArenaTeam team : arena.getTeams()) {
			Collections.sort(team.getPlayers(), comp);
		}
	}

	@Override
	protected void createComp() {
		comp = new Comparator<ArenaPlayer>() {
			@Override
			public int compare(ArenaPlayer o1, ArenaPlayer o2) {
				ArenaPlayerState o1State = o1.getPlayerState();
				ArenaPlayerState o2State = o2.getPlayerState();
				if (o1State != ArenaPlayerState.PLAYING && o2State == ArenaPlayerState.PLAYING)
					return 1;
				if (o1State == ArenaPlayerState.PLAYING && o2State != ArenaPlayerState.PLAYING)
					return -1;
				return 0;
			}
		};
	}

}
