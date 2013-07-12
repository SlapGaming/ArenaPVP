package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener {
	ArenaManager arenaManager;

	public MoveListener(ArenaManager arenaManager) {
		this.arenaManager = arenaManager;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		ArenaPlayer arenaPlayer = arenaManager.getPlayerByName(player.getName());
		if (arenaPlayer != null) {
			arenaPlayer.getArena().getGamemode().onPlayerMove(event);
		}
	}
}
