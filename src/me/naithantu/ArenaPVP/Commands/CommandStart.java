package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Arena.ArenaExtras.ArenaState;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class CommandStart extends AbstractArenaCommand {

	protected CommandStart(CommandSender sender, String[] args) {
		super(sender, args);
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
		arena.getArenaGameController().startGame();
		this.msg(sender, "You have started arena " + arena.getArenaName() + "!");
	}
}
