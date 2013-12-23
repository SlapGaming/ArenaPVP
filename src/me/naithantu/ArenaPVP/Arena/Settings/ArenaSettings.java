package me.naithantu.ArenaPVP.Arena.Settings;

import me.naithantu.ArenaPVP.ArenaPVP;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ArenaSettings {
    private YamlStorage arenaStorage;
    private	FileConfiguration config;

    private SettingMenu settingMenu;

    private List<Setting> settings = new ArrayList<Setting>();

    private	Setting<Integer> maxPlayers;
    private	Setting<Integer> respawnTime;
    private	Setting<Integer> scoreLimit;
    private	Setting<Integer> outOfBoundsTime;
    private	Setting<Integer> spawnProtection;

    private	Setting<Boolean> friendlyFire;
    private	Setting<Boolean>  outOfBoundsArea;
    private Setting<Boolean>  spectatorOutOfBoundsArea;
    private	Setting<Boolean> autoBalance;
    private	Setting<Boolean>  allowItemDrop;
    private	Setting<Boolean>  allowBlockChange;
    private Setting<Boolean>  allowSpectateFly;
    private Setting<Boolean>  allowSpectateInArea;
	
	public ArenaSettings(ArenaPVP plugin, YamlStorage arenaStorage) {
        this.arenaStorage = arenaStorage;
		config = arenaStorage.getConfig();
		initializeSettings();
        settingMenu = new SettingMenu(plugin, arenaStorage, this);
    }

    public void reloadSettings(){
        arenaStorage.reloadConfig();
        initializeSettings();
    }

	public void initializeSettings() {
        //Make sure settings list is clear.
        settings.clear();

        //General settings
        maxPlayers = new Setting<Integer>(config.getInt("maxplayers"), SettingGroup.GENERAL, "maxplayers", "Max players", "Number of players that can join.");
        scoreLimit = new Setting<Integer>(config.getInt("scorelimit"), SettingGroup.GENERAL, "scorelimit", "Score limit", "Score needed to win.");
        autoBalance = new Setting<Boolean>(config.getBoolean("autobalance"), SettingGroup.GENERAL, "autobalance", "Auto balance", "Balance teams before game start.");
        friendlyFire = new Setting<Boolean>(config.getBoolean("friendlyfire"), SettingGroup.GENERAL, "friendlyfire", "Friendly fire", "Allow teammates to hit eachother.");
        settings.add(maxPlayers);
        settings.add(scoreLimit);
        settings.add(autoBalance);
        settings.add(friendlyFire);

        //Other? settings
        allowItemDrop = new Setting<Boolean>(config.getBoolean("allowitemdrop"), SettingGroup.OTHER, "allowitemdrop", "Allow item drop", "Allow players to drop items and armour.");
        allowBlockChange = new Setting<Boolean>(config.getBoolean("allowblockchange"), SettingGroup.OTHER, "allowblockchange", "Allow block change", "Allow players to change blocks");
        settings.add(allowItemDrop);
        settings.add(allowBlockChange);

        //Respawn settings
        respawnTime = new Setting<Integer>(config.getInt("respawntime"), SettingGroup.RESPAWN, "respawntime", "Respawn time", "Number of seconds before a player respawns");
        spawnProtection = new Setting<Integer>(config.getInt("spawnprotection"), SettingGroup.RESPAWN, "spawnprotection", "Spawn protection", "Number of seconds a player is invulnerable after respawning");
        settings.add(respawnTime);
        settings.add(spawnProtection);

        //Out of bounds settings
        outOfBoundsArea = new Setting<Boolean>(config.getBoolean("outofboundsarea"), SettingGroup.OUTOFBOUNDS, "outofboundsarea", "Out of bounds area", "Have an out of bounds area.");
        outOfBoundsTime = new Setting<Integer>(config.getInt("outofboundstime"), SettingGroup.OUTOFBOUNDS, "outofboundstime", "Out of bounds time", "Number of seconds before an out of bounds player is killed");
        settings.add(outOfBoundsArea);
        settings.add(outOfBoundsTime);

        //Spectate settings
        allowSpectateInArea = new Setting<Boolean>(config.getBoolean("allowspectateinarea"), SettingGroup.SPECTATOR, "allowspectateinarea", "Allow spectate in area", "Allow spectators to get into the pvp out of bounds area.");
        allowSpectateFly = new Setting<Boolean>(config.getBoolean("allowspectatefly"), SettingGroup.SPECTATOR, "allowspectatefly", "Allow spectate fly" , "Allow spectators to fly around.");
        spectatorOutOfBoundsArea = new Setting<Boolean>(config.getBoolean("spectatoroutofboundsarea"), SettingGroup.SPECTATOR, "spectatoroutofboundsarea", "Spectator out of bounds area", "Have a seperate out of bounds area for spectators.");
        settings.add(allowSpectateInArea);
        settings.add(allowSpectateFly);
        settings.add(spectatorOutOfBoundsArea);
	}

    public SettingMenu getSettingMenu(){
        return settingMenu;
    }

    public List<Setting> getSettings(){
        return settings;
    }

	public int getMaxPlayers() {
		return maxPlayers.getSetting();
	}

	public int getRespawnTime() {
		return respawnTime.getSetting();
	}

	public int getScoreLimit() {
		return scoreLimit.getSetting();
	}

	public int getOutOfBoundsTime() {
		return outOfBoundsTime.getSetting();
	}
	
	public int getSpawnProtection() {
		return spawnProtection.getSetting();
	}

	public boolean isFriendlyFire() {
		return friendlyFire.getSetting();
	}

	public boolean isOutOfBoundsArea() {
		return outOfBoundsArea.getSetting();
	}

    public boolean isSpectatorOutOfBoundsArea() {
        return spectatorOutOfBoundsArea.getSetting();
    }

	public boolean isAutoBalance() {
		return autoBalance.getSetting();
	}
	
	public boolean isAllowItemDrop() {
		return allowItemDrop.getSetting();
	}
	
	public boolean isAllowBlockChange() {
		return allowBlockChange.getSetting();
	}

    public boolean isAllowSpectateFly() {
        return allowSpectateFly.getSetting();
    }

    public boolean isAllowSpectateInArea() {
        return allowSpectateInArea.getSetting();
    }
}
