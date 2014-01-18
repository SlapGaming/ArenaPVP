package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PickupItemListener implements Listener {

	public PickupItemListener() {
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		ArenaPlayer arenaPlayer = ArenaManager.getPlayerByName(player.getName());
		if (arenaPlayer != null) {
			arenaPlayer.getArena().getGamemode().onPlayerPickupItem(event, arenaPlayer);
		}
	}
}
