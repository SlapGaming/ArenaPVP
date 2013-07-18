package me.naithantu.ArenaPVP.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Util {
	public static void msg(CommandSender sender, String msg) {
		if (sender instanceof Player) {
			sender.sendMessage(ChatColor.DARK_RED + "[PVP] " + ChatColor.WHITE + msg);
		} else {
			sender.sendMessage("[PVP] " + msg);
		}
	}
	
	public static Location getLocationFromString(String string){
		String[] stringSplit = string.split(":");
		World world = Bukkit.getServer().getWorld(stringSplit[0]);
		Double x = Double.valueOf(stringSplit[1]);
		Double y = Double.valueOf(stringSplit[2]);
		Double z = Double.valueOf(stringSplit[3]);
		Float yaw = Float.valueOf(stringSplit[4]);
		Float pitch = Float.valueOf(stringSplit[5]);
		Location location = new Location(world, x, y, z, yaw, pitch);
		return location;
	}
	
	public static String getStringFromLocation(Location location){
		return location.getWorld() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
	}
}
