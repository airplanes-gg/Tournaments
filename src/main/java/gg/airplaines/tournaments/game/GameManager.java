package gg.airplaines.tournaments.game;

import at.stefangeyer.challonge.model.Match;
import gg.airplaines.tournaments.TournamentsPlugin;
import gg.airplaines.tournaments.game.arena.Arena;
import gg.airplaines.tournaments.game.kit.Kit;
import gg.airplaines.tournaments.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class GameManager {
    private final TournamentsPlugin plugin;
    private final Collection<Game> activeGames = new HashSet<>();

    /**
     * Creates the Game Manager.
     * @param plugin Instance of the plugin.
     */
    public GameManager(final TournamentsPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets all currently existing games.
     * @return All active games.
     */
    public Collection<Game> activeGames() {
        return activeGames;
    }

    /**
     * Manually add a game. Used in duels.
     * @param game Game to add.
     */
    public void addGame(Game game) {
        activeGames.add(game);
    }

    public CompletableFuture<Game> createGame(Arena arena, Kit kit, Match match) {
        UUID gameUUID = UUID.randomUUID();

        // Makes a copy of the arena with the generated uuid.
        CompletableFuture<File> arenaCopy = arena.arenaFile().createCopy(gameUUID.toString());

        // Creates the game.
        CompletableFuture<Game> gameCreation = CompletableFuture.supplyAsync(() -> {
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                WorldCreator worldCreator = new WorldCreator(gameUUID.toString());
                Bukkit.createWorld(worldCreator);
            });

            // Wait for the world to be generated.
            boolean loaded = false;
            World world = null;
            while(!loaded) {
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                for(World w : Bukkit.getWorlds()) {
                    if(w.getName().equals(gameUUID.toString())) {
                        loaded = true;
                        world = w;
                        break;
                    }
                }
            }

            return new Game(plugin, kit, arena, world, gameUUID, match);
        });

        return arenaCopy.thenCompose(file -> gameCreation);
    }

    /**
     * Deletes a game that is no longer needed.
     * This also deletes its temporary world folder.
     * @param game Game to delete.
     */
    public void deleteGame(Game game) {
        activeGames.remove(game);
        File worldFolder = game.world().getWorldFolder();
        Bukkit.unloadWorld(game.world(), false);

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            FileUtils.deleteDirectory(worldFolder);
        });
    }

    /**
     * Get the game a given player is currently in.
     * Null if not in a game.
     * @param player Player to get game of.
     * @return Game they are in.
     */
    public Game game(Player player) {
        // Makes a copy of the active games to prevent ConcurrentModificationException.
        List<Game> games = new ArrayList<>(activeGames);

        // Loop through each game looking for the player.
        for(Game game : games) {
            if(game.players().contains(player)) {
                return game;
            }

            if(game.spectators().contains(player)) {
                return game;
            }
        }

        return null;
    }

    /**
     * Get the game of a given world.
     * Returns null if there isn't one.
     * @param world World to get game of.
     * @return Game using that world.
     */
    public Game game(World world) {
        // Makes a copy of the active games to prevent ConcurrentModificationException.
        List<Game> games = new ArrayList<>(activeGames);

        for(Game game : games) {
            if(game.world().equals(world)) {
                return game;
            }
        }

        return null;
    }
}