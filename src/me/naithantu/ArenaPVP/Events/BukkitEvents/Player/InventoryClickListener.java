package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

	public InventoryClickListener() {
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getWhoClicked() instanceof Player){
			Player player = (Player) event.getWhoClicked();
			
			ArenaPlayer arenaPlayer = ArenaManager.getPlayerByName(player.getName());
			if (arenaPlayer != null) {
				arenaPlayer.getArena().getGamemode().onPlayerInventoryClick(event, arenaPlayer);
			}
		}
	}
}
