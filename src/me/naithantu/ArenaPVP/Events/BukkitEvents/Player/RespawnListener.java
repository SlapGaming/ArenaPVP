package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.naithantu.ArenaPVP.Events.BukkitEvents.ArenaPlayerRespawnEvent;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaPlayer;

public class RespawnListener implements Listener {
	ArenaManager arenaManager;
	
	public RespawnListener(ArenaManager arenaManager){
		this.arenaManager = arenaManager;
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		Player player = event.getPlayer();
		ArenaPlayer arenaPlayer = arenaManager.getPlayerByName(player.getName());
		if(arenaPlayer != null){
			ArenaPlayerRespawnEvent arenaEvent = new ArenaPlayerRespawnEvent(event, arenaPlayer);
			arenaPlayer.getArena().getGamemode().onPlayerRespawn(arenaEvent);
			event.setRespawnLocation(arenaEvent.getRespawnLocation());

		}
	}
}
