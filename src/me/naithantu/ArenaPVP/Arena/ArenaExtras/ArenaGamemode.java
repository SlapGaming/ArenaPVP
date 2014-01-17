package me.naithantu.ArenaPVP.Arena.ArenaExtras;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.Settings.ArenaSettings;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Gamemodes.Gamemodes.CTF.CTF;
import me.naithantu.ArenaPVP.Gamemodes.Gamemodes.*;
import me.naithantu.ArenaPVP.Gamemodes.Gamemodes.Paintball.Paintball;
import me.naithantu.ArenaPVP.Gamemodes.Gamemodes.Redstone.Redstone;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.TabController;

public class ArenaGamemode {

	/*
	 * Could also use reflections to do this... not sure what is best We'll just
	 * stick with coded in gamemodes for now, might switch to reflections later
	 */

	public static Gamemode getGamemode(Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns, ArenaUtil arenaUtil, YamlStorage arenaStorage, String name, TabController tabController) {
		switch(name.toLowerCase()) {
		    case "ctf": 	return new CTF(arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
		    case "ffa": 		return new FFA(arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
		    case "lms":		return new LMS(arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
		    case "lts":		return new LTS(arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
            case "redstone": return new Redstone(arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
		    case "tdm":		return new TDM(arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
            case "paintball": return new Paintball(arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
            case "oitc": return new OITC(arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
	    	default:
			    return null;
		}
	}
}
