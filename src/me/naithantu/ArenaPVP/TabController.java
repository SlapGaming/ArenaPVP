package me.naithantu.ArenaPVP;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mcsg.double0negative.tabapi.TabAPI;

public class TabController {
	
	private ArenaPVP plugin;

	private boolean tabApiFound;
	
	public TabController(ArenaPVP plugin) {
		this.plugin = plugin;
		tabApiFound = findTabAPI();
	}
	
	private boolean findTabAPI(){
		boolean returnBool = false;
		TabAPI api = (TabAPI) plugin.getServer().getPluginManager().getPlugin("TabAPI");
		if (api != null) returnBool = true;
		return returnBool;
	}
	
	public boolean hasTabAPI() {
		return tabApiFound;
	}
	
	public enum Gamemodes {
		DUEL, SPLEEF, DM, TDM, CTF, LMS, LTS
	}
	
	public int setTopTab(Player p, Gamemodes gm) {
		int returnInt = 5;
		
		TabAPI.clearTab(p);
		TabAPI.setPriority(plugin, p, 2);
		
		//Default stuff
		TabAPI.setTabString(plugin, p, 0, 1, ChatColor.GOLD + "-- PVPArena --");
		TabAPI.setTabString(plugin, p, 1, 0, ChatColor.YELLOW + " -------------");
		TabAPI.setTabString(plugin, p, 1, 1, ChatColor.YELLOW + "--------------");
		TabAPI.setTabString(plugin, p, 1, 2, ChatColor.YELLOW + "-------------");
		TabAPI.setTabString(plugin, p, 2, 0, ChatColor.GRAY + "Status   ->");
		TabAPI.setTabString(plugin, p, 4, 0, ChatColor.GRAY + "Players ->");
		
		switch (gm) {
		case CTF: 
			TabAPI.setTabString(plugin, p, 0, 0, ChatColor.DARK_AQUA + "         CTF");	
			TabAPI.setTabString(plugin, p, 0, 2, ChatColor.DARK_AQUA + "         CTF ");
			TabAPI.setTabString(plugin, p, 3, 0, ChatColor.GRAY + "Arena    ->");
			TabAPI.setTabString(plugin, p, 5, 0, ChatColor.GRAY + "Team     ->");
			TabAPI.setTabString(plugin, p, 7, 1, ChatColor.GOLD + "-- Teams --");
			returnInt = 8;
			break;
		case DM: 
			TabAPI.setTabString(plugin, p, 0, 0, ChatColor.DARK_AQUA + "   Deathmatch");	
			TabAPI.setTabString(plugin, p, 0, 2, ChatColor.DARK_AQUA + "   Deathmatch ");
			TabAPI.setTabString(plugin, p, 3, 0, ChatColor.GRAY + "Arena    ->");
			TabAPI.setTabString(plugin, p, 5, 0, ChatColor.GRAY + "Score    ->");
			TabAPI.setTabString(plugin, p, 7, 1, ChatColor.GOLD + "-- Players --");
			returnInt = 8;
			break;
		case DUEL: 
			TabAPI.setTabString(plugin, p, 0, 0, ChatColor.DARK_AQUA + "        Duel");	
			TabAPI.setTabString(plugin, p, 0, 2, ChatColor.DARK_AQUA + "        Duel ");
			TabAPI.setTabString(plugin, p, 3, 0, ChatColor.GRAY + "Arena    ->");
			TabAPI.setTabString(plugin, p, 5, 0, ChatColor.GRAY + "Score    ->");
			TabAPI.setTabString(plugin, p, 7, 1, ChatColor.GOLD + "-- Players --");
			returnInt = 8;
			break;
		case LMS:
			TabAPI.setTabString(plugin, p, 0, 0, ChatColor.DARK_AQUA + "         LMS");	
			TabAPI.setTabString(plugin, p, 0, 2, ChatColor.DARK_AQUA + "         LMS ");
			TabAPI.setTabString(plugin, p, 3, 0, ChatColor.GRAY + "Arena    ->");
			TabAPI.setTabString(plugin, p, 6, 1, ChatColor.GOLD + "-- Players --");
			returnInt = 7;
			break;
		case LTS: 
			TabAPI.setTabString(plugin, p, 0, 0, ChatColor.DARK_AQUA + "         LTS");	
			TabAPI.setTabString(plugin, p, 0, 2, ChatColor.DARK_AQUA + "         LTS ");
			TabAPI.setTabString(plugin, p, 3, 0, ChatColor.GRAY + "Arena    ->");
			TabAPI.setTabString(plugin, p, 6, 1, ChatColor.GOLD + "-- Teams --");
			returnInt = 7;
			break;
		case SPLEEF: 
			TabAPI.setTabString(plugin, p, 0, 0, ChatColor.DARK_AQUA + "      Spleef!");		
			TabAPI.setTabString(plugin, p, 0, 2, ChatColor.DARK_AQUA + "      Spleef! ");
			TabAPI.setTabString(plugin, p, 3, 0, ChatColor.GRAY + "Type     ->");
			TabAPI.setTabString(plugin, p, 6, 1, ChatColor.GOLD + "-- Players --");
			returnInt = 7;
			break;
		case TDM: 
			TabAPI.setTabString(plugin, p, 0, 0, ChatColor.DARK_AQUA + "         TDM");	
			TabAPI.setTabString(plugin, p, 0, 2, ChatColor.DARK_AQUA + "         TDM ");
			TabAPI.setTabString(plugin, p, 3, 0, ChatColor.GRAY + "Arena    ->");
			TabAPI.setTabString(plugin, p, 5, 0, ChatColor.GRAY + "Team     ->");
			TabAPI.setTabString(plugin, p, 7, 1, ChatColor.GOLD + "-- Teams --");
			returnInt = 8;
			break;
		}
		return returnInt;
	}

}
