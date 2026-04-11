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

import java.util.Collection;
import java.util.List;

public class SkyboxCommands {

    public static String[] getRegisteredSkyboxes() {

        return SkyboxEngine.getConfigInstance().getSkyboxRegistry().keySet().toArray(String[]::new);
    }

    public static  CommandAPICommand getToggleCommand() {
        return new CommandAPICommand("togglevisuals")
                .withArguments(new EntitySelectorArgument.ManyPlayers("target"))
                .withArguments(new BooleanArgument("toggle"))
                .executes((sender,args) -> {
                    Collection<Player> players = args.getUnchecked("target");
                    if (players == null) {
                        if (sender instanceof Player p) {
                            players = List.of(p);
                        } else {
                            TextOutputUtil.sendMiniMessage(sender, true, "<red>Please Specify a target when running from console");
                            return;
                        }
                    }
                    boolean toggle = args.getOrDefaultUnchecked("toggle",true);
                    for (Player p : players) {


                        if (toggle) {
                            if (SkyboxEngine.getData().disabledSkyboxes.contains(p.getUniqueId())) {
                                SkyboxEngine.getData().disabledSkyboxes.remove(p.getUniqueId());
                                SkyboxEngine.getPlayerSkyboxManager().getSkyboxData(p).changeRenderedPlayerSkybox(true);
                                SkyboxEngine.getInstance().saveData();
                            }
                        } else {
                            if (!SkyboxEngine.getData().disabledSkyboxes.contains(p.getUniqueId())) {
                                SkyboxEngine.getData().disabledSkyboxes.add(p.getUniqueId());
                                SkyboxEngine.getPlayerSkyboxManager().getSkyboxData(p).changeRenderedPlayerSkybox(true);
                                SkyboxEngine.getInstance().saveData();
                            }
                        }
                        if (players.size() <= 1) {
                            TextOutputUtil.sendMiniMessage(sender,true,"Skybox visuals for <accent>" + p.getName() + " <base>have been <accent>" + (toggle ? "enabled" : "disabled"));
                        } else {
                            TextOutputUtil.sendMiniMessage(sender,true,"Skybox visuals for <accent> " + players.size() + "<base> players have been <accent>" + (toggle ? "enabled" : "disabled"));
                        }
                    }
                });
    }


    public static CommandAPICommand getInfoCommand() {
        return new CommandAPICommand("info")
                .withArguments(new StringArgument("skybox").replaceSuggestions(ArgumentSuggestions.strings(getRegisteredSkyboxes())))
                .executes(((commandSender, commandArguments) -> {

                    String skybox = commandArguments.getUnchecked("skybox");

                    Settings.SkyboxSettings settings = ConfigManager.getSkyboxSettings(skybox);
                    if (settings == null) {
                        TextOutputUtil.sendMiniMessage(commandSender,true,"<red>Please specify a valid skybox");
                        return;
                    }

                    TextOutputUtil.sendMiniMessage(commandSender,true,"<base>Showing Info for <accent>" + skybox);

                    TextOutputUtil.sendMiniMessage(commandSender,false,"  <base>Shader Model: <accent>" + settings.getSkyboxId());
                    if (settings.getTintProvider() != null && !settings.getTintProvider().isEmpty()) {
                        TextOutputUtil.sendMiniMessage(commandSender, "  <base>Tint Provider: <accent>" + settings.getTintProvider());
                    } else {
                        TextOutputUtil.sendMiniMessage(commandSender, "  <base>Tint Provider: <accent>not specified");
                    }

                    //TextOutputUtil.sendMiniMessage(commandSender,true,"<base> - " + settings.ge);
                }));
    }

