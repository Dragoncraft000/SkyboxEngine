package de.vectorflare.skyboxengine.skybox;

import de.vectorflare.skyboxengine.config.Settings;

public class ActiveSkybox implements Comparable<ActiveSkybox> {

    public final Settings.SkyboxSettings skybox;
    public final SkyboxReason reason;
    public final int priority;
    public ActiveSkybox(Settings.SkyboxSettings skybox,SkyboxReason reason,int priority) {
        this.skybox = skybox;
        this.reason = reason;
        this.priority = priority;
    }

    public ActiveSkybox(Settings.SkyboxSettings skybox,int priority) {
        this.skybox = skybox;
        this.reason = SkyboxReason.CUSTOM;
        this.priority = priority;
    }

    public ActiveSkybox(Settings.SkyboxSettings skybox) {
        this.skybox = skybox;
        this.reason = SkyboxReason.CUSTOM;
        this.priority = 1;
    }
    public ActiveSkybox(Settings.SkyboxSettings skybox,SkyboxReason reason) {
        this.skybox = skybox;
        this.reason = reason;
        this.priority = 1;
    }

    @Override
    public int compareTo(ActiveSkybox o) {
        if (o == null) {
            return 1;
        }
        return Integer.compare(this.priority, o.priority);
    }

}
