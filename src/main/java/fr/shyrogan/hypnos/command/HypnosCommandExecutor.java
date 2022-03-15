package fr.shyrogan.hypnos.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fr.shyrogan.hypnos.Hypnos.hypnos;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public class HypnosCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender, @NotNull Command command,
            @NotNull String label, @NotNull String[] args
    ) {
        if (args.length >= 1 && "reload".equalsIgnoreCase(args[0]) && sender.hasPermission("hypnos.reload")) {
            hypnos().reloadConfig();
            sender.sendMessage("Configuration reloaded!");
        } else {
            sender.sendMessage(miniMessage().deserialize(
                            "<gradient:#5e4fa2:#f79459:red>Hypnos version " + hypnos().getDescription()
                                                                                      .getVersion()
                            + " developed by "
                            + String.join(", ", hypnos().getDescription().getAuthors())
                    )
            );
        }

        return true;
    }
}
