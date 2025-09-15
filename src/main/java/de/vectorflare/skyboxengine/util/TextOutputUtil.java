package de.vectorflare.skyboxengine.util;

import de.vectorflare.skyboxengine.SkyboxEngine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;

@SuppressWarnings({"unused"})
public class TextOutputUtil {

    public static void broadcast(boolean usePrefix, String message) {
        String prefix = SkyboxEngine.getConfigInstance().getPrefix();
        String finalMessage = usePrefix ? prefix + " " + message : message;
        Bukkit.broadcast(MiniMessage(finalMessage));
    }
    public static void broadcast(String message) {
        Bukkit.broadcast(MiniMessage(message));
    }
    public static void sendMiniMessage(Player player, String message) {
        player.sendMessage(MiniMessage(message));
    }
    public static void sendMiniMessageAsync(Player player, String message) {
        Bukkit.getScheduler().runTaskAsynchronously(SkyboxEngine.getInstance(),() -> player.sendMessage(MiniMessage(message)));
    }

    public static void sendMiniMessage(Player player, boolean usePrefix, String message) {
        String prefix = SkyboxEngine.getConfigInstance().getPrefix();
        String finalMessage = usePrefix ? prefix + " " + message : message;
        player.sendMessage(MiniMessage(finalMessage));
    }

    public static void sendMiniMessageAsync(Player player, boolean usePrefix, String message) {
        Bukkit.getScheduler().runTaskAsynchronously(SkyboxEngine.getInstance(),() -> {
            String prefix = SkyboxEngine.getConfigInstance().getPrefix();
            String finalMessage = usePrefix ? prefix + " " + message : message;
            player.sendMessage(MiniMessage(finalMessage));
        });
    }
    public static void sendMiniMessage(CommandSender sender, String message) {
        sender.sendMessage(MiniMessage(message));
    }

    public static void sendMiniMessageAsync(CommandSender sender, String message) {
        Bukkit.getScheduler().runTaskAsynchronously(SkyboxEngine.getInstance(),() -> sender.sendMessage(MiniMessage(message)));
    }
    public static void sendMiniMessage(CommandSender sender, boolean usePrefix, String message) {
        String prefix = SkyboxEngine.getConfigInstance().getPrefix();
        String finalMessage = usePrefix ? prefix + " " + message : message;
        sender.sendMessage(MiniMessage(finalMessage));
    }
    public static void sendMiniMessageAsync(CommandSender sender, boolean usePrefix, String message) {
        Bukkit.getScheduler().runTaskAsynchronously(SkyboxEngine.getInstance(),() -> {
            String prefix = SkyboxEngine.getConfigInstance().getPrefix();
            String finalMessage = usePrefix ? prefix + " " + message : message;
            sender.sendMessage(MiniMessage(finalMessage));
        });
    }

    public static Component MiniMessage(String message) {
        return net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize(
                message,
                Placeholder.parsed("accent", miniColor(SkyboxEngine.getConfigInstance().getAccentColor())),
                Placeholder.parsed("base",miniColor(SkyboxEngine.getConfigInstance().getBaseColor()))
                );
    }

    public static String miniColor(String color) {
        return "<color:" + color + ">";
    }

    public static String convertLegacyToMiniMessage(String legacyText) {
        LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.builder()
                .character('&')
                .hexCharacter('#')
                .extractUrls()
                .build();
        Component component = legacySerializer.deserialize(legacyText);
        return MiniMessage.miniMessage().serialize(component);
    }

    public static long toTicks(long seconds) {
        return seconds * 20;
    }

    public static void sendTitle(Player player, String title, String subtitle, double fadein, double stay, double fadeout) {
        player.showTitle(Title.title(TextOutputUtil.MiniMessage(title), TextOutputUtil.MiniMessage(subtitle),
                Title.Times.times(
                        Duration.ofMillis((long) (fadein * 1000)),
                        Duration.ofMillis((long) (stay * 1000)),
                        Duration.ofMillis((long) (fadeout * 1000))
                )));
    }
    public static void sendTitleAsync(Player player, String title, String subtitle, double fadein, double stay, double fadeout) {
        Bukkit.getScheduler().runTaskAsynchronously(SkyboxEngine.getInstance(),() -> player.showTitle(Title.title(TextOutputUtil.MiniMessage(title), TextOutputUtil.MiniMessage(subtitle),
                Title.Times.times(
                        Duration.ofMillis((long) (fadein * 1000)),
                        Duration.ofMillis((long) (stay * 1000)),
                        Duration.ofMillis((long) (fadeout * 1000))
                ))));
    }

    public static void sendTitle(Player player, String title, double fadein, double stay, double fadeout) {
        player.showTitle(Title.title(
                TextOutputUtil.MiniMessage(title),
                TextOutputUtil.MiniMessage(""),
                Title.Times.times(
                        Duration.ofMillis((long) (fadein * 1000)),
                        Duration.ofMillis((long) (stay * 1000)),
                        Duration.ofMillis((long) (fadeout * 1000))
                )
        ));
    }

    public static void sendTitleAsync(Player player, String title, double fadein, double stay, double fadeout) {
        Bukkit.getScheduler().runTaskAsynchronously(SkyboxEngine.getInstance(),() -> player.showTitle(Title.title(
                TextOutputUtil.MiniMessage(title),
                TextOutputUtil.MiniMessage(""),
                Title.Times.times(
                        Duration.ofMillis((long) (fadein * 1000)),
                        Duration.ofMillis((long) (stay * 1000)),
                        Duration.ofMillis((long) (fadeout * 1000))
                )
        )));
    }

    public static void sendTitle(Player player, String title, String subtitle) {
        player.showTitle(Title.title(TextOutputUtil.MiniMessage(title), TextOutputUtil.MiniMessage(subtitle),
                Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(1), Duration.ofSeconds(1))));
    }
    public static void sendActionbar(Player player,String message) {
        player.sendActionBar(TextOutputUtil.MiniMessage(message));
    }

    public static void sendActionbarAsync(Player player,String message) {
        Bukkit.getScheduler().runTaskAsynchronously(SkyboxEngine.getInstance(),() -> player.sendActionBar(TextOutputUtil.MiniMessage(message)));
    }
}
