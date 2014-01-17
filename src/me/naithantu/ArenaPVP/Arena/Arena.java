package me.naithantu.ArenaPVP.Arena;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.*;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSpawns.SpawnType;
import me.naithantu.ArenaPVP.Arena.Runnables.StartArena;
import me.naithantu.ArenaPVP.Arena.Settings.ArenaSettings;
import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Events.ArenaEvents.EventJoinArena;
import me.naithantu.ArenaPVP.Events.ArenaEvents.EventLeaveArena;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.TabController;
import me.naithantu.ArenaPVP.Util.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class Arena {
    private ArenaPVP plugin = ArenaPVP.getInstance();
    private ArenaSpawns arenaSpawns;
    private ArenaSettings settings;
    private ArenaUtil arenaUtil;
    private ArenaArea arenaArea;
    private ArenaChat arenaChat;
    private ArenaSpectators arenaSpectators;

    private List<ArenaTeam> teams = new ArrayList<ArenaTeam>();
    private String nickName;

    private String arenaName;
    private ArenaState arenaState;
    private Gamemode gamemode;

    private YamlStorage arenaStorage;
    private FileConfiguration arenaConfig;

    private TabController tabController;

    public Arena(String arenaName) {
        this.arenaName = arenaName;
        arenaState = ArenaState.BEFORE_JOIN;
        arenaStorage = new YamlStorage("maps", arenaName);
        //Make sure config contains all necessary values
        arenaConfig = arenaStorage.getConfig();
        if(!arenaConfig.contains("teams.0")){
            arenaStorage.copyDefaultConfig("arena.yml");
        }

        settings = new ArenaSettings(arenaStorage, this);
        arenaSpawns = new ArenaSpawns(this, settings, arenaStorage);
        arenaUtil = new ArenaUtil(this);
        arenaArea = new ArenaArea(this, settings, arenaConfig);
        arenaSpectators = new ArenaSpectators(this);
        arenaChat = new ArenaChat(this);

        tabController = plugin.getTabController();

        setupArena();
        initializeArena();
    }

    public void setupArena(){
        nickName = arenaConfig.getString("nickname");
    }

    private void initializeArena(){
        // Create teams with proper names.
        for (String teamNumber : arenaConfig.getConfigurationSection("teams").getKeys(false)) {
            ArenaTeam team = new ArenaTeam(arenaConfig, Integer.parseInt(teamNumber));
            teams.add(team);
        }

        String gamemodeName = arenaConfig.getString("gamemode");

        // Create gamemode.
        gamemode = ArenaGamemode.getGamemode(this, settings, arenaSpawns, arenaUtil, arenaStorage, gamemodeName, tabController);
    }

    public String getNickName() {
        return nickName;
    }

    public ArenaSpawns getArenaSpawns() {
        return arenaSpawns;
    }

    public ArenaSettings getSettings() {
        return settings;
    }

    public ArenaUtil getArenatUtil() {
        return arenaUtil;
    }

    public ArenaArea getArenaArea() {
        return arenaArea;
    }

    public ArenaSpectators getArenaSpectators() {
        return arenaSpectators;
    }

    public ArenaChat getArenaChat() {
        return arenaChat;
    }

    public YamlStorage getArenaStorage() {
        return arenaStorage;
    }

    public String getArenaName() {
        return arenaName;
    }

    public Gamemode getGamemode() {
        return gamemode;
    }

    public ArenaState getArenaState() {
        return arenaState;
    }

    public void setArenaState(ArenaState arenaState) {
        this.arenaState = arenaState;
    }

    public void joinSpectate(Player player) {
        //Teleport first to avoid problems with MVInventories
        YamlStorage playerStorage = new YamlStorage("players", player.getName());
        Util.saveLocation(player.getLocation(), playerStorage, "location");
        Util.savePlayerConfig(player, playerStorage);
        ArenaPlayer arenaPlayer = new ArenaPlayer(plugin, player, this, null);
        arenaPlayer.setPlayerState(ArenaPlayerState.SPECTATING);
        ArenaManager.addPlayer(arenaPlayer);
        player.teleport(arenaSpawns.getRespawnLocation(player, arenaPlayer, SpawnType.SPECTATOR));
        arenaSpectators.addSpectator(player);
        arenaSpectators.getSpectators().put(player, arenaPlayer);
        changeToSpectate(player, arenaPlayer);
        gamemode.updateTabs();
    }

    public void leaveSpectate(Player player, ArenaPlayer arenaPlayer) {
        YamlStorage playerStorage = new YamlStorage("players", player.getName());
        ArenaManager.removePlayer(arenaPlayer);
        arenaSpectators.removeSpectator(player);
        arenaSpectators.getSpectators().remove(player);
        Configuration playerConfig = playerStorage.getConfig();
        Util.loadPlayerConfig(player, playerStorage);
        player.teleport(Util.getLocation(playerStorage, "location"));
        playerConfig.set("location", null);
        playerStorage.saveConfig();
        gamemode.clearTab(player);
        gamemode.updateTabs();
    }

    public void changeToSpectate(Player player, ArenaPlayer arenaPlayer) {
        if(settings.isAllowSpectateFly()){
            player.setAllowFlight(true);
            player.setFlying(true);
        }
        arenaSpectators.addSpectator(player);
    }

    public boolean joinGame(Player player, ArenaTeam teamToJoin) {
        EventJoinArena event = new EventJoinArena(player, teamToJoin);
        gamemode.onPlayerJoinArena(event);
        if (!event.isCancelled()) {
            Util.msg(player, "You joined team " + event.getTeam().getColoredName() + "!");

            //Teleport first to avoid problems with MVInventories
            YamlStorage playerStorage = new YamlStorage("players", player.getName());
            Util.saveLocation(player.getLocation(), playerStorage, "location");

            event.getTeam().joinTeam(player, this);

            Util.savePlayerConfig(player, playerStorage);
            arenaSpectators.onPlayerJoin(player);
            return true;
        }
        return false;
    }

    public void leaveGame(final ArenaPlayer arenaPlayer) {
        EventLeaveArena event = new EventLeaveArena(arenaPlayer);
        gamemode.onPlayerLeaveArena(event);
        if (!event.isCancelled()) {
            Player player = Bukkit.getPlayerExact(arenaPlayer.getPlayerName());
            if (player != null) {
                YamlStorage playerStorage = new YamlStorage("players", player.getName());
                Configuration playerConfig = playerStorage.getConfig();
                if (player.isDead()) {
                    playerConfig.set("hastoleave", true);
                } else {
                    Util.loadPlayerConfig(player, playerStorage);
                    player.teleport(Util.getLocation(playerStorage, "location"));
                    playerConfig.set("location", null);
                }
                playerStorage.saveConfig();
            }
            arenaPlayer.getTeam().leaveTeam(arenaPlayer, player);
        }
    }

    public void startGame() {
        StartArena startArena = new StartArena(this);
        startArena.runTaskTimer(plugin, 0, 20);
    }

    public void stopGame(ArenaPlayer winPlayer) {
        if (winPlayer != null) {
            arenaUtil.sendMessageAll(winPlayer.getTeam().getTeamColor() + winPlayer.getPlayerName() + " has won the game!");
        }
        stopGame();
    }

    public void stopGame(ArenaTeam winTeam) {
        if (winTeam != null) {
            arenaUtil.sendMessageAll("Team " + winTeam.getColoredName() + " has won the game!");
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
        gamemode.onArenaStop();

        //Let online players leave the game.
        for (ArenaTeam team : teams) {
            List<ArenaPlayer> players = new ArrayList<ArenaPlayer>(team.getPlayers());
            for (ArenaPlayer arenaPlayer : players) {
                leaveGame(arenaPlayer);
            }
        }

        //Let spectators leave the game.
        Set<Entry<Player, ArenaPlayer>> spectators = new HashSet<>(arenaSpectators.getSpectators().entrySet());
        for (Entry<Player, ArenaPlayer> entry : spectators) {
            leaveSpectate(entry.getKey(), entry.getValue());
        }

        //Paste schematic on arena.
        File schematic = new File(plugin.getDataFolder() + File.separator + "maps", arenaName + ".schematic");
        if (schematic.exists()) {
            EditSession editSession = new EditSession(new BukkitWorld(Bukkit.getWorld(arenaConfig.getString("schematicworld"))), 999999999);
            SchematicFormat format = SchematicFormat.getFormat(schematic);

            CuboidClipboard cuboidClipboard;
            try {
                cuboidClipboard = format.load(schematic);
            } catch (IOException | DataException e) {
                e.printStackTrace();
                return;
            }

            try {
                Vector pos = cuboidClipboard.getOrigin();
                cuboidClipboard.place(editSession, pos, false);
            } catch (MaxChangedBlocksException e) {
                e.printStackTrace();
            }
        }

        //Destroy iconMenu (otherwise listener will stay loaded)
        settings.getSettingMenu().destroy();

        //Remove arena from arenaManager.
        ArenaManager.removeArena(arenaName);
    }

    public void addTeam(ArenaTeam team) {
        teams.add(team);
    }

    public List<ArenaTeam> getTeams() {
        return teams;
    }

    public ArenaTeam getTeam(int teamNumber) {
        for (ArenaTeam team : teams) {
            if (team.getTeamNumber() == teamNumber) {
                return team;
            }
        }
        return null;
    }

    public ArenaTeam getTeam(String teamName) {
        for (ArenaTeam team : teams) {
            if (team.getTeamName().equals(teamName)) {
                return team;
            }
        }
        return null;
    }
}
