package me.naithantu.ArenaPVP.Arena;

import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaPlayerState;
import me.naithantu.ArenaPVP.Arena.PlayerExtras.PlayerScore;
import me.naithantu.ArenaPVP.Arena.PlayerExtras.PlayerTimers;
import me.naithantu.ArenaPVP.ArenaPVP;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.HashSet;

public class ArenaPlayer {
	private String playerName;
	private PlayerScore playerScore;
	private PlayerTimers timers;
	private Arena arena;
	private ArenaTeam team;
	private ArenaPlayerState playerState = ArenaPlayerState.PLAYING;
	private ChatChannel chatChannel;

    private boolean dying;

	public ArenaPlayer(ArenaPVP plugin, Player player, Arena arena, ArenaTeam team) {
		this.playerName = player.getName();
		this.arena = arena;
		this.team = team;
		this.playerScore = new PlayerScore(playerName);
		this.timers = new PlayerTimers(arena, this, player);
		this.chatChannel = ChatChannel.valueOf(arena.getArenaStorage().getConfig().getString("defaultchatchannel"));
	}

	public String getPlayerName() {
		return playerName;
	}

	public Arena getArena() {
		return arena;
	}

	public ArenaTeam getTeam() {
		return team;
	}

	public PlayerScore getPlayerScore() {
		return playerScore;
	}

	public ArenaPlayerState getPlayerState() {
		return playerState;
	}

	public void setPlayerState(ArenaPlayerState playerState) {
		this.playerState = playerState;
	}

	public ChatChannel getChatChannel() {
		return chatChannel;
	}

	public void setChatChannel(ChatChannel chatChannel) {
		this.chatChannel = chatChannel;
	}

	public PlayerTimers getTimers() {
		return timers;
	}

	public enum ChatChannel {
		TEAM, ALL, NONE
	}

    public void setDying(boolean dying){
        this.dying = dying;
    }

    public boolean isDying(){
        return dying;
    }

    /**
     * Reset a players params.
     * This includes health etc
     * @param p The player
     */
    public static void resetStats(Player p) {
        if (p.isDead()) return;
        //Wipe XP
        p.setExp(0);
        p.setLevel(0);

        //Health etc
        p.setHealth(20);
        p.setFoodLevel(20);

        //Wipe all potions
        for (PotionEffect potionEffect : new HashSet<>(p.getActivePotionEffects())) {
            p.removePotionEffect(potionEffect.getType());
        }

        //Leave/Remove Vehicles
        p.eject();
        p.leaveVehicle();

        //Set player stats
        p.setGameMode(GameMode.SURVIVAL);
        p.setFlying(false);
        p.setAllowFlight(false);
    }

}
