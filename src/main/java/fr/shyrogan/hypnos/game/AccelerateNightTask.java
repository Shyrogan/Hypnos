package fr.shyrogan.hypnos.game;

import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;

import static fr.shyrogan.hypnos.utils.GameConstants.TICK_DURATION_MS;
import static fr.shyrogan.hypnos.utils.GameConstants.WAKING_UP_TIME;

public final class AccelerateNightTask extends BukkitRunnable {

    public static boolean IS_ACTIVE = false;

    private final World   world;
    private final int     randomTickSpeed;
    private final boolean daylightCycle;
    private final long    speed;

    /**
     * Creates and registers a {@link AccelerateNightTask} which accelerates
     * the night to make it last given duration.
     *
     * @param duration The {@link Duration}
     */
    @SuppressWarnings("ConstantConditions")
    public AccelerateNightTask(Plugin plugin, World world, Duration duration) {
        this.world = world;
        this.randomTickSpeed = world.getGameRuleValue(GameRule.RANDOM_TICK_SPEED);
        this.daylightCycle = world.getGameRuleDefault(GameRule.DO_DAYLIGHT_CYCLE);
        final var NIGHT_DURATION_TICK = WAKING_UP_TIME - world.getFullTime();
        this.speed = NIGHT_DURATION_TICK / (duration.toMillis() / TICK_DURATION_MS);
        this.world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        this.world.setGameRule(GameRule.RANDOM_TICK_SPEED, (int) (NIGHT_DURATION_TICK / speed * randomTickSpeed));

        runTaskTimer(plugin, 0L, 1L);
        IS_ACTIVE = true;
    }

    @Override
    public void run() {
        // We don't do anything, the night is over.
        if (world.getTime() <= speed) {
            cancel();
            world.setGameRule(GameRule.RANDOM_TICK_SPEED, randomTickSpeed);
            this.world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, daylightCycle);
            IS_ACTIVE = false;

            return;
        }
        // Accelerates the night
        world.setFullTime(world.getFullTime() + speed);
    }

}
