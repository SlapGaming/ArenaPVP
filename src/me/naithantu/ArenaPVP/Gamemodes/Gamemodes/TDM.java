package me.naithantu.ArenaPVP.Gamemodes.Gamemodes;

import org.bukkit.entity.Player;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Events.BukkitEvents.ArenaPlayerDeathEvent;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaTeam;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaSettings;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaSpawns;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaUtil;
import me.naithantu.ArenaPVP.Storage.YamlStorage;

public class TDM extends Gamemode {
	public TDM(ArenaPVP plugin, ArenaManager arenaManager, Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns, ArenaUtil arenaUtil, YamlStorage arenaStorage) {
		super(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage);
	}
	
	@Override
	public String getName(){
		return "TDM";
	}
	
	public void onPlayerDeath(ArenaPlayerDeathEvent event){
		super.onPlayerDeath(event);
		Player killer = event.getEntity().getKiller();
		if(killer != null){
			ArenaTeam team = arenaManager.getPlayerByName(killer.getName()).getTeam();
			team.addScore();
			if(team.getScore() >= settings.getScoreLimit()){
				arena.stopGame(team);
			}
		}
	}
}
