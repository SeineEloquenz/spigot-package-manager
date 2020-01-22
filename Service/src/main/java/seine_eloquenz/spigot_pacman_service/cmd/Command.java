package seine_eloquenz.spigot_pacman_service.cmd;

import seine_eloquenz.spigot_pacman_service.SpigotPacman;

import java.io.File;

public abstract class Command {

    private final SpigotPacman pacman;

    public Command(SpigotPacman pacman) {
        this.pacman = pacman;
    }

    public abstract String getName();

    public void execute(String... args) {
        executeLogic(args);
    }

    abstract void executeLogic(String... args);

    protected void awaitSpigotShutdown() {
        File spigotJar = new File("./spigot.jar");
        while (!spigotJar.canWrite()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
