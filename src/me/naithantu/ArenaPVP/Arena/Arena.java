package me.naithantu.ArenaPVP.Arena;

import me.naithantu.ArenaPVP.Arena.ArenaExtras.*;
import me.naithantu.ArenaPVP.Arena.Settings.ArenaSettings;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.TabController;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Arena {
    private ArenaPVP plugin = ArenaPVP.getInstance();
    private ArenaSpawns arenaSpawns;
    private ArenaSettings settings;
    private ArenaUtil arenaUtil;
    private ArenaArea arenaArea;
    private ArenaChat arenaChat;
    private ArenaSpectators arenaSpectators;
    private ArenaPlayerController arenaPlayerController;
    private ArenaSpectatorController arenaSpectatorController;
    private ArenaGameController arenaGameController;

    private List<ArenaTeam> teams = new ArrayList<>();
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
        if (!arenaConfig.contains("teams.0")) {
            arenaStorage.copyDefaultConfig("arena.yml");
        }

        settings = new ArenaSettings(arenaStorage, this, true);
        arenaSpawns = new ArenaSpawns(this, settings, arenaStorage);
        arenaUtil = new ArenaUtil(this);
        arenaArea = new ArenaArea(this, settings, arenaConfig);
        arenaSpectators = new ArenaSpectators(this);
        arenaChat = new ArenaChat(this);

        arenaGameController = new ArenaGameController(this);
        arenaPlayerController = new ArenaPlayerController(this, settings, arenaSpectators);
        arenaSpectatorController = new ArenaSpectatorController(this, settings, arenaSpawns, arenaSpectators);

        tabController = plugin.getTabController();

        setupArena();
        initializeArena();
    }

    public void setupArena() {
        nickName = arenaConfig.getString("nickname");
    }

    private void initializeArena() {
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

    public ArenaPlayerController getArenaPlayerController() {
        return arenaPlayerController;
    }

    public ArenaSpectatorController getArenaSpectatorController() {
        return arenaSpectatorController;
    }

    public ArenaGameController getArenaGameController() {
        return arenaGameController;
    }

    public YamlStorage getArenaStorage() {
        return arenaStorage;
    }

    public FileConfiguration getArenaConfig() {
        return arenaConfig;
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
