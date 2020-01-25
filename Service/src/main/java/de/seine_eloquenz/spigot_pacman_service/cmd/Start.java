package de.seine_eloquenz.spigot_pacman_service.cmd;

import de.seine_eloquenz.spigot_pacman_service.SpigotPacman;

import java.io.IOException;

public class Start extends Command {

    private String[] args;

    public Start(final SpigotPacman pacman) {
        super(pacman);
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    protected void executeLogic(final String... args) {
        this.args = args;
        try {
            pacman().getServer().start();
        } catch (IOException e) {
            System.out.println("IOException. Could not start server.");
        }
    }
}
