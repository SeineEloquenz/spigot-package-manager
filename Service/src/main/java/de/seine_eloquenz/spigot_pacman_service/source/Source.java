package de.seine_eloquenz.spigot_pacman_service.source;

import java.io.File;
import java.util.List;

public interface Source {

	File downloadPlugin(String name) throws Exception;

	List<String> searchForPackage(String name) throws Exception;

	String getPackageDetails(String name) throws Exception;
	
	String getName();
}
