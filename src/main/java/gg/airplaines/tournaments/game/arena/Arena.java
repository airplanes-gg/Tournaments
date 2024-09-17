/*
 * This file is part of Tournament, licensed under the MIT License.
 *
 *  Copyright (c) airplanes.gg
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package gg.airplaines.tournaments.game.arena;

import gg.airplaines.tournaments.TournamentsPlugin;
import gg.airplaines.tournaments.game.arena.file.ArenaFile;
import gg.airplaines.tournaments.game.kit.Kit;
import gg.airplaines.tournaments.utils.FileUtils;
import gg.airplaines.tournaments.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents an area in which a game is played.
 */
public class Arena {
    private final String id;
    private final String builders;
    private final Collection<Kit> kits = new ArrayList<>();
    private final String name;
    private final ArenaFile arenaFile;
    private final int voidLevel;
    private final Location spectatorSpawn;
    private final File configFile;
    private final List<Location> spawns = new ArrayList<>();
    private final Location tournamentSpawn;

    // Raw Location Data
    private final List<String> spawnsRaw;
    private final String spectatorSpawnRaw;
    private final String tournamentSpawnRaw;

    /**
     * Creates the arena.
     * @param plugin Instance of the plugin.
     * @param configFile Configuration file for the arena.
     */
    public Arena(final TournamentsPlugin plugin, final File configFile) {
        this.configFile = configFile;
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        id = FileUtils.removeFileExtension(configFile.getName(), true);
        arenaFile = plugin.arenaManager().arenaFileManager().loadArenaFile(id);

        this.name = config.getString("name");


        if(config.isSet("builders")) {
            this.builders = config.getString("builders");
        }
        else {
            this.builders = "Airplanes";
        }

        if(config.isSet("voidLevel")) {
            voidLevel = config.getInt("voidLevel");
        }
        else {
            voidLevel = 0;
        }

        this.spectatorSpawnRaw = config.getString("spectatorSpawn");
        this.spectatorSpawn = LocationUtils.fromString(spectatorSpawnRaw);

        this.tournamentSpawnRaw = config.getString("tournamentSpawn");
        this.tournamentSpawn = LocationUtils.fromString(config.getString("tournamentSpawn"));

        // Load kits
        for(String kitID : config.getStringList("kits")) {
            Kit kit = plugin.kitManager().kit(kitID);

            if(kit == null) {
                continue;
            }

            kits.add(kit);
        }

        // Load the arena spawns.
        spawnsRaw = new ArrayList<>(config.getStringList("spawns"));
        for(final String spawn : spawnsRaw) {
            spawns.add(LocationUtils.fromString(spawn));
        }
    }

    /**
     * Gets the file the arena is stored in.
     * @return Arena file.
     */
    public ArenaFile arenaFile() {
        return arenaFile;
    }

    /**
     * Gets the builders of the arena.
     * @return Arena builders.
     */
    public String builders() {
        return builders;
    }

    /**
     * Get the arena's configuration file.
     * @return Arena config file.
     */
    public File configFile() {
        return configFile;
    }

    /**
     * Gets the id of the arena.
     * @return Arena id.
     */
    public String id() {
        return id;
    }

    /**
     * Get all kits the arena is made for.
     * @return Arena kits.
     */
    public Collection<Kit> kits() {
        return kits;
    }

    /**
     * Get the name of the arena.
     * @return Arena name.
     */
    public String name() {
        return name;
    }

    /**
     * Get the player spawns of the arena.
     * @param world World to get the spawns of.
     * @return List of player spawns.
     */
    public List<Location> spawns(World world) {
        List<Location> worldSpawns = new ArrayList<>();

        for(Location spawn : this.spawns) {
            worldSpawns.add(LocationUtils.replaceWorld(world, spawn));
        }

        return worldSpawns;
    }

    /**
     * Get the spectator area of the arena in a specific world.
     * @param world World to get spectator spawn of.
     * @return Spectator spawn location.
     */
    public Location spectatorSpawn(World world) {
        return LocationUtils.replaceWorld(world, spectatorSpawn);
    }

    /**
     * Get the arena's tournament spawn.
     * Returns null if it doesn't have one.
     * @param world World to get the tournament spawn of.
     * @return The arena's tournament spawn.
     */
    public Location tournamentSpawn(World world) {
        return LocationUtils.replaceWorld(world, tournamentSpawn);
    }

    /**
     * Gets the y-level in which players die.
     * @return Arena void level.
     */
    public int voidLevel() {
        return voidLevel;
    }
}