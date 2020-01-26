package de.seine_eloquenz.spigot_pacman_service.source.spigot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import de.seine_eloquenz.spigot_pacman_service.Downloader;
import de.seine_eloquenz.spigot_pacman_service.SpigotPacman;
import de.seine_eloquenz.spigot_pacman_service.source.PluginNotFoundException;
import de.seine_eloquenz.spigot_pacman_service.source.Resource;
import de.seine_eloquenz.spigot_pacman_service.source.Source;
import de.seine_eloquenz.spigot_pacman_service.util.JsonUtils;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class SpigotSource implements Source {

	public static final String RESOURCE_API_ENDPOINT = "https://api.spiget.org/v2/resources/";

	@Override
	public File downloadPlugin(Resource resource) throws IOException, PluginNotFoundException {
		String url = RESOURCE_API_ENDPOINT + resource.getID() + "/download";
		System.out.println("Downloading Plugin.");
		File dest = new File(SpigotPacman.UPDATE_FOLDER_PATH + "spm-" + resource.getID() + resource.getType());
		FileUtils.copyURLToFile(new URL(url), dest);
		System.out.println("Completed! " + resource.getName() + " -> " + dest);
		return dest;
	}

	@Override
	public List<Resource> searchForPackage(String search) throws IOException {
		JsonArray json = Downloader.getJSONArray("https://api.spiget.org/v2/search/resources/" + search + "?size=10&fields=name%2Cfile");
		return JsonUtils.stream(json)
				.map(JsonElement::getAsJsonObject)
				.filter(o -> o.getAsJsonObject("file").get("type").getAsString().equalsIgnoreCase(".jar"))
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
