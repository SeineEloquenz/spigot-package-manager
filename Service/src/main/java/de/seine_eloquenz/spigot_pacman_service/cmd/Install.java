package de.seine_eloquenz.spigot_pacman_service.cmd;

import de.seine_eloquenz.spigot_pacman_service.Downloader;
import de.seine_eloquenz.spigot_pacman_service.SpigotPacman;
import de.seine_eloquenz.spigot_pacman_service.source.PluginNotFoundException;
import de.seine_eloquenz.spigot_pacman_service.source.Resource;
import de.seine_eloquenz.spigot_pacman_service.util.Terminal;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Install extends Command {

    public Install(final SpigotPacman pacman) {
        super(pacman);
    }

    @Override
    public String getName() {
        return "install";
    }

    @Override
    protected void executeLogic(final String... args) {
        if (args.length >= 1) {
            try {
                String query = String.join(" ", args);
                List<Resource> result = pacman().getSource().searchForPackage(query);
                if (result.isEmpty()) {
                    System.out.println("Could not find plugin.");
                    return;
                }
                Resource plugin = result.get(0);
                if (plugin.isInstalled()) {
                    System.out.println("Plugin is already installed, please upgrade instead");
                } else {
                    System.out.println("Found plugin " + plugin.getName());
                    if (Terminal.askYesNo("Do you wish to install it?")) {
                        File pluginFile = pacman().getSource().downloadPlugin(plugin);
                        System.out.println("Plugin will be installed on next restart.");
                    }
                }
            } catch (IOException | PluginNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
