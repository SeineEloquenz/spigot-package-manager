package de.seine_eloquenz.spigot_pacman_service.cmd;

import de.seine_eloquenz.spigot_pacman_service.SpigotPacman;
import de.seine_eloquenz.spigot_pacman_service.source.PluginNotFoundException;
import de.seine_eloquenz.spigot_pacman_service.source.Resource;
import de.seine_eloquenz.spigot_pacman_service.source.Source;
import de.seine_eloquenz.spigot_pacman_service.util.ArrayUtils;
import de.seine_eloquenz.spigot_pacman_service.util.Terminal;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Upgrade extends Command {

    public Upgrade(final SpigotPacman pacman) {
        super(pacman);
    }

    @Override
    public String getName() {
        return "upgrade";
    }

    @Override
    protected void executeLogic(final String... args) {
        if (args.length > 0) {
            switch (args[0]) {
                case "-a": {
                    Source source = pacman().getSource();
                    for (Resource resource : pacman().getInstalledResources()) {
                        try {
                            source.downloadPlugin(resource);
                        } catch (IOException e) {
                            System.out.println("IO Error occured during upgrade of " + resource.getName());
                        } catch (PluginNotFoundException e) {
                            System.out.println("Plugin " + resource.getName() + " not found, Upgrade failed.");
                        }
                    }
                    break;
                }
                case "-p": {
                    if (args.length > 1) {
                        try {
                            String query = String.join(" ", ArrayUtils.cutFirstParam(args));
                            List<Resource> result = pacman().getSource().searchForPackage(query);
                            if (result.isEmpty()) {
                                System.out.println("Could not find plugin.");
                                return;
                            }
                            Resource plugin = result.get(0);
                            if (pacman().isInstalled(plugin)) {
                                if (Terminal.askYesNo("Do you wish to upgrade?")) {
                                    File pluginFile = pacman().getSource().downloadPlugin(plugin);
                                    System.out.println("Plugin will be upgraded on next restart.");
                                }
                            } else {
                                System.out.println("Plugin not installed. Please install it first");
                            }
                        } catch (IOException | PluginNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Too many arguments. Use " + getName() + " -p <plugin name>");
                    }
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }
}
