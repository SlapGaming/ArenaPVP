package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockListener implements Listener {

	public BlockListener(){
	}
	
	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent event){
		Player player = event.getPlayer();
		ArenaPlayer arenaPlayer = ArenaManager.getPlayerByName(player.getName());
		if(arenaPlayer != null){
			arenaPlayer.getArena().getGamemode().onPlayerBreakBlock(event, arenaPlayer);
		}
	}
	
	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent event){
		Player player = event.getPlayer();
		ArenaPlayer arenaPlayer = ArenaManager.getPlayerByName(player.getName());
		if(arenaPlayer != null){
			arenaPlayer.getArena().getGamemode().onPlayerPlaceBlock(event, arenaPlayer);
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ArenaPlayer arenaPlayer = ArenaManager.getPlayerByName(player.getName());
		if(arenaPlayer != null){
			arenaPlayer.getArena().getGamemode().onPlayerInteractBlock(event, arenaPlayer);
		}
	}
	
}
