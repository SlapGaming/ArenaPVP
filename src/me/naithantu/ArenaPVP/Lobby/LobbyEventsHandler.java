package me.naithantu.ArenaPVP.Lobby;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class LobbyEventsHandler {

    private Lobby lobby;

    public LobbyEventsHandler(Lobby lobby){
        this.lobby = lobby;
    }

    public void onPlayerDamage(EntityDamageEvent event){
        event.setCancelled(true);
    }

    public void onPlayerDeath(PlayerDeathEvent event){

    }

    public void onPlayerRespawn(PlayerRespawnEvent event){
        event.setRespawnLocation(lobby.getSpawnLocation());
    }
}
