package de.seine_eloquenz.spigot_pacman_service.cmd;

import de.seine_eloquenz.spigot_pacman_service.SpigotPacman;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ServerUpgrade extends Command {

    public ServerUpgrade(final SpigotPacman pacman) {
        super(pacman);
    }

    @Override
    public String getName() {
        return "server-upgrade";
    }

    @Override
    void executeLogic(final String... args) {
        if (args.length > 0) {
            if (args[0].equals("-h")) {
                System.out.println("Use server-upgrade -v <version> Where version is minecraft version or latest for" +
                        "for latest stable build!");
            }
            if (args.length == 2) {
                if (args[0].equals("-v")) {
                    File server = pacman.buildServer(args[1]);
                    try {
                        pacman.getServer().stop();
                        System.out.println("Sending stop command to server...");
                        pacman.getServer().awaitShutdown();
                        System.out.println("Copying server jar");
                        Files.copy(server.toPath(), pacman.serverJar().toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Restarting server...");
                        pacman.getServer().start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
