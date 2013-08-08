package me.naithantu.ArenaPVP.Arena.ArenaExtras;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Gamemodes.Gamemodes.CTF;
import me.naithantu.ArenaPVP.Gamemodes.Gamemodes.LTS;
import me.naithantu.ArenaPVP.Gamemodes.Gamemodes.TDM;
import me.naithantu.ArenaPVP.Storage.YamlStorage;

public class ArenaGamemode {

	/*
	 * Could also use reflections to do this... not sure what is best We'll just
	 * stick with coded in gamemodes for now, might switch to reflections later
	 */

	public static Gamemode getGamemode(ArenaPVP plugin, ArenaManager arenaManager, Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns, ArenaUtil arenaUtil, YamlStorage arenaStorage, String name) {
		System.out.println(name);
		if (name.equalsIgnoreCase("tdm")) {
			return new TDM(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage);
		} else if (name.equalsIgnoreCase("lts")) {
			return new LTS(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage);
		} else if (name.equalsIgnoreCase("ctf")){
			return new CTF(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage);
		}
		return null;
	}
}
