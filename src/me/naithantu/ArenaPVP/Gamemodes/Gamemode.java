package me.naithantu.ArenaPVP.Gamemodes;

import me.naithantu.ArenaPVP.Arena.Settings.SettingMenu;
import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.IconMenu;
import me.naithantu.ArenaPVP.TabController;
import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaArea;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaChat;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaPlayerState;
import me.naithantu.ArenaPVP.Arena.Settings.ArenaSettings;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSpawns;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaSpectators;
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
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.PlayerInventory;
import org.mcsg.double0negative.tabapi.TabAPI;

public abstract class Gamemode {
	protected ArenaPVP plugin;
	protected ArenaManager arenaManager;
	protected Arena arena;
	protected ArenaSettings settings;
	protected ArenaSpawns arenaSpawns;
	protected ArenaArea arenaArea;
	protected ArenaSpectators arenaSpectators;
	protected ArenaUtil arenaUtil;
	protected ArenaChat arenaChat;
	protected YamlStorage arenaStorage;
	protected Configuration arenaConfig;
	protected TabController tabController;
	protected Gamemodes gm;

	public Gamemode(ArenaPVP plugin, ArenaManager arenaManager, Arena arena, ArenaSettings settings, ArenaSpawns arenaSpawns, ArenaUtil arenaUtil, YamlStorage arenaStorage, TabController tabController, Gamemodes gm) {
		this.plugin = plugin;
		this.arenaManager = arenaManager;
		this.arena = arena;
		this.settings = settings;
		this.arenaSpawns = arenaSpawns;
		this.arenaUtil = arenaUtil;
		this.arenaStorage = arenaStorage;
		this.tabController = tabController;
		this.gm = gm;
		arenaSpectators = arena.getArenaSpectators();
		arenaChat = arena.getArenaChat();
		arenaArea = arena.getArenaArea();
		arenaConfig = arenaStorage.getConfig();
		createComp();
	}

	public abstract String getName();
	
	public abstract boolean isTeamGame();

    //IconMenu related methods.
    public boolean hasConfigSettings(){
        return false;
    }

    public void setupIconMenu(IconMenu iconMenu){}

    public void handleMenuClick(IconMenu.OptionClickEvent event){}

    public void stopChanging(){};
	
	public Gamemodes getGamemode(){
		return gm;
	}

	public void sendScore(CommandSender sender) {
		Util.msg(sender, "Score:");
		for (ArenaTeam team : arena.getTeams()) {
			Util.msg(sender, "Team " + team.getColoredName() + ": " + team.getScore());
		}
	}

	public void sendScoreAll() {
		arenaUtil.sendMessageAll("Score:");
		for (ArenaTeam team : arena.getTeams()) {
			arenaUtil.sendMessageAll("Team " + team.getColoredName() + ": " + team.getScore());
		}
	}

	public void onPlayerDeath(PlayerDeathEvent event, ArenaPlayer arenaPlayer) {
		arenaPlayer.getTimers().cancelAllTimers();
		event.setDroppedExp(0);
		event.setKeepLevel(true);
		event.getDrops().clear();

		Player player = event.getEntity();

		if (arena.getArenaState() == ArenaState.PLAYING && arenaPlayer.getPlayerState() == ArenaPlayerState.PLAYING) {
            if(event.getDeathMessage() != null)
                arenaUtil.sendNoPrefixMessageAll(event.getDeathMessage());

            // Add death and kill to players.
			arenaPlayer.getPlayerScore().addDeath();
			if (player.getKiller() != null)
				arenaManager.getPlayerByName(player.getKiller().getName()).getPlayerScore().addKill();
		}
        event.setDeathMessage(null);
    }

