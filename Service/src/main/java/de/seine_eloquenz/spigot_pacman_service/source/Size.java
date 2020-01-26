package de.seine_eloquenz.spigot_pacman_service.source;

/**
 * Class representing resource size
 */
public class Size {
    private int size;
    private String unit;

    public Size(final int size, final String unit) {
        this.size = size;
        this.unit = unit;
    }

    @Override
    public String toString() {
        return size + unit;
    }
}
