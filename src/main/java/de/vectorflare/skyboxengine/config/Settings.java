package de.vectorflare.skyboxengine.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import de.exlll.configlib.Ignore;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("FieldMayBeFinal")
@Configuration
@Getter
public final class Settings {

    private String prefix = "<base>[<gradient:aqua:light_purple>SkyboxEngine</gradient><base>]";
    private String baseColor = "#aaa8aa";
    private String accentColor = "aqua";

    @Comment({"1 -> Player skybox changes", "2 -> Skybox Time Color Data"})
    private int debugLogLevel = 0;
    private Map<String,SkyboxSettings> skyboxRegistry = new HashMap<>();

    @Comment("Lowest Priority Skyboxes always applied if no other is active, leave empty to disable")
    private String defaultSkybox = "";


    @Comment("Maps Dimension Keys (e.g minecraft:overworld) to registered skyboxes")
    private Map<String,String> dimensionSkyboxes = new HashMap<>();

    @Comment("Maps Biome Keys (e.g minecraft:forest) to registered skyboxes")
    private Map<String,String> biomeSkyboxes = new HashMap<>();
    private int biomeCheckInterval = 20;


    @Comment("Configurable to solve Conflicts with skyboxes activated through the API")
    private int dimensionSkyboxPriority = 150;
    private int biomeSkyboxPriority = 100;
    private int commandSkyboxPriority = 50;





    @SuppressWarnings("unused")
    @Configuration
    @Getter
    public static class SkyboxSettings {
        private String skyboxId = "";
        @Ignore
        private String registryName = "";
        private Map<String,Boolean> flags;

        public boolean getFlag(String key) {
            if (flags == null) {
                return false;
            }
            return flags.getOrDefault(key,false);
        }


    }

}