    public static CommandAPICommand getEnableCommand() {
        return new CommandAPICommand("enable")
                .withArguments(new EntitySelectorArgument.ManyPlayers("target"))
                .withArguments(new StringArgument("skybox").replaceSuggestions(ArgumentSuggestions.strings(getRegisteredSkyboxes())))
                .withOptionalArguments(new IntegerArgument("priority"))
                .withOptionalArguments(new GreedyStringArgument("forceCheck").withPermission("skyboxengine.command.force").replaceSuggestions(ArgumentSuggestions.strings(new String[]{"force"})))
                .executes((sender, args) -> {
                    List<Player> players = args.getUnchecked("target");
                    if (players == null) {
                        if (sender instanceof Player p) {
                            players = List.of(p);
                        } else {
                            TextOutputUtil.sendMiniMessage(sender, true, "<red>Please Specify a target when running from console");
                            return;
                        }
                    }
                    String skybox = args.getUnchecked("skybox");
                    int priority = args.getOrDefaultUnchecked("priority",SkyboxEngine.getConfigInstance().getCommandSkyboxPriority());
                    if (priority == -1) {
                        priority = SkyboxEngine.getConfigInstance().getCommandSkyboxPriority();
                    }
                    Settings.SkyboxSettings settings = ConfigManager.getSkyboxSettings(skybox);
                    if (settings == null) {
                        TextOutputUtil.sendMiniMessage(sender,true,"<red>Please specify a valid skybox");
                    }
                    boolean internalRegistered = false;
                    int count = 0;
                    for (Player p : players) {
                        PlayerSkyboxData data = SkyboxEngine.getPlayerSkyboxManager().getSkyboxData(p);
                        ActiveSkybox current = null;
                        for (ActiveSkybox as : data.playerSkyboxes.toArray(ActiveSkybox[]::new)) {
                            if (as.skybox.equals(settings)) {
                                current = as;
                            }
                        }

                        boolean forceCheck = args.getOrDefaultUnchecked("forceCheck","").equals("force");
                        if (!forceCheck && current != null) {
                            internalRegistered = true;
                            continue;
                        }

                        count++;
                        data.addActivePlayerSkybox(new ActiveSkybox(settings, SkyboxReason.COMMAND, priority));
                    }
                    if (internalRegistered) {
                        TextOutputUtil.sendMiniMessage(sender,true,"<red>Registering multiple instances of the same skybox on a player is not supported and may lead to visual artifacts. You can fix this by adding a new skybox configuration with the same settings as this one.");
                        TextOutputUtil.sendMiniMessage(sender,"<red>If you wish to continue anyways, add 'force' to the end of the command <i>(You can enter a priority of -1 to continue without overriding the priority)</i>");
                    }
                    if (count == 0) {
                        TextOutputUtil.sendMiniMessage(sender,true,"<base>Nothing to change with current flags");
                    }
                    else if (count <= 1) {
                        TextOutputUtil.sendMiniMessage(sender,true,"<base>Enabled Skybox <accent>" + skybox + "<base> for <accent>" + players.getFirst().getName());
                    } else {
                        TextOutputUtil.sendMiniMessage(sender,true,"<base>Enabled Skybox <accent>" + skybox + "<base> for <accent>" + players.size() + "<base> players");
                    }
                });
    }


    public static CommandAPICommand getReplaceCommand() {
        return new CommandAPICommand("replace")
                .withArguments(new EntitySelectorArgument.ManyPlayers("target"))
                .withArguments(new StringArgument("skybox").replaceSuggestions(ArgumentSuggestions.strings(getRegisteredSkyboxes())))
                .withOptionalArguments(new IntegerArgument("priority"))
                .withOptionalArguments(new GreedyStringArgument("forceCheck").withPermission("skyboxengine.command.force").replaceSuggestions(ArgumentSuggestions.strings(new String[]{"force"})))
                .executes((sender, args) -> {
                    List<Player> players = args.getUnchecked("target");
                    if (players == null) {
                        if (sender instanceof Player p) {
                            players = List.of(p);
                        } else {
                            TextOutputUtil.sendMiniMessage(sender, true, "<red>Please Specify a target when running from console");
                            return;
                        }
                    }
                    String skybox = args.getUnchecked("skybox");
                    int priority = args.getOrDefaultUnchecked("priority",SkyboxEngine.getConfigInstance().getCommandSkyboxPriority());
                    if (priority == -1) {
                        priority = SkyboxEngine.getConfigInstance().getCommandSkyboxPriority();
                    }
                    Settings.SkyboxSettings settings = ConfigManager.getSkyboxSettings(skybox);
                    if (settings == null) {
                        TextOutputUtil.sendMiniMessage(sender,true,"<red>Please specify a valid skybox");
                    }
                    int count = 0;
                    for (Player p : players) {
                        PlayerSkyboxData data = SkyboxEngine.getPlayerSkyboxManager().getSkyboxData(p);
                        for (ActiveSkybox as : data.playerSkyboxes.toArray(ActiveSkybox[]::new)) {
                            if (as.reason == SkyboxReason.COMMAND || as.reason == SkyboxReason.CUSTOM) {
                                data.removeActiveSkybox(new ActiveSkybox(as.skybox, SkyboxReason.COMMAND, 1));
                            }
                        }
                        count++;
                        data.addActivePlayerSkybox(new ActiveSkybox(settings, SkyboxReason.COMMAND, priority));
                    }
                    if (count == 0) {
                        TextOutputUtil.sendMiniMessage(sender,true,"<base>Nothing to change");
                    }
                    else if (count <= 1) {
                        TextOutputUtil.sendMiniMessage(sender,true,"<base>Enabled Skybox <accent>" + skybox + "<base> for <accent>" + players.getFirst().getName());
                    } else {
                        TextOutputUtil.sendMiniMessage(sender,true,"<base>Enabled Skybox <accent>" + skybox + "<base> for <accent>" + players.size() + "<base> players");
                    }
                });
    }

