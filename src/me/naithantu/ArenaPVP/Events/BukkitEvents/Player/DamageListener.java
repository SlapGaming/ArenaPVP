package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;

public class DamageListener implements Listener {
	ArenaManager arenaManager;
	
	public DamageListener(ArenaManager arenaManager){
		this.arenaManager = arenaManager;
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			ArenaPlayer arenaPlayer = arenaManager.getPlayerByName(player.getName());
			if(arenaPlayer != null){
				arenaPlayer.getArena().getGamemode().onPlayerDamageByEntity(event, arenaPlayer);
			}
		}
	}
}
