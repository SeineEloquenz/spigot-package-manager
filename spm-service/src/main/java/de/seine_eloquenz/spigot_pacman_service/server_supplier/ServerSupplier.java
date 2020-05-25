package de.seine_eloquenz.spigot_pacman_service.server_supplier;

import de.seine_eloquenz.spigot_pacman_service.ServerType;
import de.seine_eloquenz.spigot_pacman_service.SpigotPacman;

import java.io.File;
import java.io.IOException;

public interface ServerSupplier {

    String BUILD_TARGET_DIR = SpigotPacman.HOME_DIR + File.separator + "build-target";

    static ServerSupplier build(ServerType type) {
        switch (type) {
            case paper: {
                return new PaperSupplier();
            }
            case spigot: {
                return new BuildToolsSupplier();
            }
            default: {
                throw new IllegalArgumentException("There is no server supplier available for the used type!");
            }
        }
    }

    /**
     * Builds the server with the given version
     * @param version the desired server version
     * @return the file the jar was saved to
     * @throws IOException if an IO Error occurs while building. The sources of these Exceptions are very much
     * implementation dependent
     * @throws InterruptedException If the thread gets interrupted
     */
    File buildServer(String version) throws IOException, InterruptedException;
}
