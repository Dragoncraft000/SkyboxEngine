package me.dragoncraft.skyboxengine.manager;

import me.dragoncraft.skyboxengine.SkyboxEngine;
import me.dragoncraft.skyboxengine.config.Settings;
import me.dragoncraft.skyboxengine.listener.BiomeSkyboxListener;
import me.dragoncraft.skyboxengine.skybox.PlayerSkybox;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class PlayerSkyboxManager {

    private final HashMap<UUID, PlayerSkybox> playerSkyboxes = new HashMap<>();
    private final BukkitTask updateTask;

    public PlayerSkyboxManager() {
        updateTask = Bukkit.getScheduler().runTaskTimerAsynchronously(SkyboxEngine.getInstance(),this::tickSkyboxes,0,1);
    }


    private void tickSkyboxes() {
        playerSkyboxes.forEach(((uuid, playerSkybox) -> {
            if (playerSkybox == null) {
                return;
            }
            playerSkybox.tickSkybox();
        }));
    }


    public PlayerSkybox getPlayerSkybox(Player player) {
        return playerSkyboxes.getOrDefault(player.getUniqueId(),null);
    }
    public boolean hasSkybox(Player player) {
        return playerSkyboxes.containsKey(player.getUniqueId()) && playerSkyboxes.get(player.getUniqueId()) != null;
    }

    public void setPlayerSkybox(Player player, PlayerSkybox settings) {
        playerSkyboxes.put(player.getUniqueId(),settings);
    }

    public void addPlayer(Player player) {
        setPlayerSkybox(player,null);
        checkWorldSkyboxChange(player,player.getWorld(),null);
    }

    public void removePlayer(Player player) {
        removePlayer(player,0);
    }
    public void removePlayer(Player player,int delay) {
        if (hasSkybox(player)) {
            playerSkyboxes.get(player.getUniqueId()).removeSkybox(delay);
        }
        playerSkyboxes.remove(player.getUniqueId());
    }



    public void checkBiomeSkyboxChange(Player player, Biome current, Biome last) {
        Settings.SkyboxSettings currentSettings = SkyboxEngine.getConfigInstance().getBiomeSkyboxes().getOrDefault(current.getKey().toString(),null);
        Settings.SkyboxSettings lastSettings = SkyboxEngine.getConfigInstance().getBiomeSkyboxes().getOrDefault(last.getKey().toString(),null);

        if (currentSettings != null && hasSkybox(player)) {
            removePlayer(player,1);
        }
        if (currentSettings != null && !hasSkybox(player)) {
            PlayerSkybox skybox = new PlayerSkybox(player, player.getWorld(), BiomeSkyboxListener.getPlayerBiome(player),currentSettings);
            setPlayerSkybox(player,skybox);
            skybox.createSkybox();
        }
        if (currentSettings == null && lastSettings != null) {
            removePlayer(player,1);
            checkWorldSkyboxChange(player,player.getWorld(),null);
        }
    }



    public void checkWorldSkyboxChange(Player player, World world,World last) {
        if (last != null && hasSkybox(player)) {
            if (getPlayerSkybox(player).getWorld() == last) {
                if (SkyboxEngine.getConfigInstance().getDebugLogLevel() > 0) {
                    SkyboxEngine.info("Removing skybox " + getPlayerSkybox(player).getSettings().getSkyboxId() + " for player " + player.getName());
                }
                removePlayer(player);
            }
        }
        if (world != null && !hasSkybox(player)) {
            if (SkyboxEngine.getConfigInstance().getDimensionSkyboxes().containsKey(world.getKey().toString())) {
                Settings.SkyboxSettings settings = SkyboxEngine.getConfigInstance().getDimensionSkyboxes().get(world.getKey().toString());
                PlayerSkybox skybox = new PlayerSkybox(player,world,settings);
                skybox.createSkybox();
                setPlayerSkybox(player,skybox);
                if (SkyboxEngine.getConfigInstance().getDebugLogLevel() > 0) {
                    SkyboxEngine.info("Creating skybox " + skybox.getSettings().getSkyboxId() + " for player " + player.getName());
                }
            }
        }
    }


}
