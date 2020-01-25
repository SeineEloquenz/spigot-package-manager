package de.seine_eloquenz.spigot_pacman_service.util;

public class WaitUtils {

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
