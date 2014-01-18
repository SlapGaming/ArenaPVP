package me.naithantu.ArenaPVP.Arena.ArenaExtras;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.Settings.ArenaSettings;
import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.Util.PlayerConfigUtil;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

public class ArenaSpectatorController {
    private Arena arena;
    private ArenaSettings settings;
    private ArenaSpawns arenaSpawns;
    private ArenaSpectators arenaSpectators;

    public ArenaSpectatorController(Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns, ArenaSpectators arenaSpectators){
        this.arena = arena;
        this.settings = settings;
        this.arenaSpawns = arenaSpawns;
        this.arenaSpectators = arenaSpectators;
    }

    public void joinSpectate(Player player) {
        //Teleport first to avoid problems with MVInventories
        ArenaPlayer arenaPlayer = new ArenaPlayer(ArenaPVP.getInstance(), player, arena, null);
        arenaPlayer.setPlayerState(ArenaPlayerState.SPECTATING);
        ArenaManager.addPlayer(arenaPlayer);

        Location location = arenaSpawns.getRespawnLocation(player, arenaPlayer, ArenaSpawns.SpawnType.SPECTATOR);
        PlayerConfigUtil.savePlayerConfig(player, new YamlStorage("players", player.getName()), location);

        arenaSpectators.getSpectators().put(player, arenaPlayer);
        changeToSpectate(player, arenaPlayer);
        arena.getGamemode().updateTabs();
    }

    public void leaveSpectate(Player player, ArenaPlayer arenaPlayer) {
        YamlStorage playerStorage = new YamlStorage("players", player.getName());
        ArenaManager.removePlayer(arenaPlayer);
        arenaSpectators.showSpectator(player);
        arenaSpectators.getSpectators().remove(player);

        if (player.isDead()) {
            Configuration playerConfig = playerStorage.getConfig();
            playerConfig.set("saved.hastoleave", true);
            playerStorage.saveConfig();
        } else {
            PlayerConfigUtil.loadPlayerConfig(player, playerStorage);
        }

        arena.getGamemode().clearTab(player);
        arena.getGamemode().updateTabs();
    }

    public void changeToSpectate(Player player, ArenaPlayer arenaPlayer) {
        if (settings.isAllowSpectateFly()) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }
        arenaSpectators.hideSpectator(player);
    }
}
