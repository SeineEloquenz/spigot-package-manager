package de.seine_eloquenz.spigot_pacman_service;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class BuildToolsManagerTest {

    @Test
    public void runBuildTools() throws IOException, InterruptedException {
        BuildToolsManager manager = new BuildToolsManager();
        manager.buildServer("spigot", "1.14.4");
    }

    @Test
    public void isLocked() {
        System.out.println(Lock.LOCKFILE.getAbsolutePath());
        System.out.println(Lock.locked());
    }
}
