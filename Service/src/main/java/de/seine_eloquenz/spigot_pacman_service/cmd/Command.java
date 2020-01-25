package de.seine_eloquenz.spigot_pacman_service.cmd;

import de.seine_eloquenz.spigot_pacman_service.SpigotPacman;

public abstract class Command {

    final SpigotPacman pacman;

    public Command(SpigotPacman pacman) {
        this.pacman = pacman;
    }

    public abstract String getName();

    public void execute(String... args) {
        executeLogic(args);
    }

    abstract void executeLogic(String... args);
}
