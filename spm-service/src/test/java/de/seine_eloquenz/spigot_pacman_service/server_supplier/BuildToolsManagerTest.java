package de.seine_eloquenz.spigot_pacman_service.server_supplier;

import de.seine_eloquenz.spigot_pacman_service.ServerType;
import de.seine_eloquenz.spigot_pacman_service.server_supplier.BuildToolsManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class BuildToolsManagerTest {

    @Disabled("For manual use only")
    @Test
    public void runBuildTools() throws IOException, InterruptedException {
        BuildToolsManager manager = new BuildToolsManager();
        manager.buildServer(ServerType.spigot, "1.14.4");
    }
}
