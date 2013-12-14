package me.naithantu.ArenaPVP.Events.BukkitEvents;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockRedstoneListener implements Listener {
	ArenaManager arenaManager;

	public BlockRedstoneListener(ArenaManager arenaManager) {
		this.arenaManager = arenaManager;
	}

	@EventHandler
	public void onRedstoneUpdate(BlockRedstoneEvent event) {
        for(Arena arena: arenaManager.getArenas().values()){
            arena.getGamemode().onRedstoneUpdate(event);
        }
	}
}
