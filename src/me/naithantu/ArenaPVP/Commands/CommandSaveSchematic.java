package me.naithantu.ArenaPVP.Commands;

import com.sk89q.jnbt.NBTOutputStream;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.io.SchematicWriter;
import com.sk89q.worldedit.session.ClipboardHolder;
import me.naithantu.ArenaPVP.Arena.Arena;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.Util.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

public class CommandSaveSchematic extends AbstractArenaCommand {

	protected CommandSaveSchematic(CommandSender sender, String[] args) {
		super(sender, args);
	}

    @Override
    protected boolean handle() {
		if (!testPermission(sender, "saveinventory") && !testPermission(sender, "admin")) {
			this.noPermission(sender);
			return true;
		}

		if (!(sender instanceof Player)) {
			this.msg(sender, "That command can only be used in-game.");
			return true;
		}

        this.executeCommand(getArenas());
		return true;
	}

    @Override
    protected Collection<Arena> getArenas() {
        return this.selectArena(args);
    }

    @Override
    protected void runCommand(Arena arena) {
        Player player = (Player) sender;

        try {
            ClipboardHolder clipBoard = WorldEdit.getInstance().getSessionManager().findByName(player.getName()).getClipboard();
            File file = new File(plugin.getDataFolder() + File.separator + "maps", arena.getArenaName() + ".schematic");
            new SchematicWriter(new NBTOutputStream(new FileOutputStream(file))).write(clipBoard.getClipboard(), clipBoard.getWorldData());

            YamlStorage arenaStorage = arena.getArenaStorage();
            Configuration arenaConfig = arenaStorage.getConfig();
            arenaConfig.set("schematicworld", player.getWorld().getName());
            arenaStorage.saveConfig();

            Util.msg(player, "Saved schematic!");
        } catch (EmptyClipboardException e) {
            Util.msg(player, "Your clipboard is empty!");
        } catch (IOException e) {
            e.printStackTrace();
            Util.msg(player, "Failed to save Schematic. See logs.");
        }
    }
}
