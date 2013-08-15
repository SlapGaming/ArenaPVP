package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;

public class PickupItemListener implements Listener {
	ArenaManager arenaManager;

	public PickupItemListener(ArenaManager arenaManager) {
		this.arenaManager = arenaManager;
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		ArenaPlayer arenaPlayer = arenaManager.getPlayerByName(player.getName());
		if (arenaPlayer != null) {
			arenaPlayer.getArena().getGamemode().onPlayerPickupItem(event, arenaPlayer);
		}
	}
}
