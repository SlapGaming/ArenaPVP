package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.Arena.Arena;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

public class CommandChange extends AbstractArenaCommand {

	protected CommandChange(CommandSender sender, String[] args) {
		super(sender, args);
	}

	@Override
    protected boolean handle() {
		if (!testPermission(sender, "change") && !testPermission(sender, "mod")) {
			this.noPermission(sender);
			return true;
		}

        if(!(sender instanceof Player)){
            this.badMsg(sender, "That command can only be used in-game!");
            return true;
        }

        this.executeCommand(getArenas());
        return true;
	}

    @Override
    protected Collection<Arena> getArenas(){
        return this.selectArena(args);
    }

    @Override
    protected void runCommand(Arena arena) {
        Player player = (Player) sender;
        arena.getSettings().getSettingMenu().openMenu(player);
        this.msg(player, "Opened main setting menu!");
    }
}
