package me.naithantu.ArenaPVP.Events.BukkitEvents;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.ArenaManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockRedstoneListener implements Listener {

	public BlockRedstoneListener() {
	}

	@EventHandler
	public void onRedstoneUpdate(BlockRedstoneEvent event) {
        for(Arena arena: ArenaManager.getArenas().values()){
            arena.getGamemode().onRedstoneUpdate(event);
        }
	}
}
