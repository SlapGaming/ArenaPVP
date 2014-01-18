package me.naithantu.ArenaPVP.Events.BukkitEvents;

import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ProjectileLaunchListener implements Listener {

	public ProjectileLaunchListener(ArenaManager arenaManager) {
	}

	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			ArenaPlayer arenaPlayer = ArenaManager.getPlayerByName(player.getName());
			if (arenaPlayer != null) {
				//arenaPlayer.getArena().getGamemode().onProjectileLaunch(event, arenaPlayer);
			}
		}
	}
}
