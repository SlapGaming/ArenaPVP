package me.naithantu.ArenaPVP.Gamemodes.Gamemodes;

import java.util.Collections;
import java.util.Comparator;

import org.bukkit.event.entity.PlayerDeathEvent;

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

public class LMS extends Gamemode {

	private Comparator<ArenaPlayer> comp;

	public LMS(ArenaPVP plugin, ArenaManager arenaManager, Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns, ArenaUtil arenaUtil, YamlStorage arenaStorage, TabController tabController) {
		super(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
		settings.setFriendlyFire(true);
	}
	
	@Override
	public String getName() {
		return "LMS";
	}
	
	@Override
	public boolean isTeamGame() {
		return false;
	}

	@Override
	public void onPlayerDeath(PlayerDeathEvent event, ArenaPlayer arenaPlayer) {
		super.onPlayerDeath(event, arenaPlayer);
		if (arena.getArenaState() == ArenaState.PLAYING && arenaPlayer.getPlayerState() == ArenaPlayerState.PLAYING) {
			arenaPlayer.setPlayerState(ArenaPlayerState.SPECTATING);
			if (checkRemainingPlayers() == 1) {
				arena.stopGame(getWinningPlayer());
			}
		}
		sortLists();
		updateTabs();
	}

	private int checkRemainingPlayers() {
		int remainingPlayers = 0;
		for (ArenaTeam team : arena.getTeams()) {
			for (ArenaPlayer arenaPlayer : team.getPlayers()) {
				if (arenaPlayer.getPlayerState() != ArenaPlayerState.SPECTATING) {
					remainingPlayers++;
					break;
				}
			}
		}
		return remainingPlayers;
	}

	private ArenaPlayer getWinningPlayer() {
		for (ArenaTeam team : arena.getTeams()) {
			for (ArenaPlayer arenaPlayer : team.getPlayers()) {
				if (arenaPlayer.getPlayerState() != ArenaPlayerState.SPECTATING) {
					return arenaPlayer;
				}
			}
		}
		return null;
	}

	@Override
	public void updateTabs() {
		if (tabController.hasTabAPI())
			return; //Currently deactivated <- Remove this line to activate
		if (!tabController.hasTabAPI())
			return;
	}

	@Override
	public void sortLists() {
		Collections.sort(arena.getTeams().get(0).getPlayers(), comp);
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
