package de.seine_eloquenz.spigot_pacman_service.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JsonUtils {

    private JsonUtils() {

    }

    public static Stream<JsonElement> stream(JsonArray array) {
        return StreamSupport.stream(array.spliterator(), false);
    }
}
