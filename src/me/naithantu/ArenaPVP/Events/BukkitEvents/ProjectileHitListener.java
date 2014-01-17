package me.naithantu.ArenaPVP.Events.BukkitEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;

public class ProjectileHitListener implements Listener {

	public ProjectileHitListener() {
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if (event.getEntity().getShooter() instanceof Player) {
			Player player = (Player) event.getEntity().getShooter();
			ArenaPlayer arenaPlayer = ArenaManager.getPlayerByName(player.getName());
			if (arenaPlayer != null) {
				arenaPlayer.getArena().getGamemode().onProjectileHit(event, arenaPlayer);
			}
		}
	}
}
