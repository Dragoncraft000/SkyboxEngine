package me.dragoncraft.skyboxengine.listener;

import me.dragoncraft.skyboxengine.SkyboxEngine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class WorldSkyboxListener implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        SkyboxEngine.getPlayerSkyboxManager().checkWorldSkyboxChange(event.getPlayer(), event.getPlayer().getWorld(), event.getFrom());
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        SkyboxEngine.getPlayerSkyboxManager().checkWorldSkyboxChange(event.getPlayer(), null, event.getPlayer().getWorld());
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        SkyboxEngine.getPlayerSkyboxManager().addPlayer(event.getPlayer());
    }
}
