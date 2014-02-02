package me.naithantu.ArenaPVP.Gamemodes.Gamemodes.CTF;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaPlayerState;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSpawns;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaUtil;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;
import me.naithantu.ArenaPVP.Arena.Settings.ArenaSettings;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.IconMenu;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.TabController;
import me.naithantu.ArenaPVP.Util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.mcsg.double0negative.tabapi.TabAPI;

import java.util.*;
import java.util.Map.Entry;

public class CTF extends Gamemode {

    private CTFMenu ctfMenu;

    private HashMap<String, ArenaTeam> flags = new HashMap<String, ArenaTeam>();
    private HashMap<ArenaTeam, ItemStack> flagBlocks = new HashMap<ArenaTeam, ItemStack>();
    private HashMap<ArenaTeam, Location> flagLocations = new HashMap<ArenaTeam, Location>();

    //The world the flags are in.
    private String worldName;

    //Contains names of players who got a help message, will be removed when they move away from the flag (to prevent spam)
    HashSet<String> gotInfoMessage = new HashSet<String>();
    private Comparator<ArenaTeam> comp;

    public CTF(Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns, ArenaUtil arenaUtil, YamlStorage arenaStorage, TabController tabController) {
        super(arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController, Gamemodes.CTF);
        //Make sure config contains all necessary values
        arenaStorage.copyDefaultConfig("me/naithantu/ArenaPVP/Gamemodes/Gamemodes/CTF/defaults.yml");

        ctfMenu = new CTFMenu(arena, arenaStorage, this);

        initialize();
    }

    public void initialize(){
        for (ArenaTeam arenaTeam : arena.getTeams()) {
            flagLocations.put(arenaTeam, Util.getLocation(arenaStorage, "gamemodes.ctf.flags." + arenaTeam.getTeamNumber()));
            ItemStack flagBlock = (ItemStack) arenaStorage.getConfig().get("gamemodes.ctf.blocks." + arenaTeam.getTeamNumber());
            flagBlocks.put(arenaTeam, flagBlock);
        }
        worldName = flagLocations.get(arena.getTeam(0)).getWorld().getName();
    }

    @Override
    public String getName() {
        return "CTF";
    }

    @Override
    public boolean isTeamGame() {
        return true;
    }

    @Override
    public boolean hasConfigSettings() {
        return true;
    }

    @Override
    public void setupIconMenu(IconMenu iconMenu){
        ctfMenu.setupIconMenu(iconMenu);
    }

    @Override
    public void handleMenuClick(IconMenu.OptionClickEvent event){
        ctfMenu.handleMenuClick(event);
    }

