package me.naithantu.ArenaPVP.Gamemodes.Gamemodes.Redstone;

import java.util.*;

import me.naithantu.ArenaPVP.Commands.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.mcsg.double0negative.tabapi.TabAPI;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.TabController;
import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaPlayerState;
import me.naithantu.ArenaPVP.Arena.Settings.ArenaSettings;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSpawns;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaUtil;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.Util.Util;

public class Redstone extends Gamemode {

    private Comparator<ArenaPlayer> comp;

    private HashMap<ArenaTeam, Location> redstoneLocations = new HashMap<ArenaTeam, Location>();

    //The world the flags are in.
    private String worldName;

    private boolean hasWinner = false;

    public Redstone(Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns, ArenaUtil arenaUtil, YamlStorage arenaStorage, TabController tabController) {
        super(arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController, Gamemodes.LTS);
        for (ArenaTeam arenaTeam : arena.getTeams()) {
            redstoneLocations.put(arenaTeam, Util.getLocation(arenaStorage, "gamemodes.redstone.locations." + arenaTeam.getTeamNumber()));
        }
        worldName = redstoneLocations.get(arena.getTeam(0)).getWorld().getName();
    }

    @Override
    public String getName() {
        return "Redstone";
    }

    @Override
    public boolean isTeamGame() {
        return true;
    }

    @Override
    public void onRedstoneUpdate(BlockRedstoneEvent event) {
        super.onRedstoneUpdate(event);
        //If haswinner is true, a team has already won but game hasn't stopped yet.
        if(hasWinner)
            return;
        if(event.getBlock().getWorld().getName().equals(worldName) && event.getNewCurrent() > event.getOldCurrent()){
            if (arena.getArenaState() == ArenaState.PLAYING){
                for (ArenaTeam arenaTeam : arena.getTeams()) {
                    if(event.getBlock().getLocation().distanceSquared(redstoneLocations.get(arenaTeam)) < 1){
                        hasWinner = true;
                        arena.stopGame(arenaTeam);
                    }
                }
            }
        }
    }

    @Override
    public void updateTabs() {
        if (!tabController.hasTabAPI())
            return;

        String status = Util.capaltizeFirstLetter(arena.getArenaState().toString());
        String arenaName = arena.getArenaName();
        int nrOfPlayers = 0;

        List<String[]> teamStrings = new ArrayList<>();
        List<ArenaTeam> teams = arena.getTeams();
        for (ArenaTeam team : teams) {
            int x = 0;
            String[] teamString = new String[team.getPlayers().size() + 1];
            teamString[x] = team.getTeamColor() + "-" + team.getTeamName() + "-";
            for (ArenaPlayer teamPlayer : team.getPlayers()) {
                x++;
                teamString[x] = teamPlayer.getPlayerName();
                nrOfPlayers++;
            }
            teamStrings.add(teamString);
        }

        String playersString = nrOfPlayers + " Players";
        String spectators = ChatColor.GRAY + "" + arena.getArenaSpectators().getSpectators().size() + " Spectators";

        for (ArenaTeam team : teams) {
            for (ArenaPlayer player : team.getPlayers()) {
                Player p = Bukkit.getPlayerExact(player.getPlayerName());
                if (p != null) {
                    setTabPlayer(p, status, arenaName, playersString, spectators, teamStrings);
                }
            }
        }
        for (Player p : arena.getArenaSpectators().getSpectators().keySet()) {
            setTabPlayer(p, status, arenaName, playersString, spectators, teamStrings);
        }
        TabAPI.updateAll();
    }

    private void setTabPlayer(Player p, String status, String arena, String players, String spectators, List<String[]> teamStrings) {
        int row = tabController.setTopTab(p, Gamemodes.REDSTONE);
        TabAPI.setTabString(plugin, p, 2, 1, status);
        TabAPI.setTabString(plugin, p, 3, 1, arena);
        TabAPI.setTabString(plugin, p, 4, 1, players);
        TabAPI.setTabString(plugin, p, 4, 2, spectators);

        setTabPlayerTeam(p, row, 0, teamStrings.get(0));
        setTabPlayerTeam(p, row, 2, teamStrings.get(1));

        switch (teamStrings.size()) {
            case 5:
                setTabPlayerTeam(p, 10, 1, teamStrings.get(4));
            case 4:
                setTabPlayerTeam(p, 13, 0, teamStrings.get(2));
                setTabPlayerTeam(p, 13, 2, teamStrings.get(3));
                break;
            case 3:
                setTabPlayerTeam(p, 13, 1, teamStrings.get(2));
                break;
        }

    }

    private void setTabPlayerTeam(Player p, int row, int colom, String[] team) {
        int x = 1;
        for (String cell : team) {
            TabAPI.setTabString(plugin, p, row, colom, cell);
            row++;
            x++;
            if (x > 6)
                return;
        }
    }

    @Override
    public void sortLists() {
        for (ArenaTeam team : arena.getTeams()) {
            Collections.sort(team.getPlayers(), comp);
        }
    }

    @Override
    protected void createComp() {
        comp = new Comparator<ArenaPlayer>() {
            @Override
            public int compare(ArenaPlayer o1, ArenaPlayer o2) {
                ArenaPlayerState o1State = o1.getPlayerState();
                ArenaPlayerState o2State = o2.getPlayerState();
                if (o1State != ArenaPlayerState.PLAYING && o2State == ArenaPlayerState.PLAYING)
                    return 1;
                if (o1State == ArenaPlayerState.PLAYING && o2State != ArenaPlayerState.PLAYING)
                    return -1;
                return 0;
            }
        };
    }


    public AbstractCommand handleGamemodeCommand(CommandSender sender, String command, String[] cmdArgs) {
        AbstractCommand commandObj = null;
        if (command.equals("setredstone")) {
            commandObj = new CommandSetRedstone(sender, cmdArgs, arena);
        }
        return commandObj;
    }
}
