package de.seine_eloquenz.spigot_pacman_plugin;

import org.bukkit.Bukkit;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

public class ShutdownListener extends ReceiverAdapter {

    @Override
    public void receive(Message message) {
        String msg = message.getObject();
        if ("shutdown".equals(msg)) {
            Bukkit.shutdown();
        }
    }
}
