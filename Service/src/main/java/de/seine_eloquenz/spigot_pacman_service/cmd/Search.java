package de.seine_eloquenz.spigot_pacman_service.cmd;

import de.seine_eloquenz.spigot_pacman_service.SpigotPacman;
import de.seine_eloquenz.spigot_pacman_service.source.PluginNotFoundException;
import de.seine_eloquenz.spigot_pacman_service.source.Resource;

import java.io.IOException;
import java.util.List;

public class Search extends Command {

    public Search(final SpigotPacman pacman) {
        super(pacman);
    }

    @Override
    public String getName() {
        return "search";
    }

    @Override
    protected void executeLogic(final String... args) {
        if (args.length == 1) {
            try {
                List<Resource> results = pacman().getSource().searchForPackage(args[0]);
                System.out.println("Results for search query : " + args[0]);
                results.forEach(r -> System.out.println("[ID] " + r.getID() + " [Name] " + r.getName() + " [Desc] " + r.getTag()));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (PluginNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
