package me.naithantu.ArenaPVP.Util;

import java.util.List;

import me.naithantu.ArenaPVP.Storage.YamlStorage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Util {
	public static void broadcast(String msg){
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
		return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
	}

	@SuppressWarnings("unchecked")
	public static void playerLeave(Player player, YamlStorage playerStorage){
		Configuration playerConfig = playerStorage.getConfig();
		//Clear players inventory and then load saved inventory.
		PlayerInventory inventory = player.getInventory();
		inventory.clear();
		inventory.setArmorContents(new ItemStack[4]);
		
		List<ItemStack> inventoryContents = (List<ItemStack>) playerConfig.getList("inventory");
		List<ItemStack> armorContents = (List<ItemStack>) playerConfig.getList("armor");

		player.setLevel(playerConfig.getInt("level"));
		player.setExp((float) playerConfig.getDouble("exp"));
		
		inventory.setContents(inventoryContents.toArray(new ItemStack[36]));
		inventory.setArmorContents(armorContents.toArray(new ItemStack[4]));
				
		playerConfig.set("inventory", null);
		playerConfig.set("armor", null);
		playerConfig.set("level", null);
		playerConfig.set("exp", null);
		playerConfig.set("hastoleave", null);
		playerStorage.saveConfig();
	}
	
	public static void playerJoin(Player player, YamlStorage playerStorage){
		Configuration playerConfig = playerStorage.getConfig();
		
		//Save players inventory and then clear it.
		PlayerInventory inventory = player.getInventory();

		
		playerConfig.set("inventory", inventory.getContents());
		playerConfig.set("armor", inventory.getArmorContents());
		playerConfig.set("level", player.getLevel());
		playerConfig.set("exp", player.getExp());
		playerStorage.saveConfig();
		
		inventory.clear();
		player.setLevel(0);
		player.setExp(0);
		inventory.setArmorContents(new ItemStack[4]);
	}
}
