package de.seine_eloquenz.spigot_pacman_service.server;

import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.commons.io.output.CloseShieldOutputStream;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        return server != null && server.isAlive();
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
        }
    }

    @Override
    public void awaitShutdown() {
        while (isRunning()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
    }

    @Override
    public InputStream consoleStream() {
        return new CloseShieldInputStream(server.getInputStream());
    }
}
