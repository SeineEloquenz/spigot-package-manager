package de.seine_eloquenz.spigot_pacman_service.cmd;

import de.seine_eloquenz.spigot_pacman_service.SpigotPacman;

public class Status extends Command {

    public Status(final SpigotPacman pacman) {
        super(pacman);
    }

    @Override
    public String getName() {
        return "status";
    }

    @Override
    void executeLogic(final String... args) {
        System.out.println(pacman.getServer().isRunning() ? "Server online" : "Server offline");
    }
}
