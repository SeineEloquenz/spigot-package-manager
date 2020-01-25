package de.seine_eloquenz.spigot_pacman_service.source;

import de.seine_eloquenz.spigot_pacman_service.Downloader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SourceBukkitDev implements Source {
	public static final String URL_PLUGINS_SERVER = "http://dev.bukkit.org";
	public static final String URL_PLUGINS_BASE = "http://dev.bukkit.org/bukkit-plugins/";
	public static final String URL_PLUGINS_SEARCH = "http://dev.bukkit.org/search/?scope=projects&search=";

	public File downloadPlugin(String name) throws IOException {
		System.out.print("\r[Connecting...       ] 0%");
		File dest = File.createTempFile("bpm-" + name, null);
		String download = findDownloadLink(URL_PLUGINS_BASE + name);
		System.out.println("[Bukkit] " + download + " -> " + dest);
		Downloader.download(download, dest);
		return dest;
	}

	protected String findDownloadLink(String url) throws IOException {
		System.out.println("[Bukkit] " + "Read " + url);
		Document doc = Jsoup.connect(url).get();
		Elements downloadLink = doc.select(".user-action-download a");
		url = downloadLink.get(0).absUrl("href");
		doc = Jsoup.connect(url).get();
		System.out.println("[Bukkit] " + "Read " + url);
		downloadLink = doc.select(".user-action-download a");
		return downloadLink.get(0).absUrl("href");
	}

	public List<String> searchForPackage(String search) throws Exception {
		List<String> results = new LinkedList<>();
		String url = URL_PLUGINS_SEARCH + URLEncoder.encode(search, "UTF-8");
		System.out.println("\n[Bukkit] " + "Read " + url);
		Document doc = Jsoup.connect(url).get();
		Elements resultElements = doc.select(".col-search-entry:not(.single-col)");
		for (Element el : resultElements) {
			Element link = el.select("a").get(0);
			String name = link.absUrl("href");
			name = name.substring(URL_PLUGINS_BASE.length());
			name = name.substring(0, name.length() - 1);
			results.add(name);
		}
		Collections.sort(results);
		return results;
	}

	public String getPackageDetails(String name) throws Exception {
		String url = URL_PLUGINS_BASE + name;
		System.out.println("\n[Bukkit] " + "Read " + url);
		Document doc = Jsoup.connect(url).get();
		Element contents = doc.select(".content-box-inner").get(0);
		return contents.text();
	}
	
	public String getName(){
		return "Bukkit";
	}
}
