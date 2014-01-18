package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

	public DeathListener(){
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		Player player = event.getEntity();
		ArenaPlayer arenaPlayer = ArenaManager.getPlayerByName(player.getName());
		if(arenaPlayer != null){
			arenaPlayer.getArena().getGamemode().onPlayerDeath(event, arenaPlayer);
		}
	}
}