    public static CommandAPICommand getDisableCommand() {
        return new CommandAPICommand("disable")
                .withArguments(new EntitySelectorArgument.ManyPlayers("target"))
                .withArguments(new StringArgument("skybox").replaceSuggestions(ArgumentSuggestions.strings(info -> getRegisteredSkyboxes())))
                .withOptionalArguments(new GreedyStringArgument("forceCheck").withPermission("skyboxengine.command.force").replaceSuggestions(ArgumentSuggestions.strings(new String[]{"force"})))
                .executes((sender, args) -> {
                    List<Player> players = args.getUnchecked("target");
                    if (players == null) {
                        if (sender instanceof Player p) {
                            players = List.of(p);
                        } else {
                            TextOutputUtil.sendMiniMessage(sender, true, "<red>Please Specify a target when running from console");
                            return;
                        }
                    }
                    String skybox = args.getUnchecked("skybox");
                    Settings.SkyboxSettings settings = ConfigManager.getSkyboxSettings(skybox);
                    if (settings == null) {
                        TextOutputUtil.sendMiniMessage(sender,true,"<red>Please specify a valid skybox");
                    }
                    boolean internalRegistered = false;
                    int count = 0;
                    for (Player p : players) {
                        PlayerSkyboxData data = SkyboxEngine.getPlayerSkyboxManager().getSkyboxData(p);
                        ActiveSkybox current = null;
                        for (ActiveSkybox as : data.playerSkyboxes.toArray(ActiveSkybox[]::new)) {
                            if (as.skybox.equals(settings)) {
                                current = as;
                            }
                        }
                        if (current == null) {
                            continue;
                        }

                        boolean forceCheck = args.getOrDefaultUnchecked("forceCheck","").equals("force");
                        if (!forceCheck && ( current.reason == SkyboxReason.BIOME || current.reason == SkyboxReason.DIMENSION)) {
                            internalRegistered = true;
                            continue;
                        }

                        count++;
                        data.removeActiveSkybox(new ActiveSkybox(settings, SkyboxReason.COMMAND, 1));
                    }
                    if (internalRegistered) {
                        TextOutputUtil.sendMiniMessage(sender,true,"<red>This Skybox is registered internally, removing these manually is not supported and may lead to visual artifacts. Please add 'force' to the end of the command if you wish to continue anyways");
                    }
                    if (count == 0) {
                        TextOutputUtil.sendMiniMessage(sender,true,"<base>Nothing to change with current flags");
                    }
                    else if (count <= 1) {
                        TextOutputUtil.sendMiniMessage(sender,true,"<base>Disabled Skybox <accent>" + skybox + "<base> for <accent>" + players.getFirst().getName());
                    } else {
                        TextOutputUtil.sendMiniMessage(sender,true,"<base>Disabled Skybox <accent>" + skybox + "<base> for <accent>" + players.size() + "<base> players");
                    }
                });
    }

}
