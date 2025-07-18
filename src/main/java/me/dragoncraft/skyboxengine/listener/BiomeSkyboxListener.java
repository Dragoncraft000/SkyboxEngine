package me.dragoncraft.skyboxengine.listener;

import me.dragoncraft.skyboxengine.SkyboxEngine;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BiomeSkyboxListener implements Listener {

    private final Map<UUID, Biome> lastPlayerBiomes = new HashMap<>();

    public BiomeSkyboxListener() {
        Bukkit.getScheduler().runTaskTimer(SkyboxEngine.getInstance(),this::checkBiomeChanges,0,1);
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        lastPlayerBiomes.remove(event.getPlayer().getUniqueId());
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        lastPlayerBiomes.put(event.getPlayer().getUniqueId(),getPlayerBiome(event.getPlayer()));
        SkyboxEngine.getPlayerSkyboxManager().checkBiomeSkyboxChange(event.getPlayer(), getPlayerBiome(event.getPlayer()),getPlayerBiome(event.getPlayer()));
    }

    public static Biome getPlayerBiome(Player player) {
        return player.getWorld().getBiome(player.getLocation());
    }


    private void checkBiomeChanges() {
        if (Bukkit.getServer().getCurrentTick() % SkyboxEngine.getConfigInstance().getBiomeCheckInterval() != 0) {
            return;
        }
        Bukkit.getOnlinePlayers().forEach(p -> {
            Biome current = getPlayerBiome(p);
            if (current.getKey().equals(lastPlayerBiomes.get(p.getUniqueId()).getKey())) {
                return;
            }
            SkyboxEngine.getPlayerSkyboxManager().checkBiomeSkyboxChange(p,current, lastPlayerBiomes.get(p.getUniqueId()));
            lastPlayerBiomes.put(p.getUniqueId(),current);
        });
    }

}
