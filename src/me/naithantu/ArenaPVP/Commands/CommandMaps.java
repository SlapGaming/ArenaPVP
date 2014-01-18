package me.naithantu.ArenaPVP.Commands;

import com.google.common.base.Joiner;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommandMaps extends AbstractCommand {

	protected CommandMaps(CommandSender sender, String[] args) {
		super(sender, args);
	}

	@Override
    protected boolean handle() {
		if (!testPermission(sender, "maps") && !testPermission(sender, "mod")) {
			this.noPermission(sender);
			return true;
		}

		List<String> mapNames = new ArrayList<String>();
		
		File mapsFolder = new File(plugin.getDataFolder() + File.separator + "maps");
		for (File map : mapsFolder.listFiles()) {
            String extension = "";

            int i = map.getName().lastIndexOf('.');
            if (i > 0) {
                extension = map.getName().substring(i+1);
            }
            
			if(extension.equalsIgnoreCase("yml")){
                mapNames.add(map.getName().replaceFirst(".yml", ""));
            }

		}
		
		this.msg(sender, "Available maps: " + Joiner.on(", ").join(mapNames));
		return true;
	}
}
