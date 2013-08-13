package me.naithantu.ArenaPVP.Events.BukkitEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;

public class ProjectileLaunchListener implements Listener {
	ArenaManager arenaManager;

	public ProjectileLaunchListener(ArenaManager arenaManager) {
		this.arenaManager = arenaManager;
	}

	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			ArenaPlayer arenaPlayer = arenaManager.getPlayerByName(player.getName());
			if (arenaPlayer != null) {
				//arenaPlayer.getArena().getGamemode().onProjectileLaunch(event, arenaPlayer);
			}
		}
	}
}
