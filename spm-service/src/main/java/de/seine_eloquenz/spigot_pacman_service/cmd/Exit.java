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
    protected void executeLogic(final String... args) {
        this.pacman().getServer().stop();
        System.exit(0);
    }
}
