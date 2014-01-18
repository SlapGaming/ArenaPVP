package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

	public QuitListener(){
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		ArenaPlayer arenaPlayer = ArenaManager.getPlayerByName(player.getName());
		if(arenaPlayer != null){
			arenaPlayer.getArena().getGamemode().onPlayerQuit(event, arenaPlayer);
		}
	}
}
