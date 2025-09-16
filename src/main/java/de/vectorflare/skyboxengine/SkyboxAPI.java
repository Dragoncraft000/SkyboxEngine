package de.vectorflare.skyboxengine;

import de.vectorflare.skyboxengine.config.ConfigManager;
import de.vectorflare.skyboxengine.config.Settings;
import de.vectorflare.skyboxengine.skybox.ActiveSkybox;
import de.vectorflare.skyboxengine.skybox.SkyboxReason;
import org.bukkit.entity.Player;

import java.util.Map;

@SuppressWarnings("unused")
public class SkyboxAPI {



    /**
     * Allows you to access the api to simplify interacting with the plugin.
     * @return The Skybox Engine API
     */
    public static SkyboxAPI getAPI() {
        return SkyboxEngine.api;
    }

    /**
     * Reloads the config files and reconstructs all active skyboxes.
     * Same effect as running /skyboxengine reload
     */
    public void reload() {
        SkyboxEngine.getConfigManager().reload();
        SkyboxEngine.getPlayerSkyboxManager().recalculateSkyboxes();
    }

    /**
     * Reconstructs all active skyboxes without reloading the config files.
     */
    public void reconstruct() {
        SkyboxEngine.getConfigManager().reload();
        SkyboxEngine.getPlayerSkyboxManager().recalculateSkyboxes();
    }

    /**
     * Gets a map of all currently registered skyboxes.
     * @return A mapping of all skybox ids to the specified settings
     */
    public Map<String, Settings.SkyboxSettings> getRegisteredSkyboxes() {
        return SkyboxEngine.getConfigInstance().getSkyboxRegistry();
    }

    /**
     * Accesses the settings of any skybox id directly.
     * @param skyboxName The id of the skybox to query
     * @return The settings of the specified skybox or null if the id is not registered
     */
    public Settings.SkyboxSettings getSkyboxSettings(String skyboxName) {
        return ConfigManager.getSkyboxSettings(skyboxName);
    }

    /**
     * Gets a map of all currently bound dimensions.
     * @return A mapping of dimension ids (e.g. minecraft:overworld) to skybox ids
     */
    public Map<String, String> getDimensionSkyboxLinks() {
        return SkyboxEngine.getConfigInstance().getDimensionSkyboxes();
    }

    /**
     * Gets a map of all currently bound biomes.
     * @return A mapping of biome ids (e.g. minecraft:plains) to skybox ids
     */
    public Map<String, String> getBiomeSkyboxLinks() {
        return SkyboxEngine.getConfigInstance().getBiomeSkyboxes();
    }


    /**
     * Gets the full skybox player data of an online player
     * @param player The player you want to get the data for
     */
    public void getSkyboxData(Player player) {
        SkyboxEngine.getPlayerSkyboxManager().getSkyboxData(player);
    }


    /**
     * Adds a new skybox to a player
     * @param player The player you want to add the skybox for
     * @param skybox The skybox you want to add to the player
     */
    public void addSkyboxToPlayer(Player player, ActiveSkybox skybox) {
        SkyboxEngine.getPlayerSkyboxManager().getSkyboxData(player).addActivePlayerSkybox(skybox);
    }


    /**
     * Adds a new skybox to a player
     * @param player The player you want to add the skybox for
     * @param skybox The skybox you want to add to the player
     */
    public void addSkyboxToPlayer(Player player, Settings.SkyboxSettings skybox) {
        SkyboxEngine.getPlayerSkyboxManager().getSkyboxData(player).addActivePlayerSkybox(new ActiveSkybox(skybox));
    }

    /**
     * Adds a new skybox to a player
     * @param player The player you want to add the skybox for
     * @param skybox The skybox you want to add to the player
     * @param reason Allows you to override the saved reason for the skybox
     */
    public void addSkyboxToPlayer(Player player, Settings.SkyboxSettings skybox, SkyboxReason reason) {
        SkyboxEngine.getPlayerSkyboxManager().getSkyboxData(player).addActivePlayerSkybox(new ActiveSkybox(skybox,reason));
    }

    /**
     * Adds a new skybox to a player
     * @param player The player you want to add the skybox for
     * @param skybox The skybox you want to add to the player
     * @param priority The priority of the new skybox, lower values will be displayed over higher ones
     */
    public void addSkyboxToPlayer(Player player, Settings.SkyboxSettings skybox,int priority) {
        SkyboxEngine.getPlayerSkyboxManager().getSkyboxData(player).addActivePlayerSkybox(new ActiveSkybox(skybox,priority));
    }


    /**
     * Removes an active skybox from a player
     * @param player The player you want to remove the skybox from
     * @param skybox The skybox you want to remove from the player
     */
    public void removeSkyboxFromPlayer(Player player, ActiveSkybox skybox) {
        SkyboxEngine.getPlayerSkyboxManager().getSkyboxData(player).removeActiveSkybox(skybox);
    }

    /**
     * Removes an active skybox from a player
     * @param player The player you want to remove the skybox from
     * @param skybox The skybox settings of the skybox you want to remove from the player
     */
    public void removeSkyboxFromPlayer(Player player, Settings.SkyboxSettings skybox) {
        SkyboxEngine.getPlayerSkyboxManager().getSkyboxData(player).removeActiveSkybox(new ActiveSkybox(skybox));
    }
    /**
     * Removes an active skybox from a player
     * @param player The player you want to remove the skybox from
     * @param skybox The skybox id of the skybox you want to remove from the player
     */
    public void removeSkyboxFromPlayer(Player player, String skybox) {
        SkyboxEngine.getPlayerSkyboxManager().getSkyboxData(player).removeActiveSkybox(new ActiveSkybox(getSkyboxSettings(skybox)));
    }
}
