package me.naithantu.ArenaPVP.Commands;

import com.google.common.base.Joiner;
import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class CommandSpectators extends AbstractArenaCommand {

	protected CommandSpectators(CommandSender sender, String[] args) {
		super(sender, args);
	}

    @Override
    protected boolean handle() {
		if (!testPermission(sender, "spectators") && !testPermission(sender, "player")) {
			this.noPermission(sender);
			return true;
        }

        this.executeCommand(getArenas());
		return true;
	}

    @Override
    protected Collection<Arena> getArenas() {
        return this.selectArena(args, ArenaState.WARMUP, ArenaState.PLAYING, ArenaState.STARTING, ArenaState.LOBBY, ArenaState.FINISHED);
    }

    @Override
    protected void runCommand(Arena arena) {
        this.msg(sender, "Showing spectators of arena " + arena.getNickName());
        this.msg(sender, ChatColor.GRAY + "Spectators: " + ChatColor.WHITE + Joiner.on(", ").join(arena.getArenaSpectators().getSpectators().keySet()));
    }
}
