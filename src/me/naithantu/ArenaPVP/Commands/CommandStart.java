package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import me.naithantu.ArenaPVP.ArenaManager;
import me.naithantu.ArenaPVP.ArenaPVP;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class CommandStart extends AbstractArenaCommand {

	protected CommandStart(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
    protected boolean handle() {
		if (!testPermission(sender, "start") && !testPermission(sender, "mod")) {
			this.noPermission(sender);
			return true;
		}

        this.executeCommand(getArenas());
		return true;
	}

    @Override
    protected Collection<Arena> getArenas() {
        return this.selectArena(args, ArenaState.LOBBY, ArenaState.WARMUP);
    }

    @Override
    protected void runCommand(Arena arena) {
		arena.startGame();
		this.msg(sender, "You have started arena " + arena.getArenaName() + "!");
	}
}
