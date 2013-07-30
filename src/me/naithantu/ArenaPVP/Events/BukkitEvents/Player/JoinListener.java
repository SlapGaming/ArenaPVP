package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaPlayer;

public class JoinListener implements Listener {
	ArenaManager arenaManager;
	
	public JoinListener(ArenaManager arenaManager){
		this.arenaManager = arenaManager;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		ArenaPlayer arenaPlayer = arenaManager.getPlayerByName(player.getName());
		if(arenaPlayer != null){
			arenaPlayer.getArena().getGamemode().onPlayerJoin(event, arenaPlayer);
		}
	}
}
