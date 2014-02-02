package me.naithantu.ArenaPVP.Arena.ArenaExtras;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;
import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;
import me.naithantu.ArenaPVP.Arena.Runnables.StartArena;
import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ArenaGameController {
    private ArenaPVP plugin;
    private Arena arena;

    public ArenaGameController(Arena arena) {
        this.plugin = ArenaPVP.getInstance();
        this.arena = arena;
    }

    public void startGame() {
        StartArena startArena = new StartArena(arena);
        startArena.runTaskTimer(plugin, 0, 20);
    }

    public void stopGame(ArenaPlayer winPlayer) {
        if (winPlayer != null) {
            arena.getArenatUtil().sendMessageAll(winPlayer.getTeam().getTeamColor() + winPlayer.getPlayerName() + " has won the game!");
        }
        stopGame();
    }

    public void stopGame(ArenaTeam winTeam) {
        if (winTeam != null) {
            arena.getArenatUtil().sendMessageAll("Team " + winTeam.getColoredName() + " has won the game!");
        }
        stopGame();
    }

    public void stopGame() {
        if (plugin.isEnabled()) {
            //Delay in case last player was killed by arrow (dm gamemodes).
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    stop();
                }
            }, 1);
        } else {
            stop();
        }
    }

    private void stop() {
        arena.getGamemode().onArenaStop();

        //Let online players leave the game.
        for (ArenaTeam team : arena.getTeams()) {
            List<ArenaPlayer> players = new ArrayList<ArenaPlayer>(team.getPlayers());
            for (ArenaPlayer arenaPlayer : players) {
                arena.getArenaPlayerController().leaveGame(arenaPlayer);
            }
        }

        //Let spectators leave the game.
        Set<Map.Entry<Player, ArenaPlayer>> spectators = new HashSet<>(arena.getArenaSpectators().getSpectators().entrySet());
        for (Map.Entry<Player, ArenaPlayer> entry : spectators) {
            arena.getArenaSpectatorController().leaveSpectate(entry.getKey(), entry.getValue());
        }

        //Paste schematic on arena.
        File schematic = new File(plugin.getDataFolder() + File.separator + "maps", arena.getArenaName() + ".schematic");
        if (schematic.exists()) {
            EditSession editSession = new EditSession(new BukkitWorld(Bukkit.getWorld(arena.getArenaConfig().getString("schematicworld"))), 999999999);
            SchematicFormat format = SchematicFormat.getFormat(schematic);

            CuboidClipboard cuboidClipboard;
            try {
                cuboidClipboard = format.load(schematic);
            } catch (IOException | DataException e) {
                e.printStackTrace();
                return;
            }

            try {
                com.sk89q.worldedit.Vector pos = cuboidClipboard.getOrigin();
                cuboidClipboard.place(editSession, pos, false);
            } catch (MaxChangedBlocksException e) {
                e.printStackTrace();
            }
        }

        //Destroy iconMenu (otherwise listener will stay loaded)
        arena.getSettings().getSettingMenu().destroy();

        //Remove arena from arenaManager.
        ArenaManager.removeArena(arena.getArenaName());
    }
}
