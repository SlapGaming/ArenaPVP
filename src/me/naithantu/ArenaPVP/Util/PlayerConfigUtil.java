package me.naithantu.ArenaPVP.Util;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

public class PlayerConfigUtil {

    private PlayerConfigUtil() {
    }

    private static boolean checkConfigIntegrity(FileConfiguration playerConfig) {
        //Check if playerConfig was properly saved.
        return playerConfig.contains("saved.gamemode");
    }

    @SuppressWarnings("unchecked")
    private static void loadPlayerSaves(Player player, FileConfiguration playerConfig) {
        //Clear players inventory and then load saved inventory.
        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setArmorContents(new ItemStack[4]);

        List<ItemStack> inventoryContents = (List<ItemStack>) playerConfig.getList("saved.inventory");
        List<ItemStack> armorContents = (List<ItemStack>) playerConfig.getList("saved.armor");

        player.setLevel(playerConfig.getInt("saved.level"));
        player.setExp((float) playerConfig.getDouble("saved.exp"));
        player.setHealth(playerConfig.getDouble("saved.health"));
        player.setFoodLevel(playerConfig.getInt("saved.hunger"));
        player.setGameMode(GameMode.valueOf(playerConfig.getString("saved.gamemode")));
        if (playerConfig.getBoolean("saved.flying") || GameMode.valueOf(playerConfig.getString("saved.gamemode")) == GameMode.CREATIVE) {
            player.setAllowFlight(true);
            player.setFlying(true);
        } else {
            player.setAllowFlight(false);
        }
        player.addPotionEffects((Collection<PotionEffect>) playerConfig.getList("saved.potioneffects"));

        inventory.setContents(inventoryContents.toArray(new ItemStack[36]));
        inventory.setArmorContents(armorContents.toArray(new ItemStack[4]));
    }

    public static void loadPlayerConfig(Player player, YamlStorage playerStorage, PlayerRespawnEvent event) {
        FileConfiguration playerConfig = playerStorage.getConfig();
        if (checkConfigIntegrity(playerConfig)) {
            loadPlayerSaves(player, playerConfig);

            //Teleport player back
            event.setRespawnLocation(Util.getLocation(playerStorage, "saved.location"));

            //Remove all saved info.
            playerConfig.set("saved", null);
            playerStorage.saveConfig();
        }
    }

    public static void loadPlayerConfig(Player player, YamlStorage playerStorage) {
        FileConfiguration playerConfig = playerStorage.getConfig();
        if (checkConfigIntegrity(playerConfig)) {
            loadPlayerSaves(player, playerConfig);

            //Teleport player back
            player.teleport(Util.getLocation(playerStorage, "saved.location"));

            //Remove all saved info.
            playerConfig.set("saved", null);
            playerStorage.saveConfig();
        } else {
            ArenaPVP.getInstance().getLogger().log(Level.SEVERE, "Was unable to load config for player " + player.getName() + "!");
        }
    }

    public static void savePlayerConfig(Player player, YamlStorage playerStorage, Location teleportLocation) {
        Configuration playerConfig = playerStorage.getConfig();

        //Make sure no previously saved info is in the config.
        playerConfig.set("saved", null);

        Util.saveLocation(player.getLocation(), playerStorage, "saved.location");
        player.teleport(teleportLocation);

        //Save players inventory and then clear it.
        PlayerInventory inventory = player.getInventory();

        playerConfig.set("saved.inventory", inventory.getContents());
        playerConfig.set("saved.armor", inventory.getArmorContents());
        playerConfig.set("saved.level", player.getLevel());
        playerConfig.set("saved.exp", player.getExp());
        playerConfig.set("saved.health", player.getHealth());
        playerConfig.set("saved.hunger", player.getFoodLevel());
        playerConfig.set("saved.gamemode", player.getGameMode().toString());
        playerConfig.set("saved.flying", player.isFlying());
        playerConfig.set("saved.potioneffects", player.getActivePotionEffects());
        playerStorage.saveConfig();

        inventory.clear();
        inventory.setArmorContents(new ItemStack[4]);
        player.setLevel(0);
        player.setExp(0);
        player.setVelocity(new Vector(0, 0, 0));
        player.setGameMode(GameMode.SURVIVAL);
        player.setFoodLevel(20);
        player.setHealth(20);
        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
    }
}
