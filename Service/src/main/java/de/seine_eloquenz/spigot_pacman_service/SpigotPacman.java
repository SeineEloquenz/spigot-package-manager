package de.seine_eloquenz.spigot_pacman_service;

import de.seine_eloquenz.spigot_pacman_service.config.ApacheCommonsConfiguration;
import de.seine_eloquenz.spigot_pacman_service.config.Configuration;
import de.seine_eloquenz.spigot_pacman_libs.Constants;
import de.seine_eloquenz.spigot_pacman_service.server.Server;
import de.seine_eloquenz.spigot_pacman_service.server.ServerImpl;
import de.seine_eloquenz.spigot_pacman_service.source.Resource;
import de.seine_eloquenz.spigot_pacman_service.source.Source;
import de.seine_eloquenz.spigot_pacman_service.source.spigot.SpigotSource;
import org.jgroups.JChannel;
import org.reflections.Reflections;
import de.seine_eloquenz.spigot_pacman_service.cmd.Command;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class SpigotPacman {

    public static final String HOME_DIR = "." + File.separator + "spm";
    public static final String PLUGIN_FOLDER_PATH = "." + File.separator + "plugins" + File.separator;
    public static final String UPDATE_FOLDER_PATH = PLUGIN_FOLDER_PATH + "update" + File.separator;

    private Map<String, Command> commands;
    private final JChannel channel;
    private final BuildToolsManager manager;
    private Configuration configuration;
    private final Server server;
    private Source source;

    public SpigotPacman() throws Exception {
        commands = new HashMap<>();
        channel = new JChannel();
        this.manager = new BuildToolsManager();
        this.configuration = new ApacheCommonsConfiguration();
        this.server = new ServerImpl(configuration.serverArguments());
        this.source = new SpigotSource();
        //noinspection ResultOfMethodCallIgnored
        (new File(UPDATE_FOLDER_PATH)).mkdirs();
        this.findAndRegisterCommands();
        channel.name("service");
        channel.connect(Constants.CHANNEL_NAME);
    }

    public Server getServer() {
        return server;
    }

    public Source getSource() {
        return source;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public boolean isInstalled(Resource resource) {
        return (new File(PLUGIN_FOLDER_PATH + "spm-" + resource.getID() + resource.getType())).exists();
    }

    public File buildServer(String version) {
        ServerType type = configuration.serverType();
        if (ServerType.paper.equals(type)) {
            // TODO paper download
            return null;
        } else {
            try {
                return manager.buildServer(type, version);
            } catch (Exception e) {
                System.err.println("Could not build server for " + type + "-" + version);
                return null;
            }
        }
    }

    public void executeCommand(String name, String... args) {
        if (this.commands.containsKey(name)) {
            this.commands.get(name).execute(args);
        } else {
            System.out.println("Unknown command. Use help for help.");
        }
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

    private void disconnect() {
        this.channel.disconnect();
    }
}
