package de.seine_eloquenz.spigot_pacman_service.util;

import org.apache.commons.io.input.CloseShieldInputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Terminal {

    public static boolean askYesNo(String question) {
        System.out.println(question + " [y,n]");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new CloseShieldInputStream(System.in)))) {
            System.out.print(":> ");
            String line;
            while ((line = in.readLine()) != null) {
                switch (line.toLowerCase()) {
                    case "y": {
                        return true;
                    }
                    case "n": {
                        return false;
                    }
                    default: {
                        System.out.println("Enter y or n");
                    }
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void wait(String message, Predicate predicate) {
        StringBuilder dots = new StringBuilder();
        while (predicate.check()) {
            if (dots.length() > 3) {
                dots = new StringBuilder();
            }
            System.out.print("\r" + message + dots.toString());
            dots.append(".");
            try {
                Thread.sleep(250);
            } catch (InterruptedException ignored) {
            }
        }
        System.out.println();
    }
}
