package de.vectorflare.skyboxengine.listener;

import io.papermc.paper.event.player.PlayerUntrackEntityEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RemovedEntityListener implements Listener {

    @EventHandler
    public void onEntityRemove(PlayerUntrackEntityEvent event) {
        if (event.getEntity().getType() != EntityType.ITEM_DISPLAY) {
            return;
        }
        WorldSkyboxListener.checkWorldSkyboxChange(event.getPlayer(), event.getPlayer().getWorld());
    }

}
