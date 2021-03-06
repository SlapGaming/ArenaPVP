package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.Util.PlayerConfigUtil;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {

	public RespawnListener() {
	}

	@EventHandler
	public void onPlayerRespawn(final PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		
		YamlStorage playerStorage = new YamlStorage("players", player.getName());
		if(playerStorage.exists()){
			Configuration playerConfig = playerStorage.getConfig();
			if(playerConfig.getBoolean("saved.hastoleave") == true){
				PlayerConfigUtil.loadPlayerConfig(player, playerStorage, event);
			}
		}
		
		final ArenaPlayer arenaPlayer = ArenaManager.getPlayerByName(player.getName());
		if (arenaPlayer != null) {
			arenaPlayer.getArena().getGamemode().onPlayerRespawn(event, arenaPlayer);
		}
	}
}
