package de.vectorflare.skyboxengine;

import com.github.retrooper.packetevents.PacketEvents;
import de.vectorflare.skyboxengine.commands.MainCommand;
import de.vectorflare.skyboxengine.config.ConfigManager;
import de.vectorflare.skyboxengine.config.Settings;
import de.vectorflare.skyboxengine.listener.BiomeSkyboxListener;
import de.vectorflare.skyboxengine.listener.MainListener;
import de.vectorflare.skyboxengine.listener.WorldSkyboxListener;
import de.vectorflare.skyboxengine.manager.PlayerSkyboxManager;
import de.vectorflare.skyboxengine.tintcolor.TintProviders;
import de.vectorflare.skyboxengine.tintcolor.premade.CombinedTimeHeightProvider;
import de.vectorflare.skyboxengine.tintcolor.premade.LowPrecisionTimeProvider;
import de.vectorflare.skyboxengine.tintcolor.premade.PlayerHeightProvider;
import de.vectorflare.skyboxengine.tintcolor.premade.TimeProvider;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import lombok.Getter;
import lombok.Setter;
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

    @Getter
    private static TintProviders tintProviders;

    static SkyboxAPI api;

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
        api = new SkyboxAPI();
        if (!loadConfig()) {
            instance.getServer().getPluginManager().disablePlugin(instance);
            return;
        }

        CommandAPI.onEnable();
        SpigotEntityLibPlatform platform = new SpigotEntityLibPlatform(this);
        APIConfig settings = new APIConfig(PacketEvents.getAPI());
        EntityLib.init(platform, settings);

        playerSkyboxManager = new PlayerSkyboxManager();
        tintProviders = new TintProviders();

        SkyboxAPI.getAPI().registerTintProvider(new TimeProvider());
        SkyboxAPI.getAPI().registerTintProvider(new LowPrecisionTimeProvider());
        SkyboxAPI.getAPI().registerTintProvider(new PlayerHeightProvider());
        SkyboxAPI.getAPI().registerTintProvider(new CombinedTimeHeightProvider());

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling Skybox Engine");
        CommandAPI.onDisable();
    }

    private void registerCommands() {
        new MainCommand();
    }

    private void registerListeners() {
        registerListener(new MainListener());
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
