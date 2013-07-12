package me.naithantu.ArenaPVP.Storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import me.naithantu.ArenaPVP.ArenaPVP;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class YamlStorage {
	ArenaPVP plugin;
	String fileName;
	File file;
	String path;
	FileConfiguration config;
	
	public YamlStorage(ArenaPVP plugin, String path, String fileName){
		this.plugin = plugin;
		this.path = path;
		this.fileName = fileName + ".yml";
		getConfig();
	}

	public void reloadConfig() {
		if (file == null) {
			file = new File(plugin.getDataFolder() + File.separator + path, fileName);
		}
		config = YamlConfiguration.loadConfiguration(file);

		// Look for defaults in the jar
		InputStream defConfigStream = plugin.getResource(fileName);
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
	}
	
	public FileConfiguration getConfig() {
		if (config == null) {
			this.reloadConfig();
		}
		return config;
	}

	public void saveConfig() {
		if (config == null || file == null) {
			return;
		}
		try {
			getConfig().save(file);
		} catch (IOException ex) {
			plugin.getLogger().log(Level.SEVERE, "Could not save config to " + config, ex);
		}
	}

	public void saveDefaultConfig() {
		if (file == null) {
			file = new File(plugin.getDataFolder(), fileName);
		}
		if (!file.exists()) {
			this.plugin.saveResource(fileName, false);
		}
	}
}
