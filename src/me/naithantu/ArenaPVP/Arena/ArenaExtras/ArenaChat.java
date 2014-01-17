package me.naithantu.ArenaPVP.Arena.ArenaExtras;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer.ChatChannel;
import me.naithantu.ArenaPVP.Arena.ArenaTeam;
import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;

public class ArenaChat {
	private Arena arena;

	private List<String> tempNoneChannel = new ArrayList<String>();

	public ArenaChat(Arena arena) {
		this.arena = arena;
	}

	public void onPlayerChat(Player player, ArenaPlayer arenaPlayer, String message, ChatChannel chatChannel) {
		ArenaTeam team = arenaPlayer.getTeam();
		ChatColor teamColor;
		if (team != null) {
			teamColor = team.getTeamColor();
		} else {
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

        if(chatChannel != ChatChannel.NONE){
            sendChatSpy(player, message, chatChannel);
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

    private void sendChatSpy(Player player, String message, ChatChannel chatChannel){
        String chatSpyMessage;

        if (chatChannel == ChatChannel.TEAM) {
            chatSpyMessage = ChatColor.GRAY + "[ArenaPVP] [TEAM] " + player.getName() + ": " + message;
        } else {
            chatSpyMessage = ChatColor.GRAY + "[ArenaPVP] [ALL] " + player.getName() + ": " + message;
        }

        for(Player chatSpyPlayer: Bukkit.getOnlinePlayers()){
            if(chatSpyPlayer.hasPermission("arenapvp.mod") || player.hasPermission("arenapvp.chatspy")){
                YamlStorage playerStorage = new YamlStorage("players", chatSpyPlayer.getName());
                FileConfiguration playerConfig = playerStorage.getConfig();
                //Check if player has chatspy enabled.
                if(playerConfig.getBoolean("chatspy")){
                    //Check if player is not in a game.
                    if(ArenaManager.getPlayerByName(chatSpyPlayer.getName()) == null){
                        chatSpyPlayer.sendMessage(chatSpyMessage);
                    }
                }
            }
        }
    }
}
