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
                )
                .withSubcommand(SkyboxCommands.getInfoCommand()).withPermission("skyboxengine.command.info")
                .withSubcommand(SkyboxCommands.getEnableCommand()).withPermission("skyboxengine.command.enable")
                .withSubcommand(SkyboxCommands.getDisableCommand()).withPermission("skyboxengine.command.disable")
                .withSubcommand(SkyboxCommands.getReplaceCommand()).withPermission("skyboxengine.command.replace")
                .withPermission("skyboxengine.command")
                .executes((sender, args) -> {
                    TextOutputUtil.sendMiniMessage(sender,true,"Hello and thanks for using my plugin!");
                    TextOutputUtil.sendMiniMessage(sender,"<base>This Plugin only works together with a resource pack to supply the skyboxes. You can take a look at <accent><hover:show_text:'Click for more Information'><click:open_url:https://modrinth.com/resourcepack/skyboxtemplate>this resource pack</click></hover><base> to help you get started with creating your own custom skyboxes.");
                    TextOutputUtil.sendMiniMessage(sender,"<base>You can register skybox effects through the config");
                    String credits = "<hover:show_text:'Click for more Information'><click:open_url:https://vectorflare.de><gradient:#AD0D2D:#EF5E2D>Dragoncraft</gradient> / <gradient:#7294ef:#434d86>Vectorflare</gradient></click>";
                    TextOutputUtil.sendMiniMessage(sender,"  <base>- <accent>" + credits);
                })
                .register();



    }

}
