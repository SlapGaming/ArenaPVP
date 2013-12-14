package me.naithantu.ArenaPVP.Arena.PlayerExtras;

import org.bukkit.scheduler.BukkitRunnable;

public class PlayerTimer extends BukkitRunnable {
    private PlayerTimers playerTimers;

    public PlayerTimer(PlayerTimers playerTimers){
        this.playerTimers = playerTimers;
        playerTimers.addTimer(this);
    }

    @Override
    public void run() {
        playerTimers.removeTimer(this);
    }
}
