package me.naithantu.ArenaPVP.Arena.ArenaExtras;

import me.naithantu.ArenaPVP.Storage.YamlStorage;
import org.bukkit.configuration.file.FileConfiguration;

public class ArenaSettings {
    private YamlStorage arenaStorage;
    private	FileConfiguration config;

    private	int maxPlayers;
    private	int respawnTime;
    private	int scoreLimit;
    private	int outOfBoundsTime;
    private	int spawnProtection;

    private	boolean friendlyFire;
    private	boolean autojoinPvpChat;
    private	boolean outOfBoundsArea;
    private	boolean autoBalance;
    private	boolean allowItemDrop;
    private	boolean allowBlockChange;
	
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
		scoreLimit = config.getInt("scorelimit");
		outOfBoundsTime = config.getInt("outofboundstime");
		spawnProtection = config.getInt("spawnprotection");

		friendlyFire = config.getBoolean("friendlyfire");
		autojoinPvpChat = config.getBoolean("autojoinpvpchat");
		outOfBoundsArea = config.getBoolean("outofboundsarea");
		autoBalance = config.getBoolean("autobalance");
		allowItemDrop = config.getBoolean("allowitemdrop");
		allowBlockChange = config.getBoolean("allowblockchange");
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public int getRespawnTime() {
		return respawnTime;
	}

	public int getScoreLimit() {
		return scoreLimit;
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

	public boolean isAutoBalance() {
		return autoBalance;
	}
	
	public boolean isAllowItemDrop() {
		return allowItemDrop;
	}
	
	public boolean isAllowBlockChange() {
		return allowBlockChange;
	}
}
