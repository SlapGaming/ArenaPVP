package me.naithantu.ArenaPVP.Objects.ArenaExtras;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Gamemodes.Gamemodes.TDM;
import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Storage.YamlStorage;

public class ArenaGamemode {

	/*
	 * Could also use reflections to do this... not sure what is best We'll just
	 * stick with coded in gamemodes for now, might switch to reflections later
	 */

	public static Gamemode getGamemode(ArenaPVP plugin, ArenaManager arenaManager, Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns, ArenaUtil arenaUtil, YamlStorage arenaStorage, String name) {
		if (name.equalsIgnoreCase("tdm")) {
			return new TDM(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage);
		} else if (name.equalsIgnoreCase("somethingelse")) {
			//Add more here durr
		}
		return null;
	}
}
