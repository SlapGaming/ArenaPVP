package me.naithantu.ArenaPVP.Arena;

import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaPlayerState;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSpawns.SpawnType;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ArenaTeam {
	ItemStack[] inventory;
	ItemStack[] armor;

	ArenaPVP plugin = ArenaPVP.getInstance();
	Configuration arenaConfig;
	int teamNumber;

	String teamName;
	ChatColor teamColor;

	int score = 0;

	List<ArenaPlayer> players = new ArrayList<ArenaPlayer>();

	public ArenaTeam(Configuration arenaConfig, int teamNumber) {
		this.arenaConfig = arenaConfig;
		this.teamNumber = teamNumber;
        setupTeam();
	}

    @SuppressWarnings("unchecked")
    public void setupTeam(){
        this.teamColor = ChatColor.getByChar(arenaConfig.getString("teamcolors." + teamNumber));

        teamName = arenaConfig.getString("teams." + teamNumber);
        List<ItemStack> inventoryContents = (List<ItemStack>) arenaConfig.getList("classes." + teamNumber + ".inventory");
        inventory = inventoryContents.toArray(new ItemStack[36]);
        List<ItemStack> armorContents = (List<ItemStack>) arenaConfig.getList("classes." + teamNumber + ".armor");
        armor = armorContents.toArray(new ItemStack[4]);
    }
	
	public int getTeamNumber() {
		return teamNumber;
	}

	public String getTeamName() {
		return teamName;
	}
	
	public ChatColor getTeamColor() {
		return teamColor;
	}

    public String getColoredName() {
        return teamColor + teamName + ChatColor.WHITE;
    }

	public List<ArenaPlayer> getPlayers() {
		return players;
	}

	public ItemStack[] getInventory() {
		return inventory;
	}

	public ItemStack[] getArmor() {
		return armor;
	}

	public void addScore() {
		score++;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void joinTeam(Player player, Arena arena) {
		ArenaPlayer arenaPlayer = new ArenaPlayer(plugin, player, arena, this);
		ArenaManager.addPlayer(arenaPlayer);
		joinTeam(player, arena, arenaPlayer);
	}

	public void joinTeam(Player player, Arena arena, ArenaPlayer arenaPlayer) {
		players.add(arenaPlayer);
		if (arena.getArenaState() == ArenaState.PLAYING && arenaPlayer.getPlayerState() != ArenaPlayerState.SPECTATING) {
			if (arena.getSettings().getRespawnTime() == 0) {
				player.teleport(arena.getArenaSpawns().getRespawnLocation(player, arenaPlayer, SpawnType.PLAYER));
			} else {
				arenaPlayer.getTimers().startRespawnTimer(player, SpawnType.PLAYER);
			}
		} else {
			player.teleport(arena.getArenaSpawns().getRespawnLocation(player, arenaPlayer, SpawnType.SPECTATOR));
		}
	}

	public void leaveTeam(ArenaPlayer arenaPlayer, Player player) {
		ArenaManager.removePlayer(arenaPlayer);
		players.remove(arenaPlayer);
		arenaPlayer.getTimers().cancelAllTimers();
	}
}
