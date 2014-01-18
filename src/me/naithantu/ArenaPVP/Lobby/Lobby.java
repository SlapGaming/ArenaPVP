package me.naithantu.ArenaPVP.Lobby;

import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.Util.PlayerConfigUtil;
import me.naithantu.ArenaPVP.Util.Util;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    private LobbyTimer lobbyTimer;
    private LobbyEventsHandler lobbyEventsHandler;
    private Vote vote;

    List<Player> players = new ArrayList<>();

    YamlStorage lobbyStorage;
    FileConfiguration lobbyConfig;

    public Lobby(String lobbyName){
        lobbyStorage = new YamlStorage("lobby", lobbyName);
        lobbyConfig = lobbyStorage.getConfig();

        lobbyTimer = new LobbyTimer(this);
        lobbyEventsHandler = new LobbyEventsHandler(this);
        vote = new Vote(this);

        //StartVoteTimer
    }

    public LobbyTimer getLobbyTimer(){
        return lobbyTimer;
    }

    public void joinLobby(Player player){
        Util.msg(player, "You have joined the lobby!");

        PlayerConfigUtil.savePlayerConfig(player, new YamlStorage("players", player.getName()), getSpawnLocation());
        players.add(player);
    }

    public Location getSpawnLocation(){
        return Util.getLocation(lobbyStorage, "spawn");
    }
}
