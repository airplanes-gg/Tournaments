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
import gg.airplaines.tournaments.utils.chat.ChatUtils;
import org.bukkit.command.CommandSender;

/**
 * This class runs the /arenas command, which displays all currently available arenas.
 */
public class ArenasCMD extends AbstractCommand {
    private final TournamentsPlugin plugin;

    /**
     * Creates the command.
     * @param plugin Instance of the plugin.
     */
    public ArenasCMD(TournamentsPlugin plugin) {
        super("arenas", "duels.admin", true);
        this.plugin = plugin;
    }

    /**
     * Executes the command.
     * @param sender The Command Sender.
     * @param args Arguments of the command.
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        ChatUtils.chat(sender, "<aqua><bold>Tournaments</bold> <dark_gray>» <aqua>Currently Loaded Arenas:");

        // Display all active arenas.
        for(Arena arena : plugin.arenaManager().getArenas()) {
            ChatUtils.chat(sender, "  <dark_gray>➤ <gray>" + arena.id()) ;
        }
    }
}