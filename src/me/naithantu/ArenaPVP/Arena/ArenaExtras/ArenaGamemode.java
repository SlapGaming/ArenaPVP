package me.naithantu.ArenaPVP.Arena.ArenaExtras;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Gamemodes.Gamemodes.*;
import me.naithantu.ArenaPVP.Gamemodes.Gamemodes.Paintball.Paintball;
import me.naithantu.ArenaPVP.TabController;
import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Gamemodes.Gamemodes.CTF.CTF;
import me.naithantu.ArenaPVP.Storage.YamlStorage;

public class ArenaGamemode {

	/*
	 * Could also use reflections to do this... not sure what is best We'll just
	 * stick with coded in gamemodes for now, might switch to reflections later
	 */

	public static Gamemode getGamemode(ArenaPVP plugin, ArenaManager arenaManager, Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns, ArenaUtil arenaUtil, YamlStorage arenaStorage, String name, TabController tabController) {
		switch(name.toLowerCase()) {
		    case "ctf": 	return new CTF(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
		    case "ffa": 		return new FFA(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
		    case "lms":		return new LMS(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
		    case "lts":		return new LTS(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
		    case "tdm":		return new TDM(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
            case "paintball": return new Paintball(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
            case "oitc": return new OITC(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage, tabController);
	    	default:
			    return null;
		}
	}
}
