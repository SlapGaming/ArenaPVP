package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.Util.Util;

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
		
		YamlStorage playerStorage = new YamlStorage(plugin, "players", player.getName());
		if(playerStorage.exists()){
			Configuration playerConfig = playerStorage.getConfig();
			if(playerConfig.contains("hastoleave")){
				Util.playerLeave(player, playerStorage);
                event.setRespawnLocation(Util.getLocation(playerStorage, "location"));
				playerConfig.set("location", null);
			}
		}
		
		final ArenaPlayer arenaPlayer = arenaManager.getPlayerByName(player.getName());
		if (arenaPlayer != null) {
			arenaPlayer.getArena().getGamemode().onPlayerRespawn(event, arenaPlayer);
		}
	}
}
