package de.vectorflare.skyboxengine.listener;

import de.vectorflare.skyboxengine.SkyboxEngine;
import de.vectorflare.skyboxengine.config.ConfigManager;
import de.vectorflare.skyboxengine.manager.PlayerSkyboxData;
import de.vectorflare.skyboxengine.skybox.ActiveSkybox;
import de.vectorflare.skyboxengine.skybox.SkyboxReason;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class BiomeSkyboxListener implements Listener {

    private static final Map<UUID, Biome> lastPlayerBiomes = new HashMap<>();

    public BiomeSkyboxListener() {
        Bukkit.getScheduler().runTaskTimer(SkyboxEngine.getInstance(),BiomeSkyboxListener::checkBiomeChanges,0,1);
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        lastPlayerBiomes.remove(event.getPlayer().getUniqueId());
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        lastPlayerBiomes.put(event.getPlayer().getUniqueId(),getPlayerBiome(event.getPlayer()));
        checkBiomeSkyboxChange(event.getPlayer(), getPlayerBiome(event.getPlayer()),getPlayerBiome(event.getPlayer()));
    }

    public static Biome getPlayerBiome(Player player) {
        return player.getWorld().getBiome(player.getLocation());
    }

    public static void retrackBiomes() {
        lastPlayerBiomes.clear();
        checkBiomeChanges();
    }


    public static void checkBiomeChanges() {
        if (Bukkit.getServer().getCurrentTick() % SkyboxEngine.getConfigInstance().getBiomeCheckInterval() != 0) {
            return;
        }
        Bukkit.getOnlinePlayers().forEach(p -> {
            Biome current = getPlayerBiome(p);
            checkBiomeSkyboxChange(p,current, lastPlayerBiomes.get(p.getUniqueId()));
            lastPlayerBiomes.put(p.getUniqueId(),current);
        });
    }


    private static void checkBiomeSkyboxChange(Player player, Biome current, Biome last) {

        String currentString = current.getKey().asString();
        String lastString = "";
        if (last != null) {
            lastString = last.getKey().asString();
        }
        if (Objects.equals(currentString, lastString)) {
            return;
        }
        String currentSkybox = SkyboxEngine.getConfigInstance().getBiomeSkyboxes().getOrDefault(currentString,"");

        PlayerSkyboxData data = SkyboxEngine.getPlayerSkyboxManager().getSkyboxData(player);
        List<ActiveSkybox> skyboxes = data.playerSkyboxes.stream().toList();
        for (ActiveSkybox activeSkybox : skyboxes) {
            if (activeSkybox.reason == SkyboxReason.BIOME) {
                data.removeActiveSkybox(activeSkybox);
                break;
            }
        }
        if (!currentSkybox.isEmpty()) {
            if (ConfigManager.getSkyboxSettings(currentSkybox) == null) {
                return;
            }
            data.addActivePlayerSkybox(new ActiveSkybox(ConfigManager.getSkyboxSettings(currentSkybox),SkyboxReason.BIOME,SkyboxEngine.getConfigInstance().getBiomeSkyboxPriority()));
        }

    }



}
