package de.seine_eloquenz.spigot_pacman_service;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.reflections.Reflections;
import de.seine_eloquenz.spigot_pacman_service.cmd.Command;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class SpigotPacman {

    public static void main(String[] args) throws Exception {
        SpigotPacman pacman = new SpigotPacman();

        if (args.length >= 1) {
            pacman.executeCommand(args[0], cutFirstParam(args));
        } else {
            System.err.println("You need to specify at least one command to run!");
        }
        pacman.disconnect();
    }

    private Map<String, Command> commands;
    private final JChannel channel;

    public SpigotPacman() throws Exception {
        commands = new HashMap<>();
        channel = new JChannel();
        this.findAndRegisterCommands();
        channel.connect("spigotPacman");
        channel.name("service");
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
                final Command command = (Command) constructor.newInstance(this);
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

    /**
     * Checks whether the server in this directory is running
     * @return true iff server running
     */
    public boolean serverRunning() {
        return (new File("./spigot.jar")).canWrite();
    }

    /**
     * Stops the server
     */
    public void stopServer() throws Exception {
        if (serverRunning()) {
            channel.send(new Message(null, "shutdown"));
        }
    }

    /**
     * Starts the server
     */
    public void startServer() throws IOException {
        if (!serverRunning()) {
            Runtime.getRuntime().exec("java -jar spigot.jar");
        }
    }

    private void disconnect() {
        this.channel.disconnect();
    }
}
