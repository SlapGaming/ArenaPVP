package me.naithantu.ArenaPVP.Commands;

import java.io.File;
import java.io.IOException;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Objects.Arena;
import me.naithantu.ArenaPVP.Objects.ArenaManager;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.Util.Util;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;

public class CommandSaveSchematic extends AbstractCommand {

	protected CommandSaveSchematic(CommandSender sender, String[] args, ArenaPVP plugin, ArenaManager arenaManager) {
		super(sender, args, plugin, arenaManager);
	}

	@Override
	public boolean handle() {
		if (!testPermission(sender, "saveinventory") && !testPermission(sender, "admin")) {
			this.noPermission(sender);
			return true;
		}

		if (!(sender instanceof Player)) {
			this.msg(sender, "That command can only be used in-game.");
			return true;
		}

		Player player = (Player) sender;

		if (arenaManager.getArenas().size() == 0) {
			this.msg(sender, "There are currently no arenas that you can change.");
			return true;
		}

		if (arenaManager.getArenas().size() > 1) {
			// If there are several arenas, find out what arena players want to join.
			if (args.length < 2) {
				this.msg(sender, "There are currently several arenas to change, please specify the arena you want to change.");
				this.msg(sender, "/pvp saveschematic <arenaname>");
				return true;
			}

			if (arenaManager.getArenas().containsKey(args[0])) {
				Arena arena = arenaManager.getArenas().get(args[0]);
				saveSchematic(arena, player);
			} else {
				this.msg(sender, "No arena with given name was found, type /pvp arenas to see available arenas.");
			}
			return true;
		} else {
			//If there is only arena
			Arena arena = arenaManager.getFirstArena();
			saveSchematic(arena, player);
		}
		return true;
	}

	public void saveSchematic(Arena arena, Player player) {
		try {
			CuboidClipboard clipBoard = WorldEdit.getInstance().getSession(player.getName()).getClipboard();
			SchematicFormat format = SchematicFormat.getFormat("MCEdit");
			File file = new File(plugin.getDataFolder() + File.separator + "maps", arena.getArenaName() + ".schematic");
			format.save(clipBoard, file);
			
			YamlStorage arenaStorage = arena.getArenaStorage();
			Configuration arenaConfig = arenaStorage.getConfig();
			arenaConfig.set("schematicworld", player.getWorld().getName());
			arenaStorage.saveConfig();
			
			Util.msg(player, "Saved schematic!");
		} catch (EmptyClipboardException e) {
			Util.msg(player, "Your clipboard is empty!");
		} catch (IOException | DataException e) {
			e.printStackTrace();
		}
	}
}
