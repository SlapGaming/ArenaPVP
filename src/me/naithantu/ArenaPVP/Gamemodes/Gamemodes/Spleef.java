package me.naithantu.ArenaPVP.Gamemodes.Gamemodes;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.TabController;
import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSettings;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSpawns;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaUtil;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Storage.YamlStorage;

public class Spleef extends Gamemode {

	public Spleef(ArenaPVP plugin, ArenaManager arenaManager, Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns, ArenaUtil arenaUtil, YamlStorage arenaStorage, TabController tabController) {
		super(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
	}

	public String getName() {
		return "Spleef";
	}

	@Override
	public void updateTabs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void sortLists() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createComp() {
		// TODO Auto-generated method stub

	}

}
