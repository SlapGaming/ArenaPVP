package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.Storage.YamlStorage;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class CommandChatspy extends AbstractCommand {

	protected CommandChatspy(CommandSender sender, String[] args) {
		super(sender, args);
	}

	@Override
    protected boolean handle() {
		if(!testPermission(sender, "chatspy") && !testPermission(sender, "mod")){
			this.noPermission(sender);
			return true;
		}

        YamlStorage playerStorage = new YamlStorage("players", sender.getName());
        FileConfiguration playerConfig = playerStorage.getConfig();

        boolean chatSpy = playerConfig.getBoolean("chatspy");
        playerConfig.set("chatspy", !chatSpy);
        playerStorage.saveConfig();

        if(chatSpy){
            this.msg(sender, "Disabled ChatSpy.");
        } else {
            this.msg(sender, "Enabled ChatSpy.");
        }
        return true;
	}
}
