package de.seine_eloquenz.spigot_pacman_service.source;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.seine_eloquenz.spigot_pacman_service.Downloader;
import de.seine_eloquenz.spigot_pacman_service.util.JsonUtils;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Resource {

    private static final String RESOURCE_API_ENDPOINT = "https://api.spiget.org/v2/resources/";

    private final int id;
    private String name;
    private String tag;
    private int authorId;
    private int likes;
    private String type;
    private Size size;
    private String url;
    private List<String> testedVersions;
    private int releaseDate;
    private int updateDate;

    public Resource(final int id) {
        this.id = id;
        JsonObject result = Downloader.getJSONObject(RESOURCE_API_ENDPOINT + id);
        JsonObject fileInfo = result.get("file").getAsJsonObject();
        this.name = result.get("name").getAsString();
        this.tag = result.get("tag").getAsString();
        this.authorId = result.get("author").getAsJsonObject().get("id").getAsInt();
        this.likes = result.get("likes").getAsInt();
        this.type = fileInfo.get("type").getAsString();
        this.size = new Size(fileInfo.get("size").getAsInt(), fileInfo.get("sizeUnit").getAsString());
        this.url = fileInfo.get("url").getAsString();
        this.testedVersions = JsonUtils.stream(result.get("testedVersions").getAsJsonArray()).map(JsonElement::getAsString).collect(Collectors.toList());
        this.releaseDate = result.get("releaseDate").getAsInt();
        this.updateDate = result.get("updateDate").getAsInt();
    }

    public void update() {
    }

    @Override
    public String toString() {
        return id + " " + name + " " + tag + " " + authorId + " " + likes + " " + type + " " + size + " " + url + " " + testedVersions;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public int getLikes() {
        return likes;
    }

    public String getType() {
        return type;
    }

    public Size getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getTestedVersions() {
        return testedVersions;
    }

    public int getReleaseDate() {
        return releaseDate;
    }

    public int getUpdateDate() {
        return updateDate;
    }

    public int getAuthorId() {
        return authorId;
    }
}
