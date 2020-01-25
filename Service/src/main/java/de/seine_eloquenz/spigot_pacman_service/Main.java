package de.seine_eloquenz.spigot_pacman_service;

import de.seine_eloquenz.spigot_pacman_service.util.ArrayUtils;
import org.apache.commons.io.input.CloseShieldInputStream;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws Exception {
        SpigotPacman pacman = new SpigotPacman();
        pacman.getServer().start();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new CloseShieldInputStream(System.in)))) {
            System.out.print(":> ");
            String line;
            while ((line = in.readLine()) != null) {
                String[] input = line.split(" ");
                pacman.executeCommand(input[0], ArrayUtils.cutFirstParam(input));
                System.out.print(":> ");
            }
        }
    }
}
