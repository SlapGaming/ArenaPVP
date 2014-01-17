package me.naithantu.ArenaPVP.Events.BukkitEvents.Player;

import me.naithantu.ArenaPVP.Arena.ArenaPlayer;
import me.naithantu.ArenaPVP.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeListener implements Listener {

    public FoodLevelChangeListener(){
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();

            ArenaPlayer arenaPlayer = ArenaManager.getPlayerByName(player.getName());
            if(arenaPlayer != null){
                arenaPlayer.getArena().getGamemode().onFoodLevelChange(event);
            }
        }
    }
}
