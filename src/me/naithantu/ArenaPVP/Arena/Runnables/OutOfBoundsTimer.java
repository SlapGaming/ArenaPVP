package me.naithantu.ArenaPVP.Arena.Runnables;

import me.naithantu.ArenaPVP.Arena.PlayerExtras.PlayerTimers;
import me.naithantu.ArenaPVP.Util.Util;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class OutOfBoundsTimer extends BukkitRunnable {
	PlayerTimers timers;
	Player player;
	int secondsLeft;
	
	public OutOfBoundsTimer(PlayerTimers timers, Player player, int outOfBoundsTime) {
		this.timers = timers;
		this.player = player;
		secondsLeft = outOfBoundsTime + 1;
	}

	@Override
	public void run() {
		secondsLeft--;
		if (secondsLeft == 0) {
			timers.killOutOfBounds(player);
		} else {
			Util.msg(player, "You have " + secondsLeft + " seconds remaining to return to the combat area!");
		}
	}
}
