package me.naithantu.ArenaPVP.Objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaSpawns.SpawnType;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaState;

public class ArenaTeam {
	ItemStack[] inventory;
	ItemStack[] armor;
	
	ArenaPVP plugin;
	Configuration arenaConfig;
	int teamNumber;
	
	String teamName;
	
	int score = 0;
	
	List<ArenaPlayer> players = new ArrayList<ArenaPlayer>();
	
	@SuppressWarnings("unchecked")
	public ArenaTeam(ArenaPVP plugin, Configuration arenaConfig, int teamNumber){
		this.plugin = plugin;
		this.arenaConfig = arenaConfig;
		this.teamNumber = teamNumber;
		
		teamName = arenaConfig.getString("teams." + teamNumber);
		List<ItemStack> inventoryContents = (List<ItemStack>) arenaConfig.getList("classes." + teamNumber + ".inventory");
		inventory = inventoryContents.toArray(new ItemStack[36]);
		List<ItemStack> armorContents = (List<ItemStack>) arenaConfig.getList("classes." + teamNumber + ".armor");
		armor = armorContents.toArray(new ItemStack[4]);
	}
	
	public String getTeamName(){
		return teamName;
	}
	
	public void setTeamName(String teamName){
		this.teamName = teamName;
	}
	
	public List<ArenaPlayer> getPlayers(){
		return players;
	}
	
	public ItemStack[] getInventory(){
		return inventory;
	}
	
	public ItemStack[] getArmor(){
		return armor;
	}
		
	public void addScore(){
		score++;
	}
	
	public int getScore(){
		return score;
	}
	
	public void setScore(int score){
		this.score = score;
	}
	
	public void joinTeam(Player player, ArenaManager arenaManager, Arena arena){
		ArenaPlayer arenaPlayer = new ArenaPlayer(plugin, player.getName(), arena, this);
		arenaManager.addPlayer(arenaPlayer);
		joinTeam(player, arenaManager, arena, arenaPlayer);
	}
	
	public void joinTeam(Player player, ArenaManager arenaManager, Arena arena, ArenaPlayer arenaPlayer){
		players.add(arenaPlayer);
		if(arena.getArenaState() == ArenaState.PLAYING){
			if (arena.getSettings().getRespawnTime() == 0) {
					player.teleport(arena.getArenaSpawns().getRespawnLocation(player, arenaPlayer, SpawnType.PLAYER));
			} else {
					arena.getArenaSpawns().addRespawnTimer(player, arenaPlayer, SpawnType.PLAYER);
			}
		} else {
			player.teleport(arena.getArenaSpawns().getRespawnLocation(player, arenaPlayer, SpawnType.SPECTATOR));
		}
	}
	
	public void leaveTeam(ArenaManager arenaManager, ArenaPlayer arenaPlayer, Player player){
		arenaManager.removePlayer(arenaPlayer);
		players.remove(arenaPlayer);
	}
}
