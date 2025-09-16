package de.vectorflare.skyboxengine.commands;

import de.vectorflare.skyboxengine.SkyboxEngine;
import de.vectorflare.skyboxengine.config.ConfigManager;
import de.vectorflare.skyboxengine.config.Settings;
import de.vectorflare.skyboxengine.manager.PlayerSkyboxData;
import de.vectorflare.skyboxengine.skybox.ActiveSkybox;
import de.vectorflare.skyboxengine.skybox.SkyboxReason;
import de.vectorflare.skyboxengine.util.TextOutputUtil;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Objects;

public class SkyboxCommands {


    public SkyboxCommands() {

    }



    public static String[] getActiveSkyboxes(Player player) {
        return SkyboxEngine.getPlayerSkyboxManager().getSkyboxData(player).playerSkyboxes.stream().map(s -> SkyboxEngine.getConfigInstance().getSkyboxRegistry().entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), s.skybox)).map(Map.Entry::getKey).findFirst().orElse(null)).toArray(String[]::new);
    }

    public static String[] getRegisteredSkyboxes() {

        return SkyboxEngine.getConfigInstance().getSkyboxRegistry().keySet().toArray(String[]::new);
    }

    public static CommandAPICommand getInfoCommand() {
        return new CommandAPICommand("info")
                .withArguments(new StringArgument("skybox").replaceSuggestions(ArgumentSuggestions.strings(getRegisteredSkyboxes())))
                .executes(((commandSender, commandArguments) -> {

                    String skybox = commandArguments.getUnchecked("skybox");

                    Settings.SkyboxSettings settings = ConfigManager.getSkyboxSettings(skybox);
                    if (settings == null) {
                        TextOutputUtil.sendMiniMessage(commandSender,true,"<red>Please specify a valid skybox");
                    }

                    TextOutputUtil.sendMiniMessage(commandSender,true,"<base>Showing Info for <accent>" + skybox);

                    TextOutputUtil.sendMiniMessage(commandSender,false,"  <base>Shader Model: <accent>" + settings.getSkyboxId());
                    if (settings.getFlags() != null && !settings.getFlags().isEmpty()) {
                        TextOutputUtil.sendMiniMessage(commandSender, "  <base>Flags:");
                        for (String key : settings.getFlags().keySet()) {
                            TextOutputUtil.sendMiniMessage(commandSender, "   <base>- <accent>" + key + "<base> -><accent> " + settings.getFlag(key));
                        }
                    }

                    //TextOutputUtil.sendMiniMessage(commandSender,true,"<base> - " + settings.ge);
                }));
    }

    public static CommandAPICommand getEnableCommand() {
        return new CommandAPICommand("enable")
                .withArguments(new PlayerArgument("target"))
                .withArguments(new StringArgument("skybox").replaceSuggestions(ArgumentSuggestions.strings(getRegisteredSkyboxes())))
                .withOptionalArguments(new IntegerArgument("priority"))
                .withOptionalArguments(new GreedyStringArgument("forceCheck").withPermission("skyboxengine.command.force").replaceSuggestions(ArgumentSuggestions.strings(new String[]{"force"})))
                .executes((commandSender, commandArguments) -> {
                    Player p = commandArguments.getUnchecked("target");
                    String skybox = commandArguments.getUnchecked("skybox");
                    int priority = commandArguments.getOrDefaultUnchecked("priority",SkyboxEngine.getConfigInstance().getCommandSkyboxPriority());
                    if (priority == -1) {
                        priority = SkyboxEngine.getConfigInstance().getCommandSkyboxPriority();
                    }
                    Settings.SkyboxSettings settings = ConfigManager.getSkyboxSettings(skybox);
                    if (settings == null) {
                        TextOutputUtil.sendMiniMessage(commandSender,true,"<red>Please specify a valid skybox");
                    }


                    PlayerSkyboxData data = SkyboxEngine.getPlayerSkyboxManager().getSkyboxData(p);
                    ActiveSkybox current = null;
                    for (ActiveSkybox as : data.playerSkyboxes.toArray(ActiveSkybox[]::new)) {
                        if (as.skybox.equals(settings)) {
                            current = as;
                        }
                    }

                    boolean forceCheck = commandArguments.getOrDefaultUnchecked("forceCheck","").equals("force");
                    if (!forceCheck && current != null) {
                        TextOutputUtil.sendMiniMessage(commandSender,true,"<red>Registering multiple instances of the same skybox on a player is not supported and may lead to visual artifacts. You can fix this by adding a new skybox configuration with the same settings as this one.");
                        TextOutputUtil.sendMiniMessage(commandSender,"<red>If you wish to continue anyways, add 'force' to the end of the command <i>(You can enter a priority of -1 to continue without overriding the priority)</i>");
                        return;
                    }

                    data.addActivePlayerSkybox(new ActiveSkybox(settings, SkyboxReason.COMMAND, priority));
                    TextOutputUtil.sendMiniMessage(commandSender,true,"<base>Enabled Skybox <accent>" + skybox + "<base> for <accent> " + p.getName());
                });
    }

    public static CommandAPICommand getDisableCommand() {
        return new CommandAPICommand("disable")
                .withArguments(new PlayerArgument("target"))
                .withArguments(new StringArgument("skybox").replaceSuggestions(ArgumentSuggestions.strings(info -> getActiveSkyboxes(info.previousArgs().getUnchecked("target")))))
                .withOptionalArguments(new GreedyStringArgument("forceCheck").withPermission("skyboxengine.command.force").replaceSuggestions(ArgumentSuggestions.strings(new String[]{"force"})))
                .executes((commandSender, commandArguments) -> {
                    Player p = commandArguments.getUnchecked("target");
                    String skybox = commandArguments.getUnchecked("skybox");
                    Settings.SkyboxSettings settings = ConfigManager.getSkyboxSettings(skybox);
                    if (settings == null) {
                        TextOutputUtil.sendMiniMessage(commandSender,true,"<red>Please specify a valid skybox");
                    }

                    PlayerSkyboxData data = SkyboxEngine.getPlayerSkyboxManager().getSkyboxData(p);
                    ActiveSkybox current = null;
                    for (ActiveSkybox as : data.playerSkyboxes.toArray(ActiveSkybox[]::new)) {
                        if (as.skybox.equals(settings)) {
                            current = as;
                        }
                    }

                    if (current == null) {
                        TextOutputUtil.sendMiniMessage(commandSender,true,"<red>Skybox isn't active on player");
                        return;
                    }

                    boolean forceCheck = commandArguments.getOrDefaultUnchecked("forceCheck","").equals("force");
                    if (!forceCheck && ( current.reason == SkyboxReason.BIOME || current.reason == SkyboxReason.DIMENSION)) {
                        TextOutputUtil.sendMiniMessage(commandSender,true,"<red>This Skybox is registered internally, removing these manually is not supported and may lead to visual artifacts. Please add 'force' to the end of the command if you wish to continue anyways");
                        return;
                    }


                    data.removeActiveSkybox(new ActiveSkybox(settings, SkyboxReason.COMMAND, 1));
                    TextOutputUtil.sendMiniMessage(commandSender,true,"<base>Disabled Skybox <accent>" + skybox + "<base> for <accent> " + p.getName());
                });
    }

}
