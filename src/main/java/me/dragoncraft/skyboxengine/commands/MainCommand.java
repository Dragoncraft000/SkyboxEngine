package me.dragoncraft.skyboxengine.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import me.dragoncraft.skyboxengine.SkyboxEngine;
import me.dragoncraft.skyboxengine.util.TextOutputUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class MainCommand {

    public MainCommand(){

        new CommandAPICommand("skyboxengine")
                .withSubcommand(new CommandAPICommand("reload")
                        .withPermission("skyboxengine.command.reload")
                        .executes((CommandSender sender, CommandArguments args) -> {
                            SkyboxEngine.getConfigManager().reload();
                            TextOutputUtil.sendMiniMessage(sender,true,"Config reloaded");
                            Bukkit.getOnlinePlayers().forEach(p -> SkyboxEngine.getPlayerSkyboxManager().checkWorldSkyboxChange(p,p.getWorld(),p.getWorld()));
                        })
                ).register();



    }

}
