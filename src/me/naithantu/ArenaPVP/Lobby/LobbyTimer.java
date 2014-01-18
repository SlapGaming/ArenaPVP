package me.naithantu.ArenaPVP.Lobby;

import org.bukkit.scheduler.BukkitRunnable;

public class LobbyTimer extends BukkitRunnable {
    private Lobby lobby;
    private int secondsRemaining = 120;

    public LobbyTimer(Lobby lobby){
        this.lobby = lobby;
    }

    @Override
    public void run() {
        secondsRemaining--;
        if(secondsRemaining == 110){
            //lobby.get
        }
    }
}
