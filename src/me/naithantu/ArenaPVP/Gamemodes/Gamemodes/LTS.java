package me.naithantu.ArenaPVP.Gamemodes.Gamemodes;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Gamemodes.Gamemode;
import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaSettings;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaSpawns;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaUtil;
import me.naithantu.ArenaPVP.Storage.YamlStorage;

public class LTS extends Gamemode {

	public LTS(ArenaPVP plugin, ArenaManager arenaManager, Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns, ArenaUtil arenaUtil, YamlStorage arenaStorage) {
		super(plugin, arenaManager, arena, settings, arenaSpawns, arenaUtil, arenaStorage);
	}

	
}
