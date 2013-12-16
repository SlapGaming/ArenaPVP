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
    String pvpRegion;
    String spectatorRegion;

    HashMap<String, OutOfBoundsTimer> outOfBoundsTimers = new HashMap<String, OutOfBoundsTimer>();

    public ArenaArea(ArenaPVP plugin, Arena arena, ArenaSettings settings, FileConfiguration config) {
        this.plugin = plugin;
        this.arena = arena;
        this.settings = settings;

        worldGuard = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        pvpRegion = config.getString("outofboundsregion");
        if (config.contains("spectatoroutofboundsregion")) {
            spectatorRegion = config.getString("spectatoroutofboundsregion");
        } else {
            spectatorRegion = pvpRegion;
        }
    }

    public void handleMove(PlayerMoveEvent event, ArenaPlayer arenaPlayer) {
        PlayerTimers playerTimers = arenaPlayer.getTimers();
        Player player = event.getPlayer();
        Location location = event.getTo();
        if (arenaPlayer.getPlayerState() == ArenaPlayerState.SPECTATING) {
            //If spectator leaves spectator area, prevent this.
            if (!checkArea(location, spectatorRegion)) {
                //If player isn't flying and only has the spectator region above him, teleport back to spectator spawn.
                if (!player.isFlying() && checkArea(location.add(0, 1, 0), spectatorRegion)) {
                    player.teleport(arena.getArenaSpawns().getRespawnLocation(player, arenaPlayer, ArenaSpawns.SpawnType.SPECTATOR));
                    Util.msg(player, "You seem to have fallen out of the spectator area, teleported back to spectator spawn!");
                } else {
                    //If player isn't flying and only has the region above him, teleport back to spectator spawn.
                    event.setTo(event.getFrom());
                    player.setVelocity(new Vector(0, 0, 0));
                }
            //If spectator enters pvp area when he's not allowed to, prevent this.
            } else if (!settings.isAllowSpectateInArea() && checkArea(location, pvpRegion)){
                //If player isn't flying and fell down into the pvp region (no pvp region above him), teleport back to spectator spawn.
                if (!player.isFlying() && !checkArea(location.add(0, 1, 0), pvpRegion)) {
                    player.teleport(arena.getArenaSpawns().getRespawnLocation(player, arenaPlayer, ArenaSpawns.SpawnType.SPECTATOR));
                    Util.msg(player, "You seem to have fallen into the pvp area, teleported back to spectator spawn!");
                } else {
                    //If player isn't flying and only has the region above him, teleport back to spectator spawn.
                    event.setTo(event.getFrom());
                    player.setVelocity(new Vector(0, 0, 0));
                }
            }
        } else if (arenaPlayer.getPlayerState() == ArenaPlayerState.PLAYING && arena.getArenaState() == ArenaState.PLAYING) {
            if (playerTimers.isOutOfBounds() && checkArea(location, pvpRegion)) {
                arenaPlayer.getTimers().setOutOfBounds(player, false);
            } else if (!playerTimers.isOutOfBounds() && !checkArea(location, pvpRegion)) {
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
