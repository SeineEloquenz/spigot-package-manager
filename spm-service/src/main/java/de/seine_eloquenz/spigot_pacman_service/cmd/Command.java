package de.seine_eloquenz.spigot_pacman_service.cmd;

import de.seine_eloquenz.spigot_pacman_service.SpigotPacman;

public abstract class Command {

    private final SpigotPacman pacman;

    public Command(SpigotPacman pacman) {
        this.pacman = pacman;
    }

    public abstract String getName();

    public final void execute(String... args) {
        executeLogic(args);
    }

    public final SpigotPacman pacman() {
        return pacman;
    }

    protected abstract void executeLogic(String... args);
}
