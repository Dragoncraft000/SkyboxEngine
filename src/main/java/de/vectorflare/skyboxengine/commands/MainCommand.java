package de.vectorflare.skyboxengine.commands;

import de.vectorflare.skyboxengine.SkyboxEngine;
import de.vectorflare.skyboxengine.config.Settings;
import de.vectorflare.skyboxengine.util.TextOutputUtil;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.command.CommandSender;

public class MainCommand {

    public MainCommand(){

        new CommandAPICommand("skyboxengine")
                .withSubcommand(new CommandAPICommand("reload")
                        .withPermission("skyboxengine.command.reload")
                        .executes((CommandSender sender, CommandArguments args) -> {
                            SkyboxEngine.getConfigManager().reload();
                            Settings settings = SkyboxEngine.getConfigInstance();
                            TextOutputUtil.sendMiniMessage(sender,true,"Config reloaded");
                            TextOutputUtil.sendMiniMessage(sender,false,"<base>  - Loaded <accent>" + settings.getSkyboxRegistry().size() + "<base> registered skyboxes");
                            TextOutputUtil.sendMiniMessage(sender,false,"<base>  - Loaded <accent>" + settings.getDimensionSkyboxes().size() + "<base> dimension overrides");
                            TextOutputUtil.sendMiniMessage(sender,false,"<base>  - Loaded <accent>" + settings.getBiomeSkyboxes().size() + "<base> biome overrides");

                            SkyboxEngine.getPlayerSkyboxManager().recalculateSkyboxes();
                        })
                ).register();



    }

}
