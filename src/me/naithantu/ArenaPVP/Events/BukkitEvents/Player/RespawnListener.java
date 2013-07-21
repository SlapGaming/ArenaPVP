package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Events.BukkitEvents.ArenaPlayerRespawnEvent;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaPlayer;

public class RespawnListener implements Listener {
	ArenaPVP plugin;
	ArenaManager arenaManager;

	public RespawnListener(ArenaPVP plugin, ArenaManager arenaManager) {
		this.plugin = plugin;
		this.arenaManager = arenaManager;
	}

	@EventHandler
	public void onPlayerRespawn(final PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		final ArenaPlayer arenaPlayer = arenaManager.getPlayerByName(player.getName());
		if (arenaPlayer != null) {
			ArenaPlayerRespawnEvent arenaEvent = new ArenaPlayerRespawnEvent(event, arenaPlayer);
			arenaPlayer.getArena().getGamemode().onPlayerRespawn(arenaEvent);
			System.out.println("Event has been changed to: " + arenaEvent.getRespawnLocation().getBlockX());
			event.setRespawnLocation(arenaEvent.getRespawnLocation());
		}
	}
}
