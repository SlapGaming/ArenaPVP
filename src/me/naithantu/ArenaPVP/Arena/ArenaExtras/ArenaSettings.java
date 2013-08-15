package me.naithantu.ArenaPVP.Arena.ArenaExtras;

import org.bukkit.configuration.file.FileConfiguration;

public class ArenaSettings {
	FileConfiguration config;

	int maxPlayers;
	int respawnTime;
	int scoreLimit;
	int outOfBoundsTime;
	int spawnProtection;

	boolean friendlyFire;
	boolean autojoinPvpChat;
	boolean outOfBoundsArea;
	boolean autoBalance;
	boolean allowItemDrop;
	boolean allowBlockChange;
	
	public ArenaSettings(FileConfiguration config) {
		this.config = config;
		initializeSettings();
	}

	public void initializeSettings() {
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

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public void setRespawnTime(int respawnTime) {
		this.respawnTime = respawnTime;
	}

	public void setScoreLimit(int scoreLimit) {
		this.scoreLimit = scoreLimit;
	}

	public void setOutOfBoundsTime(int outOfBoundsTime) {
		this.outOfBoundsTime = outOfBoundsTime;
	}

	public void setSpawnProtection(int spawnProtection) {
		this.spawnProtection = spawnProtection;
	}
	
	public void setFriendlyFire(boolean friendlyFire) {
		this.friendlyFire = friendlyFire;
	}

	public void setForcePvpChat(boolean forcePvpChat) {
		this.autojoinPvpChat = forcePvpChat;
	}

	public void setOutOfBoundsArea(boolean outOfBoundsArea) {
		this.outOfBoundsArea = outOfBoundsArea;
	}

	public void setAutoBalance(boolean autoBalance) {
		this.autoBalance = autoBalance;
	}
	
	public void setAllowItemDrop(boolean allowItemDrop) {
		this.allowItemDrop = allowItemDrop;
	}
	
	public void setAllowBlockChange(boolean allowBlockChange) {
		this.allowBlockChange = allowBlockChange;
	}
}
