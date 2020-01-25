package de.seine_eloquenz.spigot_pacman_plugin;

import de.seine_eloquenz.lbcfs.LbcfsPlugin;
import de.seine_eloquenz.spigot_pacman_libs.Constants;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.jgroups.JChannel;

@Plugin(name = "SPM", version = "1.0")
@Author("Seine_Eloquenz")
@Dependency("LBCFS")
public class SpigotPacman extends LbcfsPlugin {

    private final JChannel channel;

    public SpigotPacman() throws Exception {
        this.channel = new JChannel();
        this.channel.name("server");
        this.channel.connect(Constants.CHANNEL_NAME);
    }

    public String getChatPrefix() {
        return "SPM";
    }

    @Override
    public void tearDown() {
        this.channel.disconnect();
    }
}
