package me.naithantu.ArenaPVP.Util;

import me.naithantu.ArenaPVP.Storage.YamlStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Util {

    private Util(){
    }

	public static void broadcast(String msg) {
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "[PVP] " + ChatColor.WHITE + msg);
	}

	public static void msg(CommandSender sender, String msg) {
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		if (sender instanceof Player) {
			sender.sendMessage(ChatColor.DARK_RED + "[PVP] " + ChatColor.WHITE + msg);
		} else {
			sender.sendMessage("[PVP] " + msg);
		}
	}
	
	public static String capitalizeFirstLetter(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

    public static Location getLocation(YamlStorage storage, String key) {
        FileConfiguration config = storage.getConfig();
        String worldName = config.getString(key + ".world");
        World world = Bukkit.getWorld(worldName);
        double x = config.getDouble(key + ".x");
        double y = config.getDouble(key + ".y");
        double z = config.getDouble(key + ".z");
        float yaw = (float) config.getDouble(key + ".yaw");
        float pitch = (float) config.getDouble(key + ".pitch");
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static void saveLocation(Location location, YamlStorage storage, String key) {
        saveLocation(location, storage, key, true);
    }

    public static void saveLocation(Location location, YamlStorage storage, String key, boolean round) {
        if (round)
            location = roundLocation(location);
        FileConfiguration config = storage.getConfig();
        config.set(key + ".world", location.getWorld().getName());
        config.set(key + ".x", location.getX());
        config.set(key + ".y", location.getY());
        config.set(key + ".z", location.getZ());
        config.set(key + ".yaw", location.getYaw());
        config.set(key + ".pitch", location.getPitch());
        storage.saveConfig();
    }

    public static Location roundLocation(Location location) {
        location.setX(0.5 * Math.round(location.getX() / 0.5));
        location.setY(0.5 * Math.round(location.getY() / 0.5));
        location.setZ(0.5 * Math.round(location.getZ() / 0.5));
        location.setYaw(45 * (Math.round(location.getYaw() / 45)));
        location.setPitch(45 * (Math.round(location.getPitch() / 45)));
        return location;
    }
}
