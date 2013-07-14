package me.naithantu.ArenaPVP.Objects.ArenaExtras;

import org.bukkit.configuration.file.FileConfiguration;

public class ArenaSettings {
	FileConfiguration config;

	int maxPlayers;
	int respawnTime;
	int scoreLimit;
	int outOfBoundsTime;
	
	boolean friendlyFire;
	boolean forcePvpChat;
	boolean outOfBoundsArea;
	boolean autoBalance;
	
	public ArenaSettings(FileConfiguration config) {
		this.config = config;
		initializeSettings();
	}

	public void initializeSettings() {
		maxPlayers = config.getInt("maxplayers");
		respawnTime = config.getInt("respawntime");
		scoreLimit = config.getInt("scorelimit");
		outOfBoundsTime = config.getInt("outofboundstime");

		friendlyFire = config.getBoolean("friendlyfire");
		forcePvpChat = config.getBoolean("forcepvpchat");
		outOfBoundsArea = config.getBoolean("outofboundsarea");
		autoBalance = config.getBoolean("autobalance");
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

	public boolean isFriendlyFire() {
		return friendlyFire;
	}

	public boolean isForcePvpChat() {
		return forcePvpChat;
	}

	public boolean isOutOfBoundsArea() {
		return outOfBoundsArea;
	}

	public boolean isAutoBalance() {
		return autoBalance;
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

	public void setFriendlyFire(boolean friendlyFire) {
		this.friendlyFire = friendlyFire;
	}

	public void setForcePvpChat(boolean forcePvpChat) {
		this.forcePvpChat = forcePvpChat;
	}

	public void setOutOfBoundsArea(boolean outOfBoundsArea) {
		this.outOfBoundsArea = outOfBoundsArea;
	}

	public void setAutoBalance(boolean autoBalance) {
		this.autoBalance = autoBalance;
	}
}
