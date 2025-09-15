package de.vectorflare.skyboxengine.listener;

import de.vectorflare.skyboxengine.SkyboxEngine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MainListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        SkyboxEngine.getPlayerSkyboxManager().addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        SkyboxEngine.getPlayerSkyboxManager().removePlayer(event.getPlayer());
    }
}
