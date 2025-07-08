package me.dragoncraft.skyboxengine.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("FieldMayBeFinal")
@Configuration
@Getter
public final class Settings {

    private String prefix = "<gray>[<aqua>SkyboxEngine<gray>]";
    @Comment({"1 -> Player skybox changes", "2 -> Skybox Time Color Data"})
    private int debugLogLevel = 0;

    @Comment({"\nDimension Skybox Configurations", "Example Dimension Config:", "  minecraft:overworld:", "    skyboxId: 'ult_effects:model_shader_1'", "    redChannel_time: true", "    doublePrecision_time: true"})
    private Map<String,SkyboxSettings> dimensionSkyboxes = new HashMap<>();

    @Comment("Delay in ticks between checks whether the player is in a new biome")
    private int biomeCheckInterval = 20;
    private Map<String,SkyboxSettings> biomeSkyboxes = new HashMap<>();

    @SuppressWarnings("unused")
    @Configuration
    @Getter
    public static class SkyboxSettings {
        private String skyboxId = "";
        private boolean redChannel_time;
        private boolean doublePrecision_time;
    }

}