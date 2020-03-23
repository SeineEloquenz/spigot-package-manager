package de.seine_eloquenz.spigot_pacman_service.server;

import de.seine_eloquenz.spigot_pacman_libs.Constants;
import de.seine_eloquenz.spigot_pacman_service.SpigotPacman;
import de.seine_eloquenz.spigot_pacman_service.util.Terminal;
import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.commons.io.output.CloseShieldOutputStream;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerImpl implements Server {

    private final ProcessBuilder processBuilder;
    private Process server;
    private String[] commandLineParams;

    public ServerImpl() {
        this("");
    }

    public ServerImpl(String... commandLineParams) {
        this.commandLineParams = commandLineParams;
        processBuilder = new ProcessBuilder();
        processBuilder.command(Stream.concat(Stream.of("java", "-jar", "spigot.jar"), Arrays.stream(commandLineParams))
                        .collect(Collectors.toList()));
    }

    @Override
    public boolean isRunning() {
        if (server == null) {
            return false;
        }
        try {
            server.exitValue();
        } catch (IllegalThreadStateException e) {
            return !failsafeFound();
        }
        return true;
    }

    @Override
    public void sendCommand(final String command) {
        if (isRunning()) {
            try (PrintWriter writer = new PrintWriter(new BufferedOutputStream(new CloseShieldOutputStream(server.getOutputStream())))){
                writer.println(command);
            }
        }
    }

    @Override
    public void stop() {
        if (isRunning()) {
            this.sendCommand("stop now");
        } else {
            System.out.println("Server is already stopped.");
            return;
        }
        this.awaitShutdown();
        server = null;
        System.out.println("Server stopped.");
    }

    @Override
    public void start() throws IOException {
        if (!isRunning()) {
            server = processBuilder.start();
            System.out.println("Server started.");
        } else {
            System.out.println("Server is already running.");
        }
    }

    @Override
    public void awaitShutdown() {
        Terminal.wait("Waiting for Server to shutdown", this::isRunning);
    }

    @Override
    public InputStream consoleStream() {
        return new CloseShieldInputStream(server.getInputStream());
    }

    private boolean failsafeFound() {
        File logFile = new File(SpigotPacman.SERVER_LOG_PATH + "latest.log");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile)));
            return reader.lines().anyMatch(s -> s.contains(Constants.SHUTDOWN_MARK));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
