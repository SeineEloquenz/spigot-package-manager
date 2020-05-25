package de.seine_eloquenz.spigot_pacman_service.server_supplier;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class PaperSupplier implements ServerSupplier {

    private static final String LATEST_API_ENDPOINT = "https://papermc.io/api/v1/paper/%s/latest/download";

    @Override
    public File buildServer(final String version) throws IOException, InterruptedException {
        final String downloadUrl = String.format(LATEST_API_ENDPOINT, version);
        final File dest = new File(ServerSupplier.BUILD_TARGET_DIR + File.separator + "paper.jar");
        FileUtils.copyURLToFile(new URL(downloadUrl), dest);
        return dest;
    }
}
