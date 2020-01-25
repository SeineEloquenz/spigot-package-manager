package de.seine_eloquenz.spigot_pacman_service.config;

import de.seine_eloquenz.spigot_pacman_service.ServerType;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;

public class ApacheCommonsConfiguration implements Configuration {

    public static final String CONFIG_FILE = "." + File.separator + "spm-config.properties";

    private final FileBasedConfiguration configuration;

    public ApacheCommonsConfiguration() {
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
            throw new IllegalStateException(); //to allow for final configuration, system exit doesnt count for compiler
        }
    }

    @Override
    public ServerType serverType() {
        return ServerType.valueOf(configuration.getString("server.type"));
    }

    @Override
    public String[] serverArguments() {
        String args = configuration.getString("server.arguments");
        return "none".equals(args) ? new String[0] : args.split(" ");
    }
    @Override
    public File serverJar() {
        return new File("." + File.separator + configuration.getString("server.jar"));
    }
}
