package de.seine_eloquenz.spigot_pacman_service.cmd;

import de.seine_eloquenz.spigot_pacman_service.SpigotPacman;

import java.io.IOException;

public class Restart extends Command {

    public Restart(final SpigotPacman pacman) {
        super(pacman);
    }

    @Override
    public String getName() {
        return "restart";
    }

    @Override
    protected void executeLogic(final String... args) {
        if (args.length == 0) {
            pacman().getServer().stop();
            pacman().getServer().awaitShutdown();
            try {
                pacman().getServer().start();
            } catch (IOException e) {
                System.out.println("IO Error occured while starting server, server is still stopped.");
            }
        } else {
            System.out.println("Use this command without arguments.");
        }
    }
}
