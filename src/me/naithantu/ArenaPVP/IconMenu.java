package me.naithantu.ArenaPVP;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class IconMenu implements Listener {

    private String name;
    private int size;
    private OptionClickEventHandler clickEventHandler;
    private Plugin plugin = ArenaPVP.getInstance();
    private boolean destroyOnClose;
    private String playerName;

    private String[] optionNames;
    private ItemStack[] optionIcons;

    private Inventory inventory;

    public IconMenu(String name, int size, OptionClickEventHandler clickEventHandler, boolean destroyOnClose, String playerName) {
        //Add chatcolors to make it impossible for players to "create" an iconmenu via anvil
        this.name = ChatColor.WHITE + "" + ChatColor.RESET + name;
        this.playerName = playerName;
        this.size = size;
        this.clickEventHandler = clickEventHandler;
        this.destroyOnClose = destroyOnClose;
        this.optionNames = new String[size];
        this.optionIcons = new ItemStack[size];
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    public IconMenu setOption(int position, ItemStack icon, String name, String... info) {
        optionNames[position] = name;
        optionIcons[position] = setItemNameAndLore(icon, name, info);
        return this;
    }

    public String getName(int position) {
        return optionNames[position];
    }

    public void clearMenu() {
        optionNames = new String[size];
        optionIcons = new ItemStack[size];
    }

    public void open(Player player) {
        inventory = Bukkit.createInventory(player, size, name);
        for (int i = 0; i < optionIcons.length; i++) {
            if (optionIcons[i] != null) {
                inventory.setItem(i, optionIcons[i]);
            }
        }
        player.openInventory(inventory);
    }

    @SuppressWarnings("deprecation")
    public void update(Player player) {
        inventory.clear();
        for (int i = 0; i < optionIcons.length; i++) {
            if (optionIcons[i] != null) {
                inventory.setItem(i, optionIcons[i]);
            }
        }
        player.updateInventory();
    }

    public void destroy() {
        HandlerList.unregisterAll(this);
        clickEventHandler = null;
        plugin = null;
        optionNames = null;
        optionIcons = null;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getTitle().equals(name)) {
            if (playerName == null || playerName.equals(event.getPlayer().getName())) {
                if (destroyOnClose) {
                    destroy();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals(name)) {
            if (playerName == null || playerName.equals(event.getWhoClicked().getName())) {
                event.setCancelled(true);
                int slot = event.getRawSlot();
                if (slot >= 0 && slot < size && optionNames[slot] != null) {
                    Plugin plugin = this.plugin;
                    OptionClickEvent e = new OptionClickEvent(this, (Player) event.getWhoClicked(), slot, optionNames[slot]);
                    clickEventHandler.onOptionClick(e);
                    if (e.willClose()) {
                        final Player p = (Player) event.getWhoClicked();
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            public void run() {
                                p.closeInventory();
                            }
                        }, 1);
                    }
                    if (e.willDestroy()) {
                        destroy();
                    }
                }
            }
        }
    }

    public interface OptionClickEventHandler {
        public void onOptionClick(OptionClickEvent event);
    }

    public class OptionClickEvent {
        private IconMenu iconMenu;
        private Player player;
        private int position;
        private String name;
        private boolean close;
        private boolean destroy;

        public OptionClickEvent(IconMenu iconMenu, Player player, int position, String name) {
            this.iconMenu = iconMenu;
            this.player = player;
            this.position = position;
            this.name = name;
            this.close = true;
            this.destroy = false;
        }

        public IconMenu getIconMenu() {
            return iconMenu;
        }

        public Player getPlayer() {
            return player;
        }

        public int getPosition() {
            return position;
        }

        public String getName() {
            return name;
        }

        public boolean willClose() {
            return close;
        }

        public boolean willDestroy() {
            return destroy;
        }

        public void setWillClose(boolean close) {
            this.close = close;
        }

        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }
    }

    private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(lore));
        item.setItemMeta(im);
        return item;
    }

}