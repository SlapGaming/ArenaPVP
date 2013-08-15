package me.naithantu.ArenaPVP.Arena.ArenaExtras;

public enum ArenaPlayerState {
	
	/*
	 * The arenaplayers state.
	 * respawning = in spectator area waiting for respawn timer to finish.
	 * playing = alive and not waiting for respawning
	 * spectating = used all respawns (or spectating and in arena list, not a team).
	 * be aware that playing does not always mean they are actually fighting
	 * check the ArenaState for that.
	 */
	PLAYING, RESPAWNING, SPECTATING
}
