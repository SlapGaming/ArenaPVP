package me.naithantu.ArenaPVP.Arena.ArenaExtras;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.PlayerExtras.PlayerTimers;
import me.naithantu.ArenaPVP.Arena.Runnables.OutOfBoundsTimer;

public class ArenaArea {
	ArenaPVP plugin;
	Arena arena;
	ArenaSettings settings;

	WorldGuardPlugin worldGuard;
	String worldGuardRegion;

	HashMap<String, OutOfBoundsTimer> outOfBoundsTimers = new HashMap<String, OutOfBoundsTimer>();

	public ArenaArea(ArenaPVP plugin, Arena arena, ArenaSettings settings, FileConfiguration config) {
		this.plugin = plugin;
		this.arena = arena;
		this.settings = settings;
		
		worldGuard = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		worldGuardRegion = config.getString("outofboundsregion");
	}

	public void handleMove(ArenaPlayer arenaPlayer, Player player, Location location) {
		if(arenaPlayer.getPlayerState() == ArenaPlayerState.PLAYING && arena.getArenaState() == ArenaState.PLAYING){
			PlayerTimers playerTimers = arenaPlayer.getTimers();
			if (checkArea(location) && playerTimers.isOutOfBounds()) {
				arenaPlayer.getTimers().setOutOfBounds(player, false);
			} else if (!checkArea(location) && !playerTimers.isOutOfBounds()){
				arenaPlayer.getTimers().setOutOfBounds(player, true);
			}
		}
	}

	public boolean checkArea(Location location) {
		RegionManager regionManager = worldGuard.getRegionManager(location.getWorld());
		for (ProtectedRegion region : regionManager.getApplicableRegions(location)) {
			if (region.getId().equals(worldGuardRegion)) {
				return true;
			}
		}
		return false;
	}
}
