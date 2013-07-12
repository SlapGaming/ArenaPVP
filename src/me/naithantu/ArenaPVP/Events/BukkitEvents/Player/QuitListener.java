package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaPlayer;

public class QuitListener implements Listener {
	ArenaManager arenaManager;
	
	public QuitListener(ArenaManager arenaManager){
		this.arenaManager = arenaManager;
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		ArenaPlayer arenaPlayer = arenaManager.getPlayerByName(player.getName());
		if(arenaPlayer != null){
			arenaPlayer.getArena().getGamemode().onPlayerQuit(event);
		}
	}
}
