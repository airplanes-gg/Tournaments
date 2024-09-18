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
package gg.airplaines.tournaments.game.arena.builder;

import gg.airplaines.tournaments.TournamentsPlugin;
import gg.airplaines.tournaments.game.arena.Arena;
import gg.airplaines.tournaments.game.kit.Kit;
import gg.airplaines.tournaments.utils.FileUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Stores data of an arena that is still being set up.
 */
public class ArenaBuilder {
    private final TournamentsPlugin plugin;
    private String spectatorSpawn = null;
    private String name;
    private String builders;
    private String id;
    private int voidLevel = -1;
    private final Collection<Kit> kits = new HashSet<>();
    private final List<String> spawns = new ArrayList<>();
    private boolean editMode = false;
    private String tournamentSpawn = null;
    private final World world;

    /**
     * Creates the arena builder.
     * @param plugin Instance of the plugin.
     */
    public ArenaBuilder(final TournamentsPlugin plugin, World world) {
        this.plugin = plugin;
        this.world = world;
    }

    /**
     * Creates an arena builder using an existing arena.
     * Used to edit the existing arena.
     * @param plugin Instance of the plugin.
     * @param arena Arena to be edited.
     */
    public ArenaBuilder(final TournamentsPlugin plugin, Arena arena, World world) {
        this.plugin = plugin;
        this.world = world;
        this.id = arena.id();
        this.builders = arena.builders();
        this.voidLevel = arena.voidLevel();
        this.name = arena.name();

        kits.addAll(arena.kits());
        editMode = true;
    }

    /**
     * Adds a supported kit to the arena.
     * @param kit Kit to add.
     */
    public void addKit(Kit kit) {
        kits.add(kit);
    }

    /**
     * Set the builders of the arena.
     * @param builders Arena builders.
     */
    public void builders(String builders) {
        this.builders = builders;
    }

    /**
     * Get if the arena builder is in edit mode.
     * @return If in edit mode.
     */
    public boolean editMode() {
        return editMode;
    }

    /**
     * Get the id of the arena being created.
     * @return Arena id.
     */
    public String id() {
        return id;
    }

    /**
     * Set the id of the arena.
     * @param id Arena id.
     */
    public void id(String id) {
        this.id = id;
    }

    /**
     * Checks if the arena is ready to be saved.
     * @return  Whether the arena can be saved.
     */
    public boolean isSet() {
        spawns.clear();
        TreeMap<Integer, String> tempSpawnLocations = new TreeMap<>();

        // Make sure the id is set.
        if(id == null) {
            System.out.println("ID not set");
            return false;
        }

        // Make sure the name is set.
        if(name == null) {
            System.out.println("name not set");
            return false;
        }

        // Make sure modes are set.
        if(kits.size() == 0) {
            System.out.println("kits not set");
            return false;
        }

        // Loop through all spawn locations.
        System.out.println("Loaded Chunks: " + world.getLoadedChunks().length);
        for(Chunk chunk : world.getLoadedChunks()) {
            World world = chunk.getWorld();
            for(int x = 0; x < 16; x++) {
                for(int y = 0; y < (world.getMaxHeight() - 1); y++) {
                    for(int z = 0; z < 16; z++) {
                        Block block = chunk.getBlock(x, y, z);

                        if(block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
                            Sign sign = (Sign) block.getState();
                            String[] lines = sign.getLines();

                            if(lines.length < 2) {
                                System.out.println("Sign found with not enough lines!");
                                continue;
                            }

                            if(!lines[0].toLowerCase().equalsIgnoreCase("[Spawn]")) {
                                System.out.println(lines[0]);
                                continue;
                            }

                            org.bukkit.material.Sign signMaterial = (org.bukkit.material.Sign) block.getState().getData();
                            float yaw = yawFromBlockFace(signMaterial.getFacing());

                            String locationString = "world," + block.getX() + "," + block.getY() + "," + block.getZ() + "," + yaw + ",0";
                            System.out.println("Sign Found: " + locationString + ": " + lines[1]);


                            switch(lines[1].toLowerCase()) {
                                case "tournament" -> tournamentSpawn = locationString;
                                case "spectate" -> spectatorSpawn = locationString;
                                default -> tempSpawnLocations.put(Integer.parseInt(lines[1]) - 1, locationString);
                            }
                        }
                    }
                }
            }
        }

        for(int index : tempSpawnLocations.keySet()) {
            spawns.add(tempSpawnLocations.get(index));
        }

        if(spectatorSpawn == null) {
            System.out.println("No Spectator Spawn Found");
            return false;
        }

        if(spawns.size() < 2) {
            System.out.println("Not Enough Spawns! Found" + spawns.size());
            return false;
        }

        return true;
    }

    /**
     * Gets all kits the arena is set for.
     * @return All kits.
     */
    public Collection<Kit> kits() {
        return kits;
    }

    /**
     * Set the name of the arena.
     * @param name Arena's name.
     */
    public void name(String name) {
        this.name = name;
    }

    /**
     * Set the void level of the arena.
     * @param voidLevel Arena void level.
     */
    public void voidLevel(int voidLevel) {
        this.voidLevel = voidLevel;
    }

    /**
     * Saves the Arena to a configuration file.
     */
    public void save() {
        try {
            File file = new File(plugin.getDataFolder(), "/arenas/" + id + ".yml");
            if(file.exists()) {
                file.delete();
            }

            file.createNewFile();

            FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            configuration.set("name", name);
            configuration.set("builders", builders);
            configuration.set("voidLevel", voidLevel);
            configuration.set("spectatorSpawn", spectatorSpawn);
            configuration.set("tournamentSpawn", tournamentSpawn);
            configuration.set("spawns", spawns);

            // Kits
            {
                List<String> kitStrings = new ArrayList<>();
                for(Kit kit : kits) {
                    kitStrings.add(kit.id());
                }

                configuration.set("kits", kitStrings);
            }

            // Saves the file.
            configuration.save(file);

            String worldID = world.getName();
            File worldFolder = world.getWorldFolder();
            for(Player worldPlayer : world.getPlayers()) {
                plugin.getLobbyManager().sendToLobby(worldPlayer);
            }

            Bukkit.unloadWorld(world,true);

            // Saves the world where it belongs.
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                // Load applicable folders.
                File mapsFolder = new File(worldFolder.getParentFile(), "maps");
                File savedWorldFolder = new File(mapsFolder, worldID);

                // Delete the old save if in edit mode.
                if(editMode) {
                    FileUtils.deleteDirectory(savedWorldFolder);
                }

                // Copies the world to the maps folder.
                FileUtils.copyFileStructure(worldFolder, savedWorldFolder);

                // Deletes the previous world.
                FileUtils.deleteDirectory(worldFolder);

                plugin.getServer().getScheduler().runTask(plugin, () -> plugin.arenaManager().loadArena(id));
                plugin.arenaManager().arenaBuilder(null);
            });
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private float yawFromBlockFace(BlockFace blockFace) {
        switch (blockFace) {
            case NORTH -> {
                return 180f;
            }
            case NORTH_EAST -> {
                return -135f;
            }
            case EAST -> {
                return -90f;
            }
            case SOUTH_EAST -> {
                return -45f;
            }
            case SOUTH -> {
                return 0f;
            }
            case SOUTH_WEST -> {
                return 45f;
            }
            case WEST -> {
                return 90f;
            }
            case NORTH_WEST -> {
                return 135f;
            }
            default -> {
                return 1f;
            }
        }
    }
}