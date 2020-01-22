package seine_eloquenz.spigot_pacman_service.cmd;

public interface Command {

    String getName();

    void execute(String... args);
}
