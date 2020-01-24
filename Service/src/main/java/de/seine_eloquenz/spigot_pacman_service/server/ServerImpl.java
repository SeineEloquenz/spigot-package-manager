package de.seine_eloquenz.spigot_pacman_service.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ServerImpl implements Server {

    private final ProcessBuilder processBuilder;
    private Process server;
    private String commandLineParams;

    public ServerImpl() {
        this("");
    }

    public ServerImpl(String commandLineParams) {
        processBuilder = new ProcessBuilder();
        processBuilder.command("java -jar spigot.jar " + commandLineParams);
    }

    @Override
    public boolean isRunning() {
        return server != null && server.isAlive();
    }

    @Override
    public void sendCommand(final String command) {
        if (isRunning()) {
            PrintWriter writer = new PrintWriter(new BufferedOutputStream(server.getOutputStream()));
            writer.println(command);
            writer.close();
        }
    }

    @Override
    public void stop() {
        if (isRunning()) {
            this.sendCommand("stop now");
        }
        this.awaitShutdown();
        server = null;
    }

    @Override
    public void start() throws IOException {
        if (!isRunning()) {
            server = processBuilder.start();
        }
    }

    @Override
    public void awaitShutdown() {
        File spigotJar = new File("./spigot.jar");
        while (!spigotJar.canWrite()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
