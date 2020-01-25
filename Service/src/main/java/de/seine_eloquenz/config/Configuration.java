package de.seine_eloquenz.config;

import de.seine_eloquenz.spigot_pacman_service.ServerType;

import java.io.File;

public interface Configuration {

    /**
     * Returns the type of the server
     * @return type
     */
    ServerType serverType();

    /**
     * Returns the server arguments from configuration
     * @return server arguments
     */
    String[] serverArguments();

    /**
     * Returns the server jar file
     * @return server jar file
     */
    File serverJar();
}
