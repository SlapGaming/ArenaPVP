package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaPlayer;

public class DropItemListener implements Listener {
	ArenaManager arenaManager;

	public DropItemListener(ArenaManager arenaManager) {
		this.arenaManager = arenaManager;
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		System.out.println("Item drop!");
		Player player = event.getPlayer();
		ArenaPlayer arenaPlayer = arenaManager.getPlayerByName(player.getName());
		if (arenaPlayer != null) {
			arenaPlayer.getArena().getGamemode().onPlayerDropItem(event, arenaPlayer);
		}
	}
}
