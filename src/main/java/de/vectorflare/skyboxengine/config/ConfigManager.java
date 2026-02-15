package de.vectorflare.skyboxengine.config;

import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurations;
import de.vectorflare.skyboxengine.SkyboxEngine;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Getter
public class ConfigManager {

    private final SkyboxEngine plugin;
    private Settings settings;

    public ConfigManager(SkyboxEngine plugin) {
        this.plugin = plugin;
    }

    @NotNull
    public Optional<Throwable> loadConfig() {
        final YamlConfigurationProperties properties = createConfigProperties();
        final File settingsFile = new File(plugin.getDataFolder(), "config.yml");

        if (!settingsFile.exists()) {
            settings = new Settings();

            settings.getSkyboxRegistry().put("texture",new Settings.SkyboxSettings("skyboxengine:model_shader_9_texture",null,false));
            settings.getSkyboxRegistry().put("vortex",new Settings.SkyboxSettings("skyboxengine:model_shader_6",null,false));
            settings.getSkyboxRegistry().put("ocean",new Settings.SkyboxSettings("skyboxengine:model_shader_5","daytime",false));
            settings.getSkyboxRegistry().put("raymarching",new Settings.SkyboxSettings("skyboxengine:model_shader_4",null,false));
            settings.getSkyboxRegistry().put("animation",new Settings.SkyboxSettings("skyboxengine:model_shader_3",null,false));
            settings.getSkyboxRegistry().put("noise",new Settings.SkyboxSettings("skyboxengine:model_shader_2",null,false));
            settings.getSkyboxRegistry().put("gradient",new Settings.SkyboxSettings("skyboxengine:model_shader_1",null,false));

            settings.getDimensionSkyboxes().put("minecraft:the_end","vortex");
            settings.getBiomeSkyboxes().put("minecraft:ocean","ocean");
            settings.getBiomeSkyboxes().put("minecraft:forest","gradient");
            settings.getBiomeSkyboxes().put("minecraft:plains","texture");

            saveYamlConfiguration(settingsFile,settings);
            return Optional.empty();
        }
        try {
            settings = YamlConfigurations.update(
                    settingsFile.toPath(),
                    Settings.class,
                    properties
            );
            SkyboxEngine.setConfigInstance(settings);
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of(e);
        }
    }

    public static YamlConfigurationProperties createConfigProperties() {
        return YamlConfigurationProperties.newBuilder()
                .charset(StandardCharsets.UTF_8)
                .outputNulls(false)
                .inputNulls(false)
                .build();
    }

    public void reload() {
        settings = YamlConfigurations.load(new File(plugin.getDataFolder(), "config.yml").toPath(), Settings.class,createConfigProperties());
        SkyboxEngine.setConfigInstance(settings);
        SkyboxEngine.info("Loaded " + settings.getSkyboxRegistry().size() + " registered skyboxes");
        SkyboxEngine.info("Loaded " + settings.getDimensionSkyboxes().size() + " dimension overrides");
        SkyboxEngine.info("Loaded " + settings.getBiomeSkyboxes().size() + " biome overrides");


    }

    public static Settings.SkyboxSettings getSkyboxSettings(String id) {
        if (!SkyboxEngine.getConfigInstance().getSkyboxRegistry().containsKey(id)) {
            return null;
        }
        return SkyboxEngine.getConfigInstance().getSkyboxRegistry().get(id);
    }

    public static Settings.SkyboxSettings getDefaultSkybox() {
        String defaultSkybox = SkyboxEngine.getConfigInstance().getDefaultSkybox();
        if (defaultSkybox == null) {
            return  null;
        }
        if (SkyboxEngine.getConfigInstance().getDefaultSkybox().isBlank()) {
            return null;
        }
        return getSkyboxSettings(defaultSkybox);
    }

    public <T> T loadYamlConfiguration(File file, Class<T> clazz) {
        return YamlConfigurations.load(file.toPath(), clazz, createConfigProperties());
    }

    public <T> void saveYamlConfiguration(File file, T configuration) {
        YamlConfigurations.save(file.toPath(), (Class<T>) configuration.getClass(), configuration, createConfigProperties());
    }
}
