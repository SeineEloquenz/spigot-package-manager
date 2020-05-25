package de.seine_eloquenz.spigot_pacman_service.server_supplier;

import de.seine_eloquenz.spigot_pacman_service.ServerType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class BuildToolsSupplierTest {

    @Disabled("For manual use only")
    @Test
    public void runBuildTools() throws IOException, InterruptedException {
        ServerSupplier manager = new BuildToolsSupplier();
        manager.buildServer("1.14.4");
    }

    @Test
    public void downloadPaper() throws IOException, InterruptedException {
        ServerSupplier manager = new PaperSupplier();
        manager.buildServer("1.14.4");
    }
}