	public void onPlayerRespawn(PlayerRespawnEvent event, ArenaPlayer arenaPlayer) {
		Player player = event.getPlayer();
		if (arena.getArenaState() == ArenaState.PLAYING && arenaPlayer.getPlayerState() != ArenaPlayerState.SPECTATING) {
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

		if (arenaSpectators.getSpectators().containsKey(event.getEntity())) {
			//If a spectator was hit, check for projectile damage.
			Player spectator = (Player) event.getEntity();
			if (event.getDamager() instanceof Projectile) {
				spectator.teleport(spectator.getLocation().add(0, 5, 0));
				Util.msg(spectator, "You were in the way of a projectile!");
				event.setCancelled(true);

				Projectile damageProjectile = (Projectile) event.getDamager();
				Location damagerLocation = damageProjectile.getLocation();
				if (!(damageProjectile instanceof Fish)) {
					//Hit by any non fishing rod projectile.
					Projectile newProjectile = damagerLocation.getWorld().spawn(damagerLocation, damageProjectile.getClass());
					newProjectile.setVelocity(damageProjectile.getVelocity());
					newProjectile.setBounce(damageProjectile.doesBounce());
					newProjectile.setShooter(damageProjectile.getShooter());
					damageProjectile.remove();
				}
			}
		} else {
			//All mob versus Player damage is fine, check if damager is a player.
			if (damager instanceof Player) {
				//Block damage if arena or player isn't playing.
				if (arena.getArenaState() != ArenaState.PLAYING || arenaPlayer.getPlayerState() != ArenaPlayerState.PLAYING) {
					event.setCancelled(true);
				} else {
					ArenaPlayer damagePlayer = arenaManager.getPlayerByName(((Player) damager).getName());
					//Block damage if damager is not in same arena.
					if (damagePlayer == null || !damagePlayer.getArena().equals(arenaPlayer.getArena()) || damagePlayer.getPlayerState() != ArenaPlayerState.PLAYING) {
						event.setCancelled(true);
					} else {
						if (!settings.isFriendlyFire() && isTeamGame()) {
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
	}

	public void onPlayerMove(PlayerMoveEvent event, ArenaPlayer arenaPlayer) {
        if(arena.getArenaState() == ArenaState.STARTING){
            //Check if player moved x or z (jumping/falling is fine).
            if (event.getTo().getX() != event.getFrom().getX() || event.getTo().getZ() != event.getFrom().getZ()) {
                Block target = event.getFrom().getBlock();
                while(target.getRelative(BlockFace.DOWN).isEmpty()) {
                    if(target.getRelative(BlockFace.DOWN).getY() == 0){
                        event.setTo(arenaSpawns.getRespawnLocation(event.getPlayer(), arenaPlayer, SpawnType.SPECTATOR));
                        return;
                    }
                    target = target.getRelative(BlockFace.DOWN);
                }
                Location targetLocation = target.getLocation();
                targetLocation.setPitch(event.getTo().getPitch());
                targetLocation.setYaw(event.getTo().getYaw());
                targetLocation.setX(event.getFrom().getX());
                targetLocation.setZ(event.getFrom().getZ());
                event.setTo(targetLocation);
            }
        } else {
            if(arenaPlayer.getPlayerState() == ArenaPlayerState.SPECTATING){
                if (settings.isSpectatorOutOfBoundsArea() && !event.getPlayer().isDead()){
                    checkMove(event, arenaPlayer);
                }
            } else {
                if (settings.isOutOfBoundsArea() && !event.getPlayer().isDead()) {
                    checkMove(event, arenaPlayer);
                }
            }
        }
	}

    private void checkMove(PlayerMoveEvent event, ArenaPlayer arenaPlayer){
        //Check if player actually moved to prevent unnecessary checking.
        if (event.getTo().getX() != event.getFrom().getX() || event.getTo().getZ() != event.getFrom().getZ() || event.getTo().getY() != event.getFrom().getY()) {
            arenaArea.handleMove(event, arenaPlayer);
        }
    }

	public void onPlayerQuit(PlayerQuitEvent event, ArenaPlayer arenaPlayer) {
		Player player = Bukkit.getPlayer(arenaPlayer.getPlayerName());

		arenaPlayer.getTimers().cancelAllTimers();
		if (arenaPlayer.getTeam() != null) {
            arena.leaveGame(arenaPlayer);
		} else {
			//Player was spectator, leave as spectator.
			arena.leaveSpectate(player, arenaPlayer);
		}
		clearTab(player);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				updateTabs();
			}
		}, 1);
	}

	public void onPlayerDropItem(PlayerDropItemEvent event, ArenaPlayer arenaPlayer) {
		if (arenaPlayer.getPlayerState() == ArenaPlayerState.SPECTATING || !settings.isAllowItemDrop()) {
			Util.msg(event.getPlayer(), "You may not drop items!");
			event.setCancelled(true);
		}
	}

	public void onPlayerPickupItem(PlayerPickupItemEvent event, ArenaPlayer arenaPlayer) {
		if (arenaPlayer.getPlayerState() != ArenaPlayerState.PLAYING)
			event.setCancelled(true);
	}

	public void onPlayerInventoryClick(InventoryClickEvent event, ArenaPlayer arenaPlayer) {
		if (arenaPlayer.getPlayerState() == ArenaPlayerState.SPECTATING || !settings.isAllowItemDrop()) {
			if (event.getSlotType() == SlotType.ARMOR && event.getCurrentItem() != null) {
				Util.msg((Player) event.getWhoClicked(), "You may not take off your armor!");
				event.setCancelled(true);
			}
		}
	}

	public void onPlayerChat(AsyncPlayerChatEvent event, ArenaPlayer arenaPlayer) {
		arenaChat.onPlayerChatEvent(event, arenaPlayer);
	}

	public void onPlayerPlaceBlock(BlockPlaceEvent event, ArenaPlayer arenaPlayer) {
		if (arenaPlayer.getPlayerState() != ArenaPlayerState.PLAYING) {
			event.setCancelled(true);
		} else {
			if (!arena.getSettings().isAllowBlockChange()) {
				event.setCancelled(true);
			}
		}
	}

	public void onPlayerBreakBlock(BlockBreakEvent event, ArenaPlayer arenaPlayer) {
		if (arenaPlayer.getPlayerState() != ArenaPlayerState.PLAYING || arena.getArenaState() != ArenaState.PLAYING) {
			event.setCancelled(true);
		} else {
			if (!arena.getSettings().isAllowBlockChange()) {
				event.setCancelled(true);
			}
		}
	}

	public void onPlayerInteractBlock(PlayerInteractEvent event, ArenaPlayer arenaPlayer) {
		if (arenaPlayer.getPlayerState() != ArenaPlayerState.PLAYING)
			event.setCancelled(true);
	}

    public void onProjectileHit(ProjectileHitEvent event, ArenaPlayer arenaPlayer) {
    }

    public void onRedstoneUpdate(BlockRedstoneEvent event) {
    }

    public void onFoodLevelChange(FoodLevelChangeEvent event){
        if(settings.isNoHungerLoss()){
            event.setCancelled(true);
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
			updateTabs();
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					updateTabs();
				}
			}, 1);
			return;
		}
		event.setCancelled(true);
	}

	public void onPlayerLeaveArena(final EventLeaveArena event) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                Player p = Bukkit.getPlayerExact(event.getArenaPlayer().getPlayerName());
                if (p != null) {
                    clearTab(p);
                }
                updateTabs();
            }
        }, 1);
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

    public void onArenaStart(){}

    public void onArenaStop(){}

	public AbstractCommand handleGamemodeCommand(CommandSender sender, String command, String[] cmdArgs) {
		return null;
	}

	public abstract void sortLists();

	public abstract void updateTabs();

	protected abstract void createComp();

	public void clearTab(Player p) {
		if (!tabController.hasTabAPI())
			return;
		try {
			TabAPI.clearTab(p);
			TabAPI.setPriority(plugin, p, -2);
			TabAPI.updatePlayer(p);
		} catch (NullPointerException ex) {
		}
	}

    public enum Gamemodes {
		FFA, TDM, CTF, LMS, LTS, PAINTBALL, OITC, REDSTONE
	}
}
