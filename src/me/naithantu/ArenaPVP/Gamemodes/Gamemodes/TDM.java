package me.naithantu.ArenaPVP.Gamemodes.Gamemodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.TabController;
import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSettings;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSpawns;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaUtil;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Storage.YamlStorage;

public class TDM extends Gamemode {
	
	private Comparator<ArenaTeam> teamComp;
	private Comparator<ArenaPlayer> playerComp;
	
	
	public TDM(ArenaPVP plugin, ArenaManager arenaManager, Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns, ArenaUtil arenaUtil, YamlStorage arenaStorage, TabController tabController) {
		super(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
	}
	
	@Override
	public String getName(){
		return "TDM";
	}
	
	@Override
	public void onPlayerDeath(PlayerDeathEvent event, ArenaPlayer arenaPlayer){
		super.onPlayerDeath(event, arenaPlayer);
		Player killer = event.getEntity().getKiller();
		if(killer != null){
			ArenaTeam team = arenaManager.getPlayerByName(killer.getName()).getTeam();
			team.addScore();
			if(team.getScore() >= settings.getScoreLimit()){
				arena.stopGame(team);
			}
		}
	}

	@Override
	public void updateTabs() {
		if (tabController.hasTabAPI()) return; //Currently deactivated <- Remove this line to activate
		if (!tabController.hasTabAPI()) return;
		
		List<ArenaPlayer> players = new ArrayList<>();
		for (ArenaTeam team : arena.getTeams()) {
			players.addAll(team.getPlayers());
		}
		Collections.sort(players, playerComp);
		
	}

	@Override
	public void sortLists() {
		Collections.sort(arena.getTeams(), teamComp);		
	}

	@Override
	protected void createComp() {
		teamComp = new Comparator<ArenaTeam>() {
			@Override
			public int compare(ArenaTeam o1, ArenaTeam o2) {
				if (o1.getScore() < o2.getScore()) return 1;
				if (o1.getScore() > o2.getScore()) return -1;
				return 0;
			}
		};
		playerComp = new Comparator<ArenaPlayer>() {
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
