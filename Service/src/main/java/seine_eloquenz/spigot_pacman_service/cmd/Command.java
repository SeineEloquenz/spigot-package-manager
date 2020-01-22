package seine_eloquenz.spigot_pacman_service.cmd;

import java.io.File;

public abstract class Command {

    public abstract String getName();

    void execute(String... args) {
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
