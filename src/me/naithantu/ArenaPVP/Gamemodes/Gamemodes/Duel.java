package me.naithantu.ArenaPVP.Gamemodes.Gamemodes;

import java.util.Collections;
import java.util.Comparator;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.TabController;
import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSettings;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSpawns;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaUtil;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Storage.YamlStorage;

public class Duel extends Gamemode {

	private Comparator<ArenaPlayer> comp;
	
	public Duel(ArenaPVP plugin, ArenaManager arenaManager, Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns,	ArenaUtil arenaUtil, YamlStorage arenaStorage, TabController tabController) {
		super(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
	}

	@Override
	public void updateTabs() {
		if (tabController.hasTabAPI()) return; //Currently deactivated <- Remove this line to activate
		if (!tabController.hasTabAPI()) return;
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
				int o1Kills = o1.getPlayerScore().getKills();
				int o2Kills = o2.getPlayerScore().getKills();
				if (o1Kills < o2Kills) return 1;
				if (o1Kills > o2Kills) return -1;
				return 0;
			}
		};
	}

}