    @Override
    public void stopChanging(){
        ctfMenu.stopChanging();
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event, ArenaPlayer arenaPlayer) {
        super.onPlayerMove(event, arenaPlayer);

        //Check if player is alive (playermoveevent is still called after player died).
        if (event.getPlayer().isDead())
            return;

        Location from = event.getFrom();
        Location to = event.getTo();

        boolean nearFlag = false;

        //Check if player actually moved.
        if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
            if (arena.getArenaState() == ArenaState.PLAYING && arenaPlayer.getPlayerState() == ArenaPlayerState.PLAYING) {
                //Check if player is in same world as flag.
                if (to.getWorld().getName().equals(worldName)) {
                    Player player = event.getPlayer();
                    String playerName = player.getName();
                    //If player has flag, check if he can capture it.
                    if (flags.containsKey(playerName)) {
                        ArenaTeam team = arenaPlayer.getTeam();
                        if (to.distanceSquared(flagLocations.get(team)) < 1) {
                            nearFlag = true;
                            if (getFlagCarrier(team) == null) {
                                ArenaTeam stolenTeam = flags.get(playerName);
                                arenaUtil.sendMessageAll(team.getTeamColor() + playerName + ChatColor.WHITE + " has captured the " + stolenTeam.getColoredName() + " flag!");
                                flags.remove(playerName);
                                player.getInventory().setArmorContents(arenaPlayer.getTeam().getArmor());
                                team.addScore();
                                if (team.getScore() >= settings.getScoreLimit()) {
                                    arena.getArenaGameController().stopGame(team);
                                } else {
                                    if (tabController.hasTabAPI()) {
                                        updateTabs();
                                    } else {
                                        sortLists();
                                        sendScoreAll();
                                    }
                                }
                            } else {
                                if (!gotInfoMessage.contains(player.getName())) {
                                    Util.msg(player, "You can not capture a flag when the enemy has your flag!");
                                    gotInfoMessage.add(player.getName());
                                }
                            }
                        }
                    } else {
                        for (ArenaTeam arenaTeam : arena.getTeams()) {
                            // Take enemy flag (can't take own flag & flag can't already be taken).
                            if (!arenaPlayer.getTeam().equals(arenaTeam)) {
                                if (to.distanceSquared(flagLocations.get(arenaTeam)) < 1) {
                                    nearFlag = true;
                                    //Check if flag is already taken.
                                    if (getFlagCarrier(arenaTeam) == null) {
                                        flags.put(player.getName(), arenaTeam);
                                        player.getInventory().setHelmet(flagBlocks.get(arenaTeam));
                                        arenaUtil.sendMessageAll(arenaPlayer.getTeam().getTeamColor() + playerName + ChatColor.WHITE + " has taken the " + arenaTeam.getColoredName() + " flag!");
                                        updateTabs();
                                    } else {
                                            if (!gotInfoMessage.contains(player.getName())) {
                                            Util.msg(player, "Someone in your team already has the enemy flag!");
                                            gotInfoMessage.add(player.getName());
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (!nearFlag && gotInfoMessage.contains(player.getName())) {
                        gotInfoMessage.remove(player.getName());
                    }
                }
            }
        }
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event, ArenaPlayer arenaPlayer) {
        super.onPlayerDeath(event, arenaPlayer);
        String playerName = event.getEntity().getName();
        if (flags.containsKey(playerName)) {
            ArenaTeam stolenTeam = flags.get(arenaPlayer.getPlayerName());
            arenaUtil.sendMessageAll("The " + stolenTeam.getColoredName() + " flag has been returned!");
            flags.remove(playerName);
            event.getEntity().getInventory().setArmorContents(arenaPlayer.getTeam().getArmor());
            updateTabs();
        }
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event, ArenaPlayer arenaPlayer) {
        super.onPlayerQuit(event, arenaPlayer);
        String playerName = event.getPlayer().getName();
        if (flags.containsKey(playerName)) {
            ArenaTeam stolenTeam = flags.get(arenaPlayer.getPlayerName());
            arenaUtil.sendMessageAll("The " + stolenTeam.getColoredName() + " flag has been returned!");
            flags.remove(playerName);
            updateTabs();
        }
    }

    public String getFlagCarrier(ArenaTeam team) {
        for (Entry<String, ArenaTeam> entry : flags.entrySet()) {
            if (entry.getValue().equals(team)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public void updateTabs() {
        sortLists();
        if (!tabController.hasTabAPI()) return;

        String status = Util.capitalizeFirstLetter(arena.getArenaState().toString());
        String arenaName = arena.getArenaName();
        String spectators = ChatColor.GRAY + "" + arena.getArenaSpectators().getSpectators().size() + " Spectators";
        List<ArenaTeam> teams = arena.getTeams();
        int nrOfPlayers = 0;
        String[] teamString = new String[teams.size() * 3];
        String[][] players = new String[teams.size()][];

        int x = 0;
        int z = 0;
        for (ArenaTeam team : teams) {
            List<ArenaPlayer> playerList = team.getPlayers();
            players[x] = new String[playerList.size()];
            ChatColor teamColor = team.getTeamColor();
            int y = 0;
            for (ArenaPlayer player : playerList) {
                players[x][y] = teamColor + player.getPlayerName();
                y++;
                nrOfPlayers++;
            }
            if (flags.containsValue(team)) {
                for(Map.Entry<String, ArenaTeam> entrySet: flags.entrySet()){
                    if(entrySet.getValue() == team){
                        teamString[z] = teamColor + "Flag; " + entrySet.getKey();
                    }
                }
            } else {
                teamString[z] = teamColor + "Flag in place";
            }
            z++;
            teamString[z] = teamColor + "-" + team.getTeamName() + "-";
            z++;
            teamString[z] = teamColor + "" + team.getScore() + " Points";
            z++;
            x++;
        }

        String playerString = nrOfPlayers + " (" + teams.size() + " Teams)";
        int rank = 1;
        for (ArenaTeam team : teams) {
            String teamName = team.getColoredName();
            String ranking = ChatColor.GREEN + "Rank " + rank;
            for (ArenaPlayer player : team.getPlayers()) {
                Player p = Bukkit.getPlayerExact(player.getPlayerName());
                if (p != null) {
                    setTabPlayer(p, status, arenaName, playerString, spectators, false, teamName, ranking, teamString, players);
                }
            }
        }
        for (Player p : arena.getArenaSpectators().getSpectators().keySet()) {
            setTabPlayer(p, status, arenaName, playerString, spectators, true, null, null, teamString, players);
        }
        TabAPI.updateAll();
    }

    private void setTabPlayer(Player p, String status, String arena, String playerString, String spectators, boolean spectator, String team, String rank, String[] teams, String[][] players) {
        int row = tabController.setTopTab(p, Gamemodes.CTF);
        TabAPI.setTabString(plugin, p, 2, 1, status);
        TabAPI.setTabString(plugin, p, 3, 1, arena);
        TabAPI.setTabString(plugin, p, 4, 1, playerString);
        TabAPI.setTabString(plugin, p, 4, 2, spectators);
        if (spectator) {
            TabAPI.setTabString(plugin, p, 5, 1, ChatColor.GRAY + "Spectator");
        } else {
            TabAPI.setTabString(plugin, p, 5, 1, team);
            TabAPI.setTabString(plugin, p, 5, 2, rank);
        }

        int colom = 0;
        for (String cell : teams) {
            if (cell != null) TabAPI.setTabString(plugin, p, row, colom, cell);
            colom++;
            if (colom == 3) {
                row++;
                if (row > 19) return;
                colom = 0;
            }
        }

        row++;
        row++;
        if (row < 18) {
            TabAPI.setTabString(plugin, p, row, 1, ChatColor.GOLD + "-- Players --");
            row++;
            colom = 0;
            for (String[] teamPlayers : players) {
                for (String aPlayer : teamPlayers) {
                    if (aPlayer != null) TabAPI.setTabString(plugin, p, row, colom, aPlayer);
                    colom++;
                    if (colom == 3) {
                        row++;
                        if (row > 19) return;
                        colom = 0;
                    }
                }
            }
        }
    }

    @Override
    public void sortLists() {
        Collections.sort(arena.getTeams(), comp);
    }

    @Override
    protected void createComp() {
        comp = new Comparator<ArenaTeam>() {
            @Override
            public int compare(ArenaTeam o1, ArenaTeam o2) {
                if (o1.getScore() < o2.getScore()) return 1;
                if (o1.getScore() > o2.getScore()) return -1;
                return 0;
            }
        };
    }
}
