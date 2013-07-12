package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.naithantu.ArenaPVP.Events.BukkitEvents.ArenaPlayerDamageEvent;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaPlayer;

public class DamageListener implements Listener {
	ArenaManager arenaManager;
	
	public DamageListener(ArenaManager arenaManager){
		this.arenaManager = arenaManager;
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			ArenaPlayer arenaPlayer = arenaManager.getPlayerByName(player.getName());
			if(arenaPlayer != null){
				arenaPlayer.getArena().getGamemode().onPlayerDamage(new ArenaPlayerDamageEvent(event, arenaPlayer));
			}
		}
	}
}
