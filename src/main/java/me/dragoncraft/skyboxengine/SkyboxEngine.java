package me.dragoncraft.skyboxengine;

import com.github.retrooper.packetevents.PacketEvents;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import lombok.Getter;
import lombok.Setter;
import me.dragoncraft.skyboxengine.commands.MainCommand;
import me.dragoncraft.skyboxengine.config.ConfigManager;
import me.dragoncraft.skyboxengine.config.Settings;
import me.dragoncraft.skyboxengine.listener.BiomeSkyboxListener;
import me.dragoncraft.skyboxengine.listener.WorldSkyboxListener;
import me.dragoncraft.skyboxengine.manager.PlayerSkyboxManager;
import me.tofaa.entitylib.APIConfig;
import me.tofaa.entitylib.EntityLib;
import me.tofaa.entitylib.spigot.SpigotEntityLibPlatform;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.logging.Logger;

public final class SkyboxEngine extends JavaPlugin {

    private static Logger logger;

    @Getter
    private static SkyboxEngine instance;
    @Getter
    private static ConfigManager configManager;
    @Getter @Setter
    private static Settings configInstance;

    @Getter
    private static PlayerSkyboxManager playerSkyboxManager;


    @Override
    public void onLoad() {
        CommandAPIBukkitConfig commandAPIBukkitConfig = new CommandAPIBukkitConfig(this).silentLogs(true);
        commandAPIBukkitConfig.skipReloadDatapacks(true);

        CommandAPI.onLoad(commandAPIBukkitConfig.verboseOutput(false));
    }

    public static void info(String message) {
        if (logger != null) {
            logger.info(message);
        }
    }

    @SuppressWarnings("unused")
    public static void warning(String message) {
        if (logger != null) {
            logger.warning(message);
        }
    }

    public static void severe(String message) {
        if (logger != null) {
            logger.severe(message);
        }
    }

    public static void registerListener(Listener listener) {
        instance.getServer().getPluginManager().registerEvents(listener,instance);
    }


    @Override
    public void onEnable() {
        logger = this.getLogger();

        instance = this;
        configManager = new ConfigManager(this);

        if (!loadConfig()) {
            instance.getServer().getPluginManager().disablePlugin(instance);
            return;
        }

        CommandAPI.onEnable();
        SpigotEntityLibPlatform platform = new SpigotEntityLibPlatform(this);
        APIConfig settings = new APIConfig(PacketEvents.getAPI()).tickTickables().trackPlatformEntities().useBstats().usePlatformLogger();
        EntityLib.init(platform, settings);

        playerSkyboxManager = new PlayerSkyboxManager();

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling Skybox Engine");
        saveDefaultConfig();
        CommandAPI.onDisable();
    }

    private void registerCommands() {
        new MainCommand();
    }

    private void registerListeners() {
        registerListener(new WorldSkyboxListener());
        registerListener(new BiomeSkyboxListener());
    }

    /**
     * Loads the plugin configuration.
     *
     * @return true if the configuration was loaded successfully, false otherwise.
     */
    private boolean loadConfig() {
        final Optional<Throwable> error = configManager.loadConfig();
        if (error.isPresent()) {
            instance.getLogger().log(java.util.logging.Level.SEVERE, "Failed to load configuration", error.get());
            return false;
        }
        configManager.reload();
        return true;
    }
}
