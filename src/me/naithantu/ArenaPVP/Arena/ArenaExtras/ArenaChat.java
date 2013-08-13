package me.naithantu.ArenaPVP.Arena.ArenaExtras;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer.ChatChannel;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;

public class ArenaChat {
	Arena arena;

	List<String> tempNoneChannel = new ArrayList<String>();

	public ArenaChat(Arena arena) {
		this.arena = arena;
	}

	public void onPlayerChat(Player player, ArenaPlayer arenaPlayer, String message, ChatChannel chatChannel) {
		ArenaTeam team = arenaPlayer.getTeam();
		String teamName;
		ChatColor teamColor;
		if (team != null) {
			teamName = team.getTeamName();
			teamColor = team.getTeamColor();
		} else {
			teamName = "Spectator";
			teamColor = ChatColor.GRAY;
		}

		if (chatChannel == ChatChannel.TEAM) {
			arena.getArenatUtil().sendNoPrefixMessageTeam(ChatColor.GRAY + "[TEAM] " + teamColor + player.getName() + ChatColor.WHITE + ": " + message, team);
		} else if (chatChannel == ChatChannel.ALL) {
			arena.getArenatUtil().sendNoPrefixMessageAll(ChatColor.GRAY + "[ALL] " + teamColor + player.getName() + ChatColor.WHITE + ": " + message);
		} else {
			tempNoneChannel.add(player.getName());
			player.chat(message);
		}
	}

	public void onPlayerChatEvent(AsyncPlayerChatEvent event, ArenaPlayer arenaPlayer) {
		if (tempNoneChannel.contains(event.getPlayer().getName())) {
			tempNoneChannel.remove(event.getPlayer().getName());
		} else if (arenaPlayer.getChatChannel() != ChatChannel.NONE) {
			event.setCancelled(true);
			onPlayerChat(event.getPlayer(), arenaPlayer, event.getMessage(), arenaPlayer.getChatChannel());
		}
	}
}
