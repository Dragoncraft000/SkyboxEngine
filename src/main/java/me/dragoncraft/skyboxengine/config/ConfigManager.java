package me.dragoncraft.skyboxengine.config;

import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurations;
import lombok.Getter;
import me.dragoncraft.skyboxengine.SkyboxEngine;
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
                .outputNulls(true)
                .inputNulls(false)
                .build();
    }

    public void reload() {
        settings = YamlConfigurations.load(new File(plugin.getDataFolder(), "config.yml").toPath(), Settings.class,createConfigProperties());
        SkyboxEngine.setConfigInstance(settings);
        SkyboxEngine.info("Loaded " + settings.getDimensionSkyboxes().size() + " skybox overrides");
        settings.getDimensionSkyboxes().forEach((key,value) -> {
            SkyboxEngine.info(" - " + key + " -> " + value.getSkyboxId() + (value.isRedChannel_time() ? " (Time Sync)" : ""));
        });
    }

    public <T> T loadYamlConfiguration(File file, Class<T> clazz) {
        return YamlConfigurations.load(file.toPath(), clazz, createConfigProperties());
    }


    public <T> void saveYamlConfiguration(File file, T configuration) {
        final YamlConfigurationProperties properties = createConfigProperties();
        YamlConfigurations.save(file.toPath(), (Class<T>) configuration.getClass(), configuration, createConfigProperties());
    }
}
