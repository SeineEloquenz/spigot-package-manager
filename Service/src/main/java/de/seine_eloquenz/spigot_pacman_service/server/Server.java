package de.seine_eloquenz.spigot_pacman_service.server;

import java.io.IOException;

public interface Server {

    /**
     * Checks if the server is running
     * @return true if server is running
     */
    boolean isRunning();

    /**
     * Stops the server
     */
    void stop();

    /**
     * Starts the server
     * @throws IOException if io error occurs
     */
    void start() throws IOException;

    /**
     * Block until server shuts down
     */
    void awaitShutdown();
}
