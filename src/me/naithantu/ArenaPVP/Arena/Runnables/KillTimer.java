package me.naithantu.ArenaPVP.Arena.Runnables;

import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.PlayerExtras.PlayerTimer;
import me.naithantu.ArenaPVP.Arena.PlayerExtras.PlayerTimers;
import me.naithantu.ArenaPVP.ArenaPVP;
import org.bukkit.entity.Player;

/**
 * Class used to kill a player after 1 tick.
 */
public class KillTimer extends PlayerTimer{
    private Player player;
    private ArenaPlayer arenaPlayer;

    public KillTimer(ArenaPlayer arenaPlayer, Player player) {
        super(arenaPlayer.getTimers());
        this.player = player;
        this.arenaPlayer = arenaPlayer;
        arenaPlayer.setDying(true);
    }

    @Override
    public void run(){
        super.run();
        player.setHealth(0);
        arenaPlayer.setDying(false);
    }
}
