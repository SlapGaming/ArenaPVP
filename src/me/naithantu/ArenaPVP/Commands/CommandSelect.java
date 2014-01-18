package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.ArenaManager;
import org.bukkit.command.CommandSender;

import java.io.File;

public class CommandSelect extends AbstractCommand {

	protected CommandSelect(CommandSender sender, String[] args) {
		super(sender, args);
	}

	@Override
    protected boolean handle() {
		if(!testPermission(sender, "select") && !testPermission(sender, "mod")){
			this.noPermission(sender);
			return true;
		}
		
		if(args.length == 0){
			this.msg(sender, "Usage: /pvp select [arenaname]");
			return true;
		}
		
		String arenaName = args[0].toLowerCase();
		
		if(ArenaManager.getArenas().containsKey(arenaName)){
			this.msg(sender, "That arena is already loaded! You can unload it with /pvp stop [arenaname].");
			return true;
		}

        File file = new File(plugin.getDataFolder() + File.separator + "maps", arenaName + ".yml");
        if(!file.exists()){
            this.msg(sender, "That arena does not exist, use /pvp maps to see available arenas!");
            return true;
        }

		Arena arena = new Arena(arenaName);
		ArenaManager.addArena(arenaName, arena);
		
		this.msg(sender, "Loaded arena with name " + arena.getArenaName() + "!");
		return true;
	}
}
