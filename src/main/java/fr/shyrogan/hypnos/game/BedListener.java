package fr.shyrogan.hypnos.game;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.plugin.Plugin;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static java.lang.Math.max;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static org.bukkit.Bukkit.broadcast;
import static org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult.OK;
import static org.bukkit.event.world.TimeSkipEvent.SkipReason.NIGHT_SKIP;

public record BedListener(Plugin plugin) implements Listener {

    @SuppressWarnings("ConstantConditions")
    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent e) {
        if (e.getBedEnterResult() != OK) return;

        final var config = plugin.getConfig();
        final var playersCount = e.getPlayer().getWorld()
                                  .getPlayers()
                                  .size();
        final var playersInBed = e.getPlayer().getWorld()
                                  .getPlayers()
                                  .stream()
                                  .filter(Player::isSleeping)
                                  .count() + 1;
        final var playersRequired = (int) max(
                (playersCount * config.getInt("players_in_bed_percentage") / 100.0),
                1
        );

        String msg;
        if (playersRequired >= playersCount) {
            final var value = config.getLong("night_duration.value");
            final var unit  = config.getString("night_duration.unit").toUpperCase();

            // Begins accelerating night time.
            new AccelerateNightTask(
                    plugin, e.getPlayer().getWorld(),
                    Duration.of(value, ChronoUnit.valueOf(unit))
            );

            msg = config.getString("messages.skipping_night");
        } else {
            msg = config.getString("messages.player_in_bed");
        }

        if (msg.isEmpty() || msg.isBlank()) return;
        msg = msg.replaceAll("%player_name%", e.getPlayer().getName())
                 .replaceAll("%player_display_name%", e.getPlayer().getName())
                 .replaceAll("%sleeping_count%", String.valueOf(playersInBed))
                 .replaceAll("%required_sleeping_count%", String.valueOf(playersRequired));
        broadcast(miniMessage().deserialize(msg));
    }

    @EventHandler
    public void onSkipTime(TimeSkipEvent e) {
        // The night cannot be skipped by bed anymore
        if (e.getSkipReason() == NIGHT_SKIP) e.setCancelled(true);
    }

    @EventHandler
    public void onLeaveBed(PlayerBedLeaveEvent e) {
        // Force player to not leave bed during the animation
        if (AccelerateNightTask.IS_ACTIVE) e.setCancelled(true);
    }

}
