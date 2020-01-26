package de.seine_eloquenz.spigot_pacman_service.source;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface Source {

	String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36";

	File downloadPlugin(Resource resource) throws IOException, PluginNotFoundException;

	List<Resource> searchForPackage(String name) throws PluginNotFoundException, IOException;

	String getPackageDetails(String name) throws IOException, PluginNotFoundException;
	
	String getName();
}
