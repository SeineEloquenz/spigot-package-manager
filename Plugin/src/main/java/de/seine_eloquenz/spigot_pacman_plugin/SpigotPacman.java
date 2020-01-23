package de.seine_eloquenz.spigot_pacman_plugin;

import de.seine_eloquenz.lbcfs.LbcfsPlugin;
import org.jgroups.JChannel;

public class SpigotPacman extends LbcfsPlugin {

    private JChannel channel;

    public SpigotPacman() throws Exception {
        this.channel = new JChannel();
        this.channel.connect("spigotPacman");
        this.channel.name("server");
        this.channel.setReceiver(new ShutdownListener());
    }

    public String getChatPrefix() {
        return "SpigotPacman";
    }

    @Override
    public void tearDown() {
        this.channel.disconnect();
    }
}
