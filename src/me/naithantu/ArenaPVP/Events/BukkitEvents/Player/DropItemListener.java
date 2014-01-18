package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropItemListener implements Listener {

	public DropItemListener() {
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		ArenaPlayer arenaPlayer = ArenaManager.getPlayerByName(player.getName());
		if (arenaPlayer != null) {
			arenaPlayer.getArena().getGamemode().onPlayerDropItem(event, arenaPlayer);
		}
	}
}
