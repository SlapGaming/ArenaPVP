package me.naithantu.ArenaPVP.Objects.ArenaExtras;

public enum ArenaPlayerState {
	
	/*
	 * The arenaplayers state.
	 * respawning = in spectator area waiting for respawn timer to finish.
	 * dead = on respawn screen.
	 * playing = alive and not waiting for respawning
	 * be aware that playing does not always mean they are actually fighting
	 * check the ArenaState for that.
	 */
	PLAYING, RESPAWNING, DEAD
}
