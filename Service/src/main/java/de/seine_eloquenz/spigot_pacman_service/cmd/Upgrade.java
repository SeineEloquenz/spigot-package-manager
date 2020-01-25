package de.seine_eloquenz.spigot_pacman_service.cmd;

import de.seine_eloquenz.spigot_pacman_service.SpigotPacman;

public class Upgrade extends Command {

    public Upgrade(final SpigotPacman pacman) {
        super(pacman);
    }

    @Override
    public String getName() {
        return "upgrade";
    }

    @Override
    protected void executeLogic(final String... args) {
        if (args.length > 0) {
            switch (args[0]) {
                case "-a": {

                    break;
                }
                case "-p": {
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }
}
