package de.vectorflare.skyboxengine.manager;

import de.vectorflare.skyboxengine.SkyboxEngine;
import de.vectorflare.skyboxengine.config.ConfigManager;
import de.vectorflare.skyboxengine.config.Settings;
import de.vectorflare.skyboxengine.skybox.ActiveSkybox;
import de.vectorflare.skyboxengine.skybox.PlayerSkybox;
import de.vectorflare.skyboxengine.skybox.SkyboxReason;
import org.bukkit.entity.Player;

import java.util.PriorityQueue;

public class PlayerSkyboxData {

    public final PriorityQueue<ActiveSkybox> playerSkyboxes;
    public Player player;
    public PlayerSkybox renderedPlayerSkybox;

    public PlayerSkyboxData(Player player) {
        this.player = player;
        this.playerSkyboxes = new PriorityQueue<>();
        if (SkyboxEngine.getConfigInstance().getDefaultSkybox() != null && !SkyboxEngine.getConfigInstance().getDefaultSkybox().isBlank()) {
            addDefaultSkybox();
        }
    }

    public void tick() {
        if (renderedPlayerSkybox != null) {
            renderedPlayerSkybox.tickSkybox();
        }

    }


    public void remove() {
        if (renderedPlayerSkybox != null) {
            renderedPlayerSkybox.removeSkybox();
        }

        playerSkyboxes.clear();
    }

    public void addActivePlayerSkybox(ActiveSkybox skybox) {
        playerSkyboxes.add(skybox);
        changeRenderedPlayerSkybox();
    }

    public void removeActiveSkybox(ActiveSkybox skybox) {
        playerSkyboxes.remove(skybox);
        changeRenderedPlayerSkybox();
    }

    public void removeSkybox(Settings.SkyboxSettings settings) {
        PriorityQueue<ActiveSkybox> duplicate = new PriorityQueue<>(playerSkyboxes);
        duplicate.forEach((skybox -> {
            if (skybox.skybox.equals(settings)) {
                playerSkyboxes.remove(skybox);
            }
        }));
        changeRenderedPlayerSkybox();
    }

    public void clearActiveSkyboxes(ActiveSkybox skybox) {
        playerSkyboxes.clear();
        changeRenderedPlayerSkybox();
    }

    public void addDefaultSkybox() {
        for (ActiveSkybox activeSkybox : playerSkyboxes) {
            if (activeSkybox.reason == SkyboxReason.DEFAULT) {
                removeActiveSkybox(activeSkybox);
                break;
            }
        }
        if (ConfigManager.getDefaultSkybox() == null) {
            return;
        }
        addActivePlayerSkybox(new ActiveSkybox(ConfigManager.getDefaultSkybox(), SkyboxReason.DEFAULT,10000));

    }

    public void changeRenderedPlayerSkybox() {
        changeRenderedPlayerSkybox(false);
    }

    public void changeRenderedPlayerSkybox(boolean force) {
        ActiveSkybox skybox = playerSkyboxes.peek();


        if (skybox == null) {
            // Remove Rendered Skybox if none is active now
            if ( renderedPlayerSkybox != null) {
                renderedPlayerSkybox.removeSkybox(0);
                renderedPlayerSkybox = null;
            }
            return;
        }
        // nothing to do rendered Skybox and new Skybox and new skybox have the same settings
        if (!force && renderedPlayerSkybox != null && skybox.skybox == renderedPlayerSkybox.getSettings()) {
            return;
        }
        // Clear Old Skybox if it exists
        if (renderedPlayerSkybox != null) {
            renderedPlayerSkybox.removeSkybox(1);
        }
        // render the new skybox
        renderedPlayerSkybox = new PlayerSkybox(player,skybox.skybox);
        renderedPlayerSkybox.createSkybox();
    }

}
