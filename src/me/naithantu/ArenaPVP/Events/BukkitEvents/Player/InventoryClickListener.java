package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {
	ArenaManager arenaManager;

	public InventoryClickListener(ArenaManager arenaManager) {
		this.arenaManager = arenaManager;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		System.out.println("Inventory click!");
		if(event.getWhoClicked() instanceof Player){
			Player player = (Player) event.getWhoClicked();
			
			ArenaPlayer arenaPlayer = arenaManager.getPlayerByName(player.getName());
			if (arenaPlayer != null) {
				arenaPlayer.getArena().getGamemode().onPlayerInventoryClick(event, arenaPlayer);
			}
		}
	}
}
