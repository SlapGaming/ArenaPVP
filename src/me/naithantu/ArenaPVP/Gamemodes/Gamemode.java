package me.naithantu.ArenaPVP.Gamemodes;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaArea;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaPlayerState;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSettings;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSpawns;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaUtil;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSpawns.SpawnType;
import me.naithantu.ArenaPVP.Commands.AbstractCommand;
import me.naithantu.ArenaPVP.Events.ArenaEvents.EventJoinArena;
import me.naithantu.ArenaPVP.Events.ArenaEvents.EventLeaveArena;
import me.naithantu.ArenaPVP.Events.ArenaEvents.EventRespawn;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.Util.Util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.PlayerInventory;

public class Gamemode {
	protected ArenaPVP plugin;
	protected ArenaManager arenaManager;
	protected Arena arena;
	protected ArenaSettings settings;
	protected ArenaSpawns arenaSpawns;
	protected ArenaArea arenaArea;
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
		arenaArea = arena.getArenaArea();
		arenaConfig = arenaStorage.getConfig();
	}

	public String getName() {
		return "none";
	}

	public void sendScore(CommandSender sender) {
		Util.msg(sender, "Score:");
		for (ArenaTeam team : arena.getTeams()) {
			Util.msg(sender, "Team " + team.getTeamName() + ": " + team.getScore());
		}
	}
	
	public void sendScoreAll() {
		arenaUtil.sendMessageAll("Score:");
		for (ArenaTeam team : arena.getTeams()) {
			arenaUtil.sendMessageAll("Team " + team.getTeamName() + ": " + team.getScore());
		}
	}

	public void onPlayerDeath(PlayerDeathEvent event, ArenaPlayer arenaPlayer) {
		event.setDroppedExp(0);
		event.setKeepLevel(true);
		event.getDrops().clear();
		
		String deathMessage = event.getDeathMessage();
		event.setDeathMessage(null);

		if(arena.getArenaState() == ArenaState.PLAYING && arenaPlayer.getPlayerState() == ArenaPlayerState.PLAYING){
			arenaUtil.sendNoPrefixMessageAll(deathMessage);
			
			Player player = event.getEntity();

			// Add death and kill to players.
			arenaPlayer.getPlayerScore().addDeath();
			if (player.getKiller() != null)
				arenaManager.getPlayerByName(player.getKiller().getName()).getPlayerScore().addKill();
		}
	}

	public void onPlayerRespawn(PlayerRespawnEvent event, ArenaPlayer arenaPlayer) {
		Player player = event.getPlayer();
		if (arena.getArenaState() == ArenaState.PLAYING && arenaPlayer.getPlayerState() != ArenaPlayerState.OUTOFGAME) {
			if (arena.getSettings().getRespawnTime() == 0) {
				event.setRespawnLocation(arenaSpawns.getRespawnLocation(player, arenaPlayer, SpawnType.PLAYER));
			} else {
				event.setRespawnLocation(arenaSpawns.getRespawnLocation(player, arenaPlayer, SpawnType.SPECTATOR));
				arenaPlayer.getTimers().startRespawnTimer(player, SpawnType.PLAYER);
			}
		} else {
			event.setRespawnLocation(arenaSpawns.getRespawnLocation(player, arenaPlayer, SpawnType.SPECTATOR));
		}
	}

	public void onPlayerDamage(EntityDamageByEntityEvent event, ArenaPlayer arenaPlayer) {
		Entity damager = event.getDamager();
		if (damager instanceof Projectile) {
			damager = ((Projectile) damager).getShooter();
		}

		//All mob version Player damage is fine, check if damager is a player.
		if (damager instanceof Player) {
			//Block damage if arena or player isn't playing.
			if (arena.getArenaState() != ArenaState.PLAYING || arenaPlayer.getPlayerState() != ArenaPlayerState.PLAYING) {
				event.setCancelled(true);
			} else {
				ArenaPlayer damagePlayer = arenaManager.getPlayerByName(((Player) damager).getName());
				//Block damage if damager is not in same arena.
				if (damagePlayer == null || !damagePlayer.getArena().equals(arenaPlayer.getArena())) {
					event.setCancelled(true);
				} else {
					if (!settings.isFriendlyFire()) {
						//Block friendly fire
						if (damagePlayer.getTeam().equals(arenaPlayer.getTeam())) {
							event.setCancelled(true);
						}
					}

					//Block pvp spawn protection damage.
					if (arenaPlayer.getTimers().hasSpawnProtection()) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

	public void onPlayerMove(PlayerMoveEvent event, ArenaPlayer arenaPlayer) {
		if(settings.isOutOfBoundsArea()){
			arenaArea.handleMove(arenaPlayer, event.getPlayer(), event.getTo());
		}
	}

	public void onPlayerQuit(PlayerQuitEvent event, ArenaPlayer arenaPlayer) {
		//TODO Create EventPlayerLeaveArena
		Player player = Bukkit.getPlayer(arenaPlayer.getPlayerName());

		YamlStorage playerStorage = new YamlStorage(plugin, "players", player.getName());
		Configuration playerConfig = playerStorage.getConfig();

		Util.playerLeave(player, playerStorage);
		player.teleport(Util.getLocationFromString(playerConfig.getString("location")));
		playerConfig.set("location", null);

		arenaPlayer.getTimers().cancelAllTimers();
		arenaPlayer.getTeam().getPlayers().remove(arenaPlayer);
		arena.getOfflinePlayers().add(player.getName());
	}

	public void onPlayerJoin(PlayerJoinEvent event, final ArenaPlayer arenaPlayer) {
		final Player player = event.getPlayer();
		String playerName = player.getName();
		if (arena.getOfflinePlayers().contains(playerName)) {
			arena.getOfflinePlayers().remove(playerName);
			//Teleport first to avoid problems with MVInventories
			final YamlStorage playerStorage = new YamlStorage(plugin, "players", player.getName());
			Configuration playerConfig = playerStorage.getConfig();
			playerConfig.set("location", Util.getStringFromLocation(player.getLocation()));

			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					player.teleport(arena.getArenaSpawns().getRespawnLocation(player, arenaPlayer, SpawnType.SPECTATOR));
					Util.playerJoin(player, playerStorage);
					arenaPlayer.getTeam().joinTeam(player, arenaManager, arena, arenaPlayer);
				}
			}, 1);
		}
	}

	public void onPlayerDropItem(PlayerDropItemEvent event, ArenaPlayer arenaPlayer) {
		if(!settings.isAllowItemDrop()){
			Util.msg(event.getPlayer(), "You may not drop items!");
			event.setCancelled(true);
		}		
	}

	public void onPlayerInventoryClick(InventoryClickEvent event, ArenaPlayer arenaPlayer) {
		if(!settings.isAllowItemDrop()){
			if (event.getSlotType() == SlotType.ARMOR && event.getCurrentItem() != null) {
				Util.msg((Player) event.getWhoClicked(), "You may not take off your armor!");
				event.setCancelled(true);
			}
		}
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
			}
			return;
		}
		event.setCancelled(true);
	}

	public void onPlayerLeaveArena(EventLeaveArena event) {

	}

	public void onPlayerArenaRespawn(final EventRespawn event) {
		if (event.getSpawnType() == SpawnType.PLAYER) {
			event.getArenaPlayer().getTimers().giveSpawnProtection();

			ArenaTeam team = event.getArenaPlayer().getTeam();
			PlayerInventory inventory = event.getPlayer().getInventory();

			inventory.setContents(team.getInventory());
			inventory.setArmorContents(team.getArmor());
		}
	}

	public AbstractCommand executeCommand(String command) {
		return null;
	}
}
