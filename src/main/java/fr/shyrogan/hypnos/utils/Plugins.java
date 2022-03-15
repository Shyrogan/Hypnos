package fr.shyrogan.hypnos.utils;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/**
 * A bench of utilities used inside the {@link Plugin} class.
 */
public class Plugins {

    /**
     * Registers given {@link Listener}s to Bukkit.
     *
     * @param plugin The plugin
     * @param listeners The listeners
     */
    public static void listeners(Plugin plugin, Listener... listeners) {
        for(Listener l: listeners)
            Bukkit.getServer().getPluginManager().registerEvents(l, plugin);
    }

}
