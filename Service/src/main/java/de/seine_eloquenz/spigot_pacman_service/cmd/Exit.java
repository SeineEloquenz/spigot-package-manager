package de.seine_eloquenz.spigot_pacman_service.cmd;

import de.seine_eloquenz.spigot_pacman_service.SpigotPacman;

public class Exit extends Command {

    public Exit(final SpigotPacman pacman) {
        super(pacman);
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    void executeLogic(final String... args) {
        pacman.getServer().stop();
        System.exit(0);
    }
}