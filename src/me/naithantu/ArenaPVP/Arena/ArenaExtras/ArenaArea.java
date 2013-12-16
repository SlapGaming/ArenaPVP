package me.naithantu.ArenaPVP.Arena.ArenaExtras;

import java.util.HashMap;

import me.naithantu.ArenaPVP.Arena.Settings.ArenaSettings;
import me.naithantu.ArenaPVP.Util.Util;
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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class ArenaArea {
    ArenaPVP plugin;
    Arena arena;
    ArenaSettings settings;

    WorldGuardPlugin worldGuard;
    String worldGuardRegion;
    String spectatorRegion;

    HashMap<String, OutOfBoundsTimer> outOfBoundsTimers = new HashMap<String, OutOfBoundsTimer>();

    public ArenaArea(ArenaPVP plugin, Arena arena, ArenaSettings settings, FileConfiguration config) {
        this.plugin = plugin;
        this.arena = arena;
        this.settings = settings;

        worldGuard = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        worldGuardRegion = config.getString("outofboundsregion");
        if (config.contains("spectatoroutofboundsregion")) {
            spectatorRegion = config.getString("spectatoroutofboundsregion");
        } else {
            spectatorRegion = worldGuardRegion;
        }
    }

    public void handleMove(PlayerMoveEvent event, ArenaPlayer arenaPlayer) {
        PlayerTimers playerTimers = arenaPlayer.getTimers();
        String regionName;
        if (arenaPlayer.getPlayerState() == ArenaPlayerState.SPECTATING) {
            regionName = spectatorRegion;
        } else {
            regionName = worldGuardRegion;
        }

        Player player = event.getPlayer();
        Location location = event.getTo();
        if (arenaPlayer.getPlayerState() == ArenaPlayerState.SPECTATING) {
            if (!checkArea(location, regionName)) {
                if (player.isFlying() || !checkArea(location.add(0, 1, 0), regionName)) {
                    event.setTo(event.getFrom());
                    player.setVelocity(new Vector(0, 0, 0));
                } else {
                    //If player isn't flying and only has the region above him, teleport back to spectator spawn.
                    player.teleport(arena.getArenaSpawns().getRespawnLocation(player, arenaPlayer, ArenaSpawns.SpawnType.SPECTATOR));
                    Util.msg(player, "You seem to have fallen out of the spectator area, teleported back to spectator spawn!");
                }
            }
        } else if (arenaPlayer.getPlayerState() == ArenaPlayerState.PLAYING && arena.getArenaState() == ArenaState.PLAYING) {
            if (playerTimers.isOutOfBounds() && checkArea(location, regionName)) {
                arenaPlayer.getTimers().setOutOfBounds(player, false);
            } else if (!playerTimers.isOutOfBounds() && !checkArea(location, regionName)) {
                arenaPlayer.getTimers().setOutOfBounds(player, true);
            }
        }
    }

    public boolean checkArea(Location location, String regionName) {
        RegionManager regionManager = worldGuard.getRegionManager(location.getWorld());
        for (ProtectedRegion region : regionManager.getApplicableRegions(location)) {
            if (region.getId().equals(regionName)) {
                return true;
            }
        }
        return false;
    }
}
