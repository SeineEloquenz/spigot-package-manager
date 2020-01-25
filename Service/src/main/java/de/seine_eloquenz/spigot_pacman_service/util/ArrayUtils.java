package de.seine_eloquenz.spigot_pacman_service.util;

public final class ArrayUtils {

    private ArrayUtils() {

    }

    public static String[] cutFirstParam(final String[] params) {
        final String[] subParams;
        if (params.length - 1 <= 0) {
            subParams = new String[0];
        } else {
            subParams = new String[params.length - 1];
            System.arraycopy(params, 1, subParams, 0, subParams.length);
        }
        return subParams;
    }
}
