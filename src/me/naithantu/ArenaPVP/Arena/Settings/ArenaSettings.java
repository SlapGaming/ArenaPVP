package me.naithantu.ArenaPVP.Arena.Settings;

import me.naithantu.ArenaPVP.Storage.YamlStorage;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ArenaSettings {
    private YamlStorage arenaStorage;
    private	FileConfiguration config;

    private	int maxPlayers;
    private	int respawnTime;
    private	IntSetting scoreLimit;
    private	int outOfBoundsTime;
    private	int spawnProtection;

    private	boolean friendlyFire;
    private	boolean autojoinPvpChat;
    private	boolean outOfBoundsArea;
    private boolean spectatorOutOfBoundsArea;
    private	boolean autoBalance;
    private	boolean allowItemDrop;
    private	boolean allowBlockChange;
    private boolean allowSpectateFly;
	
	public ArenaSettings(YamlStorage arenaStorage) {
        this.arenaStorage = arenaStorage;
		config = arenaStorage.getConfig();
		initializeSettings();
	}

    public void reloadSettings(){
        arenaStorage.reloadConfig();
        initializeSettings();
    }

	private void initializeSettings() {
		maxPlayers = config.getInt("maxplayers");
		respawnTime = config.getInt("respawntime");
		//scoreLimit = config.getInt("scorelimit");
		outOfBoundsTime = config.getInt("outofboundstime");
		spawnProtection = config.getInt("spawnprotection");

		friendlyFire = config.getBoolean("friendlyfire");
		autojoinPvpChat = config.getBoolean("autojoinpvpchat");
		outOfBoundsArea = config.getBoolean("outofboundsarea");
        spectatorOutOfBoundsArea = config.getBoolean("spectatoroutofboundsarea");
		autoBalance = config.getBoolean("autobalance");
		allowItemDrop = config.getBoolean("allowitemdrop");
		allowBlockChange = config.getBoolean("allowblockchange");

        scoreLimit = new IntSetting(config.getInt("scorelimit"), SettingGroup.GAMESETTINGS, "Score limit");
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public int getRespawnTime() {
		return respawnTime;
	}

	public int getScoreLimit() {
		return scoreLimit.getSetting();
	}

	public int getOutOfBoundsTime() {
		return outOfBoundsTime;
	}
	
	public int getSpawnProtection() {
		return spawnProtection;
	}

	public boolean isFriendlyFire() {
		return friendlyFire;
	}

	public boolean isForcePvpChat() {
		return autojoinPvpChat;
	}

	public boolean isOutOfBoundsArea() {
		return outOfBoundsArea;
	}

    public boolean isSpectatorOutOfBoundsArea() {
        return spectatorOutOfBoundsArea;
    }

	public boolean isAutoBalance() {
		return autoBalance;
	}
	
	public boolean isAllowItemDrop() {
		return allowItemDrop;
	}
	
	public boolean isAllowBlockChange() {
		return allowBlockChange;
	}

    public boolean isAllowSpectateFly() {
        return allowSpectateFly;
    }
}
