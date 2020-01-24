package de.seine_eloquenz.spigot_pacman_service;

import de.seine_eloquenz.spigot_pacman_libs.Constants;
import de.seine_eloquenz.spigot_pacman_service.server.Server;
import de.seine_eloquenz.spigot_pacman_service.server.ServerImpl;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
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
    public static final String CONFIG_FILE = "." + File.separator + "spm-config.properties";

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
    private final BuildToolsManager manager;
    private FileBasedConfiguration configuration;
    private final Server server;

    public SpigotPacman() throws Exception {
        commands = new HashMap<>();
        channel = new JChannel();
        this.manager = new BuildToolsManager();
        File configFile = new File(CONFIG_FILE);
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder
                = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                .configure(params.fileBased().setFile(configFile));
        try {
            this.configuration = builder.getConfiguration();
        } catch (ConfigurationException e) {
            System.err.println("Configuration not found!");
            System.exit(1);
        }
        this.server = new ServerImpl();
        this.findAndRegisterCommands();
        channel.name("service");
        channel.connect(Constants.CHANNEL_NAME);
    }

    public Server getServer() {
        return server;
    }

    public String serverType() {
        return configuration.getString("server.type");
    }

    public File serverJar() {
        return new File("." + File.separator + configuration.getString("server.jar"));
    }

    public File buildServer(String version) {
        String type = serverType();
        if (ServerType.paper.name().equals(type)) {
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

    private void disconnect() {
        this.channel.disconnect();
    }
}
