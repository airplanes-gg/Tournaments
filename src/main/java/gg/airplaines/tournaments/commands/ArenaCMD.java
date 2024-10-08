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
package gg.airplaines.tournaments.commands;

import gg.airplaines.tournaments.TournamentsPlugin;
import gg.airplaines.tournaments.game.arena.Arena;
import gg.airplaines.tournaments.game.arena.builder.ArenaBuilder;
import gg.airplaines.tournaments.game.kit.Kit;
import gg.airplaines.tournaments.utils.chat.ChatUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Manages the /arena command, which is used for setting up new arenas.
 */
public class ArenaCMD extends AbstractCommand {
    private final TournamentsPlugin plugin;

    /**
     * Creates the command.
     * @param plugin Instance of the plugin.
     */
    public ArenaCMD(final TournamentsPlugin plugin) {
        super("arena", "duels.admin", false);
        this.plugin = plugin;
    }


    /**
     * Executes the command.
     * @param sender The Command Sender.
     * @param args Arguments of the command.
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        // Makes sure an argument is given.
        if(args.length == 0) {
            return;
        }

        switch (args[0].toLowerCase()) {
            case "create" -> createCMD(player, args);
            case "setname" -> setNameCMD(player, args);
            case "addkit" -> addKit(player, args);
            case "setvoidlevel" -> setVoidLevel(player, args);
            case "finish" -> finishCMD(player);
            case "edit" -> editCMD(player, args);
            case "listkits" -> listKitsCMD(player);
            case "removekit" -> removeKit(player, args);
        }
    }

    /**
     * Runs the /arena create command.
     * This command starts the arena creation process.
     * @param player Player running the command.
     * @param args Command arguments.
     */
    private void createCMD(Player player, String[] args) {
        if(plugin.arenaManager().arenaBuilder() != null) {
            ChatUtils.chat(player, "&cError &8» &cThere is already an arena being set up.");
            return;
        }

        // Makes sure the command is being used properly.
        if(args.length == 1) {
            ChatUtils.chat(player, "&cUsage &8» &c/arena create [id]");
            return;
        }

        // Gets the arena id.
        String id = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");

        // Creates the arena world.
        WorldCreator worldCreator = new WorldCreator(id).type(WorldType.FLAT);
        World world = Bukkit.createWorld(worldCreator);

        // Sets world settings.
        world.setGameRuleValue("keepInventory", "true");
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setStorm(false);
        world.setWeatherDuration(Integer.MAX_VALUE);
        world.setTime(6000);
        world.getWorldBorder().setCenter(world.getSpawnLocation());
        world.getWorldBorder().setSize(210);
        world.setKeepSpawnInMemory(true);

        player.setGameMode(GameMode.CREATIVE);
        player.teleport(world.getSpawnLocation());
        player.setFlying(true);

        // Starts the arena setup process.
        plugin.arenaManager().arenaBuilder(new ArenaBuilder(plugin, world));
        plugin.arenaManager().arenaBuilder().id(id);

        ChatUtils.chat(player, "&b&lTournament &8» &bCreated an arena with the id &f" + id + "&b.");
        ChatUtils.chat(player, "&b&lTournament &8» &bNext, set the arena name with &f/arena setname [name]&b.");
    }

    /**
     * Runs the /arena setname command.
     * This command sets the name of the arena.
     * @param player Player running the command.
     * @param args Command arguments.
     */
    private void setNameCMD(Player player, String[] args) {
        // Makes sure there an arena is being set up.
        if(plugin.arenaManager().arenaBuilder() == null) {
            ChatUtils.chat(player, "&cError &8» &cYou need to create an arena first! /arena create");
            return;
        }

        // Makes sure the command is being used properly.
        if(args.length == 1) {
            ChatUtils.chat(player, "&cUsage &8» &c/arena setname [name]");
            return;
        }

        // Gets the arena name.
        String name = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");

        // Sets the arena name.
        plugin.arenaManager().arenaBuilder().name(name);
        ChatUtils.chat(player, "&b&lTournament &8» &bArena name set to &f" + name + "&b.");
        ChatUtils.chat(player, "&b&lTournament &8» &bNext, add all allowable modes with &f/arena addkit [kit]&b.");
    }

    /**
     * Runs the /arena addkit command.
     * This command adds to the list of kits that the arena is allowed to be used in.
     * @param player Player running the command.
     * @param args Command arguments.
     */
    private void addKit(Player player, String[] args) {
        // Makes sure there an arena is being set up.
        if(plugin.arenaManager().arenaBuilder() == null) {
            ChatUtils.chat(player, "&cError &8» &cYou need to create an arena first! /arena create");
            return;
        }

        // Makes sure the command is being used properly.
        if(args.length == 1) {
            ChatUtils.chat(player, "&cUsage &8» &c/arena addkit [kit]");
            return;
        }

        final Kit kit = plugin.kitManager().kit(args[1]);

        if(kit == null) {
            ChatUtils.chat(player, "&cNot a valid kit!");
            return;
        }

        plugin.arenaManager().arenaBuilder().addKit(plugin.kitManager().kit(args[1]));

        ChatUtils.chat(player, "&b&lTournament &8» &bAdded &f" + args[1] + "&b as a valid kit.");
        ChatUtils.chat(player, "&b&lTournament &8» &bWhen you are done, finish the arena with &f/arena finish&b.");
    }

