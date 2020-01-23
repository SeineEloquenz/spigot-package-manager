package de.seine_eloquenz.spigot_pacman_service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class BuildToolsManager {

	private void downloadBuildTools() throws IOException {
		String url = "https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar";
		File btDir = new File("BuildTools");
		btDir.mkdir();
		File dest = new File(btDir, "BuildTools.jar");

		System.out.print("Downloading latest BuildTools...\n[Connecting...       ] 0%");
		Downloader.download(url, dest);
	}

	private void checkConditions() throws InterruptedException {
		Process proc;
		try {
			proc = Runtime.getRuntime().exec("git --version");
		} catch (Exception e) {
			throw new IllegalStateException("Git not installed", e);
		}
		proc.waitFor();
		if (proc.exitValue() != 0) {
			throw new IllegalStateException("Git not installed");
		}
	}

	/**
	 * Cleans the given directory
	 * @param dir directory to clean
	 */
	public void clean(File dir) {
		if(dir == null)
			dir = new File("BuildTools");
		File[] oldBuilds = dir.listFiles((directory, name) -> name.endsWith(".jar") && (name.startsWith("craftbukkit") || name.startsWith("spigot")));
		if (oldBuilds != null) {
			for(File build: oldBuilds){
				System.out.println("Deleting: " + build);
				//noinspection ResultOfMethodCallIgnored
				build.delete();
			}
		}
	}
	
	private void displayProgress(double percent, String msg){
		int percent0 = (int) (percent / 5);
		System.out.print("\r[");
		for (int i = 0; i < 20; i++) {
			System.out.print(i < percent0 ? '|' : ' ');
		}
		System.out.print("] " + ((int) (percent)) + "% " + msg);
	}

	private void runBuildTools(String version) throws IOException {
		System.out.println("Running BuildTools...");
		System.out.print("[Starting...         ] 0%");
		File javaLoc = new File(new File(System.getProperty("java.home"), "bin"), "java");
		if (!javaLoc.exists()) {
			javaLoc = new File(javaLoc.getAbsolutePath() + ".exe");
			if (!javaLoc.exists()) {
				throw new IllegalStateException("Unable to find Java location");
			}
		}
		ProcessBuilder procBuilder = new ProcessBuilder(javaLoc.getAbsolutePath(), "-jar", "BuildTools.jar", "--rev",
				version);
		procBuilder.directory(new File("BuildTools"));
		procBuilder.redirectErrorStream(true);
		Process proc = procBuilder.start();
		BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String s;
		displayProgress(0, "Downloading Minecraft");
		while((s = in.readLine()) != null){
			if(s.contains("Loading mappings")){
				displayProgress(10, "Decompiling vanilla server");
			} else if(s.contains("Applying CraftBukkit Patches")){
				displayProgress(20, "Patching for CraftBukkit  ");
			} else if(s.contains("Compiling Bukkit")){
				displayProgress(30, "Compiling Bukkit          ");
			} else if(s.contains("Compiling CraftBukkit")) {
				displayProgress(40, "Compiling CraftBukkit     ");
			} else if(s.contains("Rebuilding Forked projects....")){
				displayProgress(60, "Patching for Spigot       ");
			} else if(s.contains("Compiling Spigot & Spigot-API")){
				displayProgress(70, "Compiling Spigot          ");
			} else if(s.contains("Success! Everything compiled successfully.")){
				displayProgress(99, "Finishing                 ");
			}
		}
		in.close();
		int exitVal = proc.exitValue();
		System.out.println("\r[||||||||||||||||||||] 100%");
		System.out.println("BuildTools exited with: " + exitVal);
	}
	
	private File findJar(String serverType) {
		File btDir = new File("BuildTools");
		File[] jars = btDir.listFiles((dir, name) -> name.endsWith(".jar"));
		if (jars != null) {
			for(File jar: jars){
				if(jar.getName().startsWith(serverType)){
					return jar;
				}
			}
		}
		throw new IllegalArgumentException("Can't find build for: " + serverType);
	}

	/**
	 * Builds a server
	 * @param serverType serverType to build
	 * @param version version to build
	 * @throws IOException io errors
	 * @throws InterruptedException interruption
	 */
	public void buildServer(String serverType, String version) throws IOException, InterruptedException {
		checkConditions();
		downloadBuildTools();
		clean(new File("BuildTools"));
		runBuildTools(version);
		
		File jar = findJar(serverType);
		File dest = new File(jar.getName());
		//noinspection ResultOfMethodCallIgnored
		jar.renameTo(dest);
		System.out.println(dest);
	}
}
