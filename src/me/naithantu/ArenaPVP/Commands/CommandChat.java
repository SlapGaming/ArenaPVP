package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.Arena.ArenaPlayer.ChatChannel;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Joiner;

public class CommandChat extends AbstractCommand {

	protected CommandChat(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
    protected boolean handle() {
		if (!testPermission(sender, "chat") && !testPermission(sender, "player")) {
			this.noPermission(sender);
			return true;
		}

		if (!(sender instanceof Player)) {
			this.msg(sender, "That command can only be used in-game.");
			return true;
		}

		ArenaPlayer arenaPlayer = arenaManager.getPlayerByName(sender.getName());
		if (arenaPlayer == null) {
			this.msg(sender, "You are not in a game!");
			return true;
		}

		if (args.length == 0) {
			this.msg(sender, "Usage: /pvp chat [channelname] <message>");
			return true;
		}

		ChatChannel chatChannel = checkChannel(args[0]);
		if (chatChannel == null) {
			this.msg(sender, "That chat channel does not exist!");
			return true;
		}

		if (args.length == 1) {
			arenaPlayer.setChatChannel(chatChannel);
			if (chatChannel != ChatChannel.NONE) {
				this.msg(sender, "You have joined the " + chatChannel.name().toLowerCase() + " channel!");
			} else {
				this.msg(sender, "You have left pvp chat and are now talking in normal chat!");
			}
		} else if (args.length > 1) {
			String message[] = new String[args.length - 1];
			for (int i = 0; i < args.length - 1; i++) {
				message[i] = args[i + 1];
			}
			arenaPlayer.getArena().getArenaChat().onPlayerChat((Player) sender, arenaPlayer, Joiner.on(' ').join(message), chatChannel);
		}
		return true;
	}

	private ChatChannel checkChannel(String channelName) {
		for (ChatChannel chatChannel : ChatChannel.values()) {
			if (chatChannel.name().equalsIgnoreCase(channelName)) {
				return chatChannel;
			}
		}
		return null;
	}
}
