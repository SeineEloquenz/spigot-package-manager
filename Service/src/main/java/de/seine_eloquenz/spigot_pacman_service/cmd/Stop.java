package de.seine_eloquenz.spigot_pacman_service.cmd;

import de.seine_eloquenz.spigot_pacman_service.SpigotPacman;

public class Stop extends Command {

    public Stop(final SpigotPacman pacman) {
        super(pacman);
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    void executeLogic(final String... args) {
        pacman().getServer().sendCommand("stop now");
    }
}