    /**
     * Runs the /arena setvoidlevel command.
     * This command sets the y level in which players should die and respawn.
     * @param player Player running the command.
     * @param args Command arguments.
     */
    private void setVoidLevel(Player player, String[] args) {
        // Makes sure there an arena is being set up.
        if(plugin.arenaManager().arenaBuilder() == null) {
            ChatUtils.chat(player, "&cError &8» &cYou need to create an arena first! /arena create");
            return;
        }

        // Makes sure the command is being used properly.
        if(args.length == 1) {
            ChatUtils.chat(player, "&cUsage &8» &c/arena setvoidlevel [y-level]");
            return;
        }

        // Gets the team size from the command.
        int voidLevel = Integer.parseInt(args[1]);

        // Sets the team size.
        plugin.arenaManager().arenaBuilder().voidLevel(voidLevel);
        ChatUtils.chat(player, "&b&lTournament &8» &bVoid level has been set to &f" + voidLevel + "&b.");
    }

    /**
     * Runs the /arena finish command.
     * This command checks if the arena is done and saves it if so.
     * @param player Player running the command.
     */
    private void finishCMD(Player player) {
        // Makes sure there an arena is being set up.
        if(plugin.arenaManager().arenaBuilder() == null) {
            ChatUtils.chat(player, "&cError &8» &cYou need to create an arena first! /arena create");
            return;
        }

        // Warn the player if setup is not complete.
        if(!plugin.arenaManager().arenaBuilder().isSet()) {
            ChatUtils.chat(player, "&cError &8» &cSetup not complete!");
            return;
        }

        ChatUtils.chat(player, "&b&lTournament &8» &aArena has been saved.");

        // Saves the arena.
        plugin.arenaManager().arenaBuilder().save();
        plugin.arenaManager().arenaBuilder(null);
    }

    /**
     * Runs the /arena edit command.
     * This command edits an existing arena.
     * @param player Player running the command.
     * @param args Command arguments.
     */
    private void editCMD(Player player, String[] args) {
        if(plugin.arenaManager().arenaBuilder() != null) {
            ChatUtils.chat(player, "&cError &8» &cThere is already an arena being set up.");
            return;
        }

        // Makes sure the command is being used properly.
        if(args.length == 1) {
            ChatUtils.chat(player, "&cUsage &8» &c/arena edit [id]");
            return;
        }

        // Gets the arena id.
        String id = args[1];
        System.out.println(id);

        // Makes sure the arena exists.
        if(plugin.arenaManager().getArena(id) == null) {
            ChatUtils.chat(player, "&cError &8» &cThat arena does not exist!");
            return;
        }

        Arena arena = plugin.arenaManager().getArena(id);
        arena.arenaFile().createCopy(arena.id()).thenAccept(file -> {
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                World world = Bukkit.createWorld(new WorldCreator(arena.id()));
                player.setGameMode(GameMode.CREATIVE);
                player.teleport(world.getSpawnLocation());
                player.setFlying(true);

                plugin.arenaManager().arenaBuilder(new ArenaBuilder(plugin, arena, world));

                ChatUtils.chat(player, "&b&lTournament &8» &bYou are now editing &f" + arena.name() + "&b.");
                ChatUtils.chat(player, "&b&lTournament &8» &bWhen you are done, finish the arena with &f/arena finish&b.");
            });
        });
    }

    /**
     * Runs the /arena listkits command.
     * This command lists all kits that currently use the edited arena.
     * @param player Player running the command.
     */
    private void listKitsCMD(Player player) {
        // Makes sure there is an arena is being set up.
        if(plugin.arenaManager().arenaBuilder() == null) {
            ChatUtils.chat(player, "&cError &8» &cYou need to create an arena first! /arena create");
            return;
        }

        // Print out all kits in chat.
        ChatUtils.chat(player, "&b&lTournament &8» &bHere's all kits that use this map:");
        for(Kit kit : plugin.arenaManager().arenaBuilder().kits()) {
            ChatUtils.chat(player, "  <dark_gray>➤ <gray>" + kit.name());
        }
    }

    /**
     * Runs the /arena removekit command.
     * This command removes a kit from the list of kits that can use the arena.
     * @param player Player running the command.
     * @param args Command arguments.
     */
    private void removeKit(Player player, String[] args) {
        // Makes sure there an arena is being set up.
        if(plugin.arenaManager().arenaBuilder() == null) {
            ChatUtils.chat(player, "&cError &8» &cYou need to create an arena first! /arena create");
            return;
        }

        // Makes sure the command is being used properly.
        if(args.length == 1) {
            ChatUtils.chat(player, "&cUsage &8» &c/arena removekit [kit]");
            return;
        }

        if(!plugin.arenaManager().arenaBuilder().kits().contains(args[0])) {
            ChatUtils.chat(player, "&c&lError &8» &cThis arena does not use that kit!");
            return;
        }

        plugin.arenaManager().arenaBuilder().kits().remove(args[0]);

        ChatUtils.chat(player, "&b&lTournament &8» &aRemoved &f" + args[1] + "&b as a valid kit.");
    }
}