package de.seine_eloquenz.spigot_pacman_service.source;

/**
 * Exception indicating the searched plugin could not be found
 */
public class PluginNotFoundException extends Exception {

    public PluginNotFoundException(String name) {
        super("Plugin " + name + " not found.");
    }
}
