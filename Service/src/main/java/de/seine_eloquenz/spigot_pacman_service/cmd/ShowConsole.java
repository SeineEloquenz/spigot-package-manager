package de.seine_eloquenz.spigot_pacman_service.cmd;

import de.seine_eloquenz.spigot_pacman_service.SpigotPacman;
import org.apache.commons.io.input.CloseShieldInputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShowConsole extends Command {

    private static final Object lock = new Object();

    public ShowConsole(final SpigotPacman pacman) {
        super(pacman);
    }

    @Override
    public String getName() {
        return "show-console";
    }

    @Override
    protected void executeLogic(final String... args) {

        ConsolePrinter printer = new ConsolePrinter(pacman());
        printer.start();
        try (BufferedReader input = new BufferedReader(new InputStreamReader(new CloseShieldInputStream(System.in)))) {
            String line;
            while ((line = input.readLine()) != null) {
                switch (line) {
                    case ":q": {
                        printer.exit();
                        synchronized (lock) {
                            System.out.println("Left console.");
                        }
                        return;
                    }
                    case ":h": {
                        System.out.println("Enter your spigot commands here. Leave with :q");
                        break;
                    }
                    default: {
                        pacman().getServer().sendCommand(line);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final class ConsolePrinter extends Thread {

            private boolean stopped = false;
            private final SpigotPacman pacman;

            public ConsolePrinter(SpigotPacman pacman) {
                this.pacman = pacman;
            }

            @Override
            public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(pacman.getServer().consoleStream()))) {
                String line;
                while (!stopped && (line = reader.readLine()) != null) {
                    synchronized (lock) {
                        if (!stopped) {
                            System.out.print("\r[C]> ");
                            System.out.println(line);
                            System.out.print(":> ");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void exit() {
            stopped = true;
        }
    }
}
