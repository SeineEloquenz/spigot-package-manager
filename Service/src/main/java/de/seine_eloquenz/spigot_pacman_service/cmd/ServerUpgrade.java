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
            if (args.length == 2) {
                if (args[0].equals("-v")) {
                    File server = pacman.buildServer(args[1]);
                    try {
                        System.out.println(Lock.LOCKFILE.getPath());
                        pacman.getServer().stop();
                        System.out.println("Sending stop command to server...");
                        pacman.getServer().awaitShutdown();
                        System.out.println("Copying server jar");
                        Files.copy(server.toPath(), pacman.serverJar().toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Starting server...");
                        pacman.getServer().start();
                        System.out.println("exiting. 0");
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
            }
        } else {

        }
    }
}
