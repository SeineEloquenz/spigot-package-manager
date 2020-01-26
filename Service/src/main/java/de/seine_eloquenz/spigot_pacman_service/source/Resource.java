package de.seine_eloquenz.spigot_pacman_service.source;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.seine_eloquenz.spigot_pacman_service.Downloader;
import de.seine_eloquenz.spigot_pacman_service.util.JsonUtils;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static de.seine_eloquenz.spigot_pacman_service.SpigotPacman.PLUGIN_FOLDER_PATH;
import static de.seine_eloquenz.spigot_pacman_service.source.spigot.SpigotSource.RESOURCE_API_ENDPOINT;

public class Resource {

    private final int id;
    private String name;
    private String tag;
    private int authorId;
    private int likes;
    private String type;
    private Size size;
    private List<String> testedVersions;
    private int releaseDate;
    private int updateDate;

    public Resource(final int id) {
        this.id = id;
    }

    public void update() {
        JsonObject result = Downloader.getJSONObject(RESOURCE_API_ENDPOINT + id);
        JsonObject fileInfo = result.get("file").getAsJsonObject();
        this.name = result.get("name").getAsString();
        this.tag = result.get("tag").getAsString();
        this.authorId = result.get("author").getAsJsonObject().get("id").getAsInt();
        this.likes = result.get("likes").getAsInt();
        this.type = fileInfo.get("type").getAsString();
        this.size = new Size(fileInfo.get("size").getAsInt(), fileInfo.get("sizeUnit").getAsString());
        this.testedVersions = JsonUtils.stream(result.get("testedVersions").getAsJsonArray()).map(JsonElement::getAsString).collect(Collectors.toList());
        this.releaseDate = result.get("releaseDate").getAsInt();
        this.updateDate = result.get("updateDate").getAsInt();
    }

    public boolean isInstalled() {
        return (new File(PLUGIN_FOLDER_PATH + "spm-" + this.getID() + this.getType())).exists();
    }

    @Override
    public String toString() {
        return getID() + " " + getName() + " ";
    }

    public int getID() {
        return id;
    }

    public String getName() {
        if (name == null) {
            this.update();
        }
        return name;
    }

    public String getTag() {
        if (tag == null) {
            this.update();
        }
        return tag;
    }

    public int getLikes() {
        return likes;
    }

    public String getType() {
        if (type == null) {
            this.update();
        }
        return type;
    }

    public Size getSize() {
        if (size == null) {
            this.update();
        }
        return size;
    }

    public List<String> getTestedVersions() {
        if (testedVersions == null) {
            this.update();
        }
        return testedVersions;
    }

    public int getReleaseDate() {
        if (releaseDate == 0) {
            this.update();
        }
        return releaseDate;
    }

    public int getUpdateDate() {
        if (updateDate == 0) {
            this.update();
        }
        return updateDate;
    }

    public int getAuthorId() {
        if (authorId == 0) {
            this.update();
        }
        return authorId;
    }
}
