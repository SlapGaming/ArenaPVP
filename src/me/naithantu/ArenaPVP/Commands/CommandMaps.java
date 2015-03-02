package me.naithantu.ArenaPVP.Commands;

import me.naithantu.ArenaPVP.Arena.Settings.ArenaSettings;
import me.naithantu.ArenaPVP.Storage.YamlStorage;
import me.naithantu.ArenaPVP.Util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class CommandMaps extends AbstractCommand {

    /** The number of maps shown per page */
    private static final int MAPS_PER_PAGE = 8;

    /** Cached settings for quick reference */
    private static ArrayList<WrappedSettings> cachedSettings;
    private static Integer totalMaps;
    private static Integer enabledMaps;

    /** The ID of the task that is going to clear the Cache */
    private static Integer cacheClearTaskID;


	protected CommandMaps(CommandSender sender, String[] args) {
		super(sender, args);
	}

	@Override
    protected boolean handle() {
		if (!testPermission(sender, "maps") && !testPermission(sender, "mod")) {
			this.noPermission(sender);
			return true;
		}

        //Check if page is given
        final int page;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                badMsg(sender, "'" + args[0] + "' is not a number.");
                return true;
            }
        } else {
            page = 1;
        }

        //Check if filter is given
        final String gamemodeFilter = (args.length > 1 ? args[1] : null);


        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        //Check if cached
        if (cachedSettings != null) {
            sendMaps(page, gamemodeFilter);

            //Recreate the wipe task
            if (scheduler.isQueued(cacheClearTaskID) || scheduler.isCurrentlyRunning(cacheClearTaskID)) {
                scheduler.cancelTask(cacheClearTaskID);
            }
            scheduleCacheWiper();
        } else {
            msg(sender, "Loading maps, please wait...");
            scheduler.runTaskAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    //Get the folder with maps
                    File mapsFolder = new File(plugin.getDataFolder() + File.separator + "maps");
                    if (!mapsFolder.exists()) {
                        badMsg(sender, "The maps folder is missing!");
                        return;
                    }

                    cachedSettings = new ArrayList<>();
                    totalMaps = 0;
                    enabledMaps = 0;

                    //Loop through the maps and find YML files
                    for (File file : mapsFolder.listFiles()) {
                        if (!file.getName().endsWith(".yml")) {
                            continue;
                        }
                        String filename = file.getName().substring(0, file.getName().length() - 4);

                        //Load the map
                        ArenaSettings settings = new ArenaSettings(new YamlStorage("maps", filename), null, false);
                        cachedSettings.add(new WrappedSettings(filename, settings));

                        //Increase values
                        totalMaps++;
                        if (settings.isEnabled()) {
                            enabledMaps++;
                        }
                    }

                    //Sort the settings
                    Collections.sort(cachedSettings);

                    //Send the requested page
                    sendMaps(page, gamemodeFilter);
                    scheduleCacheWiper();
                }
            });
        }
		return true;
	}

    /**
     * Send a page of maps to the CommandSender
     * @param page the page
     * @param filter Filtered results
     */
    private void sendMaps(int page, String filter) {
        ArrayList<WrappedSettings> maps = CommandMaps.cachedSettings;
        int totalMaps = CommandMaps.totalMaps;
        int enabledMaps = CommandMaps.enabledMaps;

        //Check if filter is given
        if (filter != null) {
            maps = new ArrayList<>();
            totalMaps = 0;
            enabledMaps = 0;

            //Filter the maps
            for (WrappedSettings cachedSetting : cachedSettings) {
                if (cachedSetting.arenaSettings.getGamemode().equalsIgnoreCase(filter)) {
                    maps.add(cachedSetting);
                    totalMaps++;
                    if (cachedSetting.arenaSettings.isEnabled()) {
                        enabledMaps++;
                    }
                }
            }
        }

        //Check if any maps found
        if (totalMaps == 0) {
            badMsg(sender, "There are no maps" + (filter == null ? "!" : " with this gamemode!"));
            return;
        }

        //Check the page
        int totalPages = (int) Math.ceil((double) totalMaps / (double) MAPS_PER_PAGE);
        if (page <= 0 || page > totalPages) {
            badMsg(sender, "There " + (totalPages == 1 ? "is" : "are") + " only " + totalPages + " " + (totalPages == 1 ? "page" : "pages") + ".");
            return;
        }

        //Send the pages
        Util.msg(sender, "Maps | Page " + page + "/" + totalPages + ChatColor.GRAY + " (" + enabledMaps + " enabled, " + (totalMaps - enabledMaps) + " disabled)");
        int startIndex = (page - 1) * MAPS_PER_PAGE;
        for (int i = 0; i < MAPS_PER_PAGE && (i + startIndex < totalMaps); i++) {
            //Get the settings
            WrappedSettings wrappedSettings = maps.get(startIndex + i);
            ArenaSettings arenaSettings = wrappedSettings.arenaSettings;

            //Send map status
            ChatColor gray = ChatColor.GRAY;
            String disabled = (arenaSettings.isEnabled() ? "" : ChatColor.STRIKETHROUGH.toString());
            sender.sendMessage(ChatColor.GOLD + disabled + "[" + arenaSettings.getGamemode().toUpperCase() + "]" + ChatColor.WHITE + " " + wrappedSettings.filename + gray + " (" + ChatColor.translateAlternateColorCodes('&', arenaSettings.getNickname()) + gray + ")");
        }
    }

    /** Schedule a task that will wipe the cached settings */
    private void scheduleCacheWiper() {
        cacheClearTaskID = plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                wipeCache(false);
            }
        }, (5 * 60 * 20)).getTaskId();
    }

    /** Wipe the cached settings */
    public static void wipeCache(boolean stopTask) {
        //Stop task if requested
        if (stopTask) {
            if (cacheClearTaskID != null) {
                Bukkit.getScheduler().cancelTask(cacheClearTaskID);
            }
        }

        //Wipe data
        cacheClearTaskID = null;
        cachedSettings = null;
        totalMaps = null;
        enabledMaps = null;
    }

    private class WrappedSettings implements Comparable<WrappedSettings> {
        public String filename;
        public ArenaSettings arenaSettings;

        private WrappedSettings(String filename, ArenaSettings arenaSettings) {
            this.filename = filename;
            this.arenaSettings = arenaSettings;
        }

        @Override
        public int compareTo(WrappedSettings o) {
            //Compare enabled status
            if (arenaSettings.isEnabled() != o.arenaSettings.isEnabled()) {
                return Boolean.compare(o.arenaSettings.isEnabled(), arenaSettings.isEnabled());
            }

            //Compare name
            return filename.compareToIgnoreCase(o.filename);
        }
    }


}
