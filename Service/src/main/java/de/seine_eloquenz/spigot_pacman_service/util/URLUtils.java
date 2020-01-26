package de.seine_eloquenz.spigot_pacman_service.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class URLUtils {

    private URLUtils() {

    }

    public static String encodeUTF8(String string) {
        try {
            return URLEncoder.encode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); //This should never happen
            return "";
        }
    }
}
