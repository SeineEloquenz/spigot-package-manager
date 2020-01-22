package seine_eloquenz.spigot_pacman_service;

import org.reflections.Reflections;
import seine_eloquenz.spigot_pacman_service.cmd.Command;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class SpigotPacman {

    public static void main(String[] args) {
        SpigotPacman pacman = new SpigotPacman();
        if (args.length >= 1) {
            pacman.executeCommand(args[0], cutFirstParam(args));
        } else {
            System.err.println("You need to specify at least one command to run!");
        }
    }

    private Map<String, Command> commands = new HashMap<>();

    public SpigotPacman() {
        this.findAndRegisterCommands();
    }

    private void executeCommand(String name, String... args) {
        this.commands.get(name).execute(args);
    }

    private void findAndRegisterCommands() {
        final Reflections reflections = new Reflections(this.getClass().getPackageName());
        final Set<Class<? extends Command>> cmdClasses = reflections.getSubTypesOf(Command.class);
        for (final Class<?> cmd : cmdClasses) {
            try {
                final Constructor<?> constructor =
                        Stream.of(cmd.getConstructors())
                                .filter(c -> c.getParameterCount() == 1 && c.getParameterTypes()[0].equals(SpigotPacman.class))
                                .findFirst().orElse(null);
                if (constructor == null) {
                    throw new InstantiationException("Could not find Constructor of " + cmd.getName());
                }
                final Command command = (Command) constructor.newInstance();
                commands.put(command.getName(), command);
            } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace(); //Should never happen in production, as all commands need to supply this constructor
            }
        }
    }

    private static String[] cutFirstParam(final String[] params) {
        final String[] subParams = new String[params.length - 1];
        if (subParams.length >= 0) {
            System.arraycopy(params, 1, subParams, 0, subParams.length);
        }
        return subParams;
    }
}
