package de.seine_eloquenz.spigot_pacman_service.source.spigot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import de.seine_eloquenz.spigot_pacman_service.Downloader;
import de.seine_eloquenz.spigot_pacman_service.source.PluginNotFoundException;
import de.seine_eloquenz.spigot_pacman_service.source.Resource;
import de.seine_eloquenz.spigot_pacman_service.source.Source;
import de.seine_eloquenz.spigot_pacman_service.util.JsonUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SpigotSource implements Source {

	public static final String URL_PLUGINS_SERVER = "https://www.spigotmc.org";
	public static final String URL_PLUGINS_BASE = "https://www.spigotmc.org/resources/";
	public static final String URL_PLUGINS_SEARCH = "https://www.spigotmc.org/search/186428656/?t=resource_update&o=relevance&c[rescat]=4+14+15+16+17+18+22+23+24+25+26&q=";
	public static final String URL_PLUGINS_SEARCH_NAME = "https://www.spigotmc.org/search/1/?t=resource_update&o=relevance&c[title_only]=1&c[rescat]=2+4+5+6+7+8+14+15+16+17+18+22+23+24+25+26&q=";

	@Override
	public File downloadPlugin(String name) throws IOException, PluginNotFoundException {
		String url = null; //TODO
		Document doc = Jsoup.connect(url).userAgent(USER_AGENT).get();
		String download = doc.select(".downloadButton .inner").get(0).absUrl("href");
		System.out.print("\r[Connecting...       ] 0%");
		File dest = File.createTempFile("bpm-" + name, null);
		System.out.println("[Spigot] " + download + " -> " + dest);
		Downloader.download(download, dest, USER_AGENT);
		return dest;
	}

	@Override
	public List<Resource> searchForPackage(String search) throws IOException {
		JsonArray json = Downloader.getJSONArray("https://api.spiget.org/v2/search/resources/" + search + "?size=10&fields=name");
		return JsonUtils.stream(json)
				.map(JsonElement::getAsJsonObject)
				.map(o -> new Resource(Integer.parseInt(o.get("id").getAsString())))
				.collect(Collectors.toList());
	}

	@Override
	public String getPackageDetails(String name) throws IOException, PluginNotFoundException {
		String url = null; //TODO
		System.out.println("\n[Spigot] " + "Read " + url);
		Document doc = Jsoup.connect(url).userAgent(USER_AGENT).get();
		String res = doc.select(".tagLine").get(0).text().trim() + "\n";
		Element text = doc.select(".messageText").get(0);
		res += br2nl(text.html());
		return res;
	}

	protected static String br2nl(String html) {
		if (html == null) {
			return null;
		}
		Document document = Jsoup.parse(html);
		document.outputSettings(new Document.OutputSettings().prettyPrint(false));
		document.select("br").append("\\n");
		document.select("p").prepend("\\n\\n");
		String s = document.html().replaceAll("\\\\n", "\n");
		return Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
	}

	public String getName() {
		return "Spigot";
	}
}
