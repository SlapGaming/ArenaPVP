package me.naithantu.ArenaPVP.Gamemodes;

import java.util.List;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Commands.AbstractCommand;
import me.naithantu.ArenaPVP.Events.ArenaEvents.EventJoinArena;
import me.naithantu.ArenaPVP.Events.ArenaEvents.EventLeaveArena;
import me.naithantu.ArenaPVP.Events.ArenaEvents.EventRespawn;
import me.naithantu.ArenaPVP.Events.BukkitEvents.ArenaPlayerDamageEvent;
import me.naithantu.ArenaPVP.Events.BukkitEvents.ArenaPlayerDeathEvent;
import me.naithantu.ArenaPVP.Events.BukkitEvents.ArenaPlayerRespawnEvent;
import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Objects.ArenaPlayer;
import me.naithantu.ArenaPVP.Objects.ArenaTeam;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaSettings;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaSpawns;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaSpawns.SpawnType;
import me.naithantu.ArenaPVP.Objects.ArenaExtras.ArenaUtil;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.Util.Util;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Gamemode {
	protected ArenaPVP plugin;
	protected ArenaManager arenaManager;
	protected Arena arena;
	protected ArenaSettings settings;
	protected ArenaSpawns arenaSpawns;
	protected ArenaUtil arenaUtil;
	protected YamlStorage arenaStorage;
	protected Configuration arenaConfig;

	public Gamemode(ArenaPVP plugin, ArenaManager arenaManager, Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns, ArenaUtil arenaUtil, YamlStorage arenaStorage) {
		this.plugin = plugin;
		this.arenaManager = arenaManager;
		this.arena = arena;
		this.settings = settings;
		this.arenaSpawns = arenaSpawns;
		this.arenaUtil = arenaUtil;
		this.arenaStorage = arenaStorage;
		arenaConfig = arenaStorage.getConfig();
	}
	
	public String getName(){
		return "none";
	}

	public void sendScore(CommandSender sender, ArenaPlayer arenaPlayer) {
		Util.msg(sender, "Score:");
		for (ArenaTeam team : arena.getTeams()) {
			Util.msg(sender, "Team " + team.getTeamName() + ": " + team.getScore());
		}
	}

	public void onPlayerDeath(ArenaPlayerDeathEvent event) {
		event.setDroppedExp(0);
		event.setKeepLevel(true);
		event.getDrops().clear();

		arenaUtil.sendMessageAll(event.getDeathMessage());
		event.setDeathMessage(null);

		Player player = event.getEntity();
		ArenaPlayer arenaPlayer = event.getArenaPlayer();

		// Add death and kill to players.
		arenaPlayer.getPlayerScore().addDeath();
		if (player.getKiller() != null)
			arenaManager.getPlayerByName(player.getKiller().getName()).getPlayerScore().addKill();
	}

	public void onPlayerRespawn(ArenaPlayerRespawnEvent event) {
		ArenaPlayer arenaPlayer = event.getArenaPlayer();
		Player player = event.getPlayer();
		if (settings.getRespawnTime() == 0) {
			if (arenaPlayer.getArena().getArenaState() != ArenaState.PLAYING) {
				event.setRespawnLocation(arenaSpawns.getRespawnLocation(player, arenaPlayer, SpawnType.SPECTATOR));
			} else {
				System.out.println("Changing respawn loc...");
				event.setRespawnLocation(arenaSpawns.getRespawnLocation(player, arenaPlayer, SpawnType.PLAYER));
			}
		} else {
			if (arenaPlayer.getArena().getArenaState() != ArenaState.PLAYING) {
				arenaSpawns.addRespawnTimer(player, arenaPlayer, SpawnType.SPECTATOR);
			} else {
				arenaSpawns.addRespawnTimer(player, arenaPlayer, SpawnType.PLAYER);
			}
		}
	}

	public void onPlayerDamage(ArenaPlayerDamageEvent event) {

	}

	public void onPlayerMove(PlayerMoveEvent event) {

	}

	public void onPlayerQuit(PlayerQuitEvent event) {
		//TODO Create EventPlayerLeaveArena
		ArenaPlayer arenaPlayer = arenaManager.getPlayerByName(event.getPlayer().getName());
		arenaPlayer.getArena().leaveGame(arenaPlayer, arenaPlayer.getTeam());
	}

	// Arena made events.
	public void onPlayerJoinArena(EventJoinArena event) {
		ArenaState arenaState = arena.getArenaState();
		if (arenaState == ArenaState.WARMUP || arenaState == ArenaState.LOBBY) {
			ArenaTeam teamToJoin = event.getTeam();
			if (teamToJoin == null || settings.getMaxPlayers() > 0 && teamToJoin.getPlayers().size() >= settings.getMaxPlayers()) {
				for (ArenaTeam team : arena.getTeams()) {
					if (teamToJoin == null || team.getPlayers().size() < teamToJoin.getPlayers().size()) {
						teamToJoin = team;
					}
				}
				event.setTeam(teamToJoin);
				return;
			}
		}
		event.setCancelled(true);
	}

	public void onPlayerLeaveArena(EventLeaveArena event) {

	}

	@SuppressWarnings("unchecked")
	public void onPlayerArenaRespawn(final EventRespawn event) {
		if (event.getSpawnType() == SpawnType.PLAYER) {
			List<ItemStack> inventoryContents = (List<ItemStack>) arenaConfig.getList("inventory");
			List<ItemStack> armorContents = (List<ItemStack>) arenaConfig.getList("armor");

			System.out.println(event.getPlayer());
			PlayerInventory inventory = event.getPlayer().getInventory();

			inventory.setContents(inventoryContents.toArray(new ItemStack[36]));
			inventory.setArmorContents(armorContents.toArray(new ItemStack[4]));
		}
	}

	public AbstractCommand executeCommand(String command) {
		return null;
	}
}
