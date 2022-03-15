package fr.shyrogan.hypnos;

import fr.shyrogan.hypnos.command.HypnosCommandExecutor;
import fr.shyrogan.hypnos.game.BedListener;
import org.bukkit.plugin.java.JavaPlugin;

import static fr.shyrogan.hypnos.utils.Plugins.listeners;

public class Hypnos extends JavaPlugin {

    private static Hypnos hypnos;

    /**
     * Returns the {@link Hypnos} singleton.
     *
     * @return The singleton.
     */
    public static Hypnos hypnos() {
        return hypnos;
    }

    /**
     * Enables the plugin.
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("Successfully loaded configuration.");

        listeners(this, new BedListener(this));
        getCommand("hypnos").setExecutor(new HypnosCommandExecutor());
        getLogger().info("Successfully enabled Hypnos.");
    }

    @Override
    public void onLoad() {
        hypnos = this;
    }
}
