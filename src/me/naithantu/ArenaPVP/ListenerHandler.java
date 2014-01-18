package me.naithantu.ArenaPVP;

import me.naithantu.ArenaPVP.Events.BukkitEvents.BlockRedstoneListener;
import me.naithantu.ArenaPVP.Events.BukkitEvents.Player.*;
import me.naithantu.ArenaPVP.Events.BukkitEvents.ProjectileHitListener;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

public class ListenerHandler {
    ArenaPVP plugin;
    PluginManager pluginManager;

    private BlockListener blockListener;
    private ChatListener chatListener;
    private DamageListener damageListener;
    private DeathListener deathListener;
    private DropItemListener dropItemListener;
    private FoodLevelChangeListener foodLevelChangeListener;
    private PickupItemListener pickupItemListener;
    private InventoryClickListener inventoryClickListener;
    private MoveListener moveListener;
    private QuitListener quitListener;
    private RespawnListener respawnListener;

    private BlockRedstoneListener blockRedstoneListener;
    private ProjectileHitListener projectileHitListener;

    public ListenerHandler(ArenaPVP plugin, PluginManager pluginManager){
        this.plugin = plugin;
        this.pluginManager = pluginManager;

        blockListener = new BlockListener();
        chatListener = new ChatListener();
        damageListener = new DamageListener();
        deathListener = new DeathListener();
        dropItemListener = new DropItemListener();
        foodLevelChangeListener = new FoodLevelChangeListener();
        pickupItemListener = new PickupItemListener();
        inventoryClickListener = new InventoryClickListener();
        moveListener = new MoveListener();
        quitListener = new QuitListener();
        respawnListener = new RespawnListener();

        blockRedstoneListener = new BlockRedstoneListener();
        projectileHitListener = new ProjectileHitListener();

        registerPermanentListeners();
        registerArenaListeners();
    }

    public void registerPermanentListeners(){
        //Register listeners that are always needed, even when no arenas are loaded.
        pluginManager.registerEvents(respawnListener, plugin);
    }

    public void registerArenaListeners(){
        //Player events.
        pluginManager.registerEvents(blockListener, plugin);
        pluginManager.registerEvents(chatListener, plugin);
        pluginManager.registerEvents(damageListener, plugin);
        pluginManager.registerEvents(deathListener, plugin);
        pluginManager.registerEvents(dropItemListener, plugin);
        pluginManager.registerEvents(foodLevelChangeListener, plugin);
        pluginManager.registerEvents(pickupItemListener, plugin);
        pluginManager.registerEvents(inventoryClickListener, plugin);
        pluginManager.registerEvents(moveListener, plugin);
        pluginManager.registerEvents(quitListener, plugin);
        //Other events
        pluginManager.registerEvents(blockRedstoneListener, plugin);
        pluginManager.registerEvents(projectileHitListener, plugin);
    }

    public void unregisterArenaListeners(){
        //Player events
        HandlerList.unregisterAll(blockListener);
        HandlerList.unregisterAll(chatListener);
        HandlerList.unregisterAll(damageListener);
        HandlerList.unregisterAll(deathListener);
        HandlerList.unregisterAll(dropItemListener);
        HandlerList.unregisterAll(foodLevelChangeListener);
        HandlerList.unregisterAll(pickupItemListener);
        HandlerList.unregisterAll(inventoryClickListener);
        HandlerList.unregisterAll(moveListener);
        HandlerList.unregisterAll(quitListener);
        //Other events
        HandlerList.unregisterAll(blockRedstoneListener);
        HandlerList.unregisterAll(projectileHitListener);


    }
}
