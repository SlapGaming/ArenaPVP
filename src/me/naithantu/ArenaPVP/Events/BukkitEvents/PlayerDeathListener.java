package me.naithantu.ArenaPVP.Events.BukkitEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaPlayer;

public class PlayerDeathListener implements Listener {
	ArenaManager arenaManager;
	
	public PlayerDeathListener(ArenaManager arenaManager){
		this.arenaManager = arenaManager;
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		Player player = event.getEntity();
		ArenaPlayer arenaPlayer = arenaManager.getPlayerByName(player.getName());
		arenaPlayer.getArena().getGamemode().onPlayerDeath(event);
	}
}
