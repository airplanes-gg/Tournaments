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
import gg.airplaines.tournaments.game.tournament.EventStatus;
import gg.airplaines.tournaments.utils.chat.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCMD extends AbstractCommand {
    private final TournamentsPlugin plugin;

    public StartCMD(TournamentsPlugin plugin) {
        super("start", "tournament.use", false);

        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        // Make sure no other tournaments are currently running.
        if(plugin.duelEventManager().activeEvent() != null) {
            ChatUtils.chat(sender, "&c&lError &8» &cThere is already a tournament active!");
            return;
        }

        // Make sure a tournament exists before starting one.
        if(plugin.duelEventManager().eventStatus() != EventStatus.WAITING) {
            ChatUtils.chat(sender, "&c&lError &8» &cYou have to create a tournament first!!");
            return;
        }

        int playingCount = plugin.getServer().getOnlinePlayers().size();
        if(!plugin.duelEventManager().hostPlaying()) {
            playingCount--;
        }

        // Make sure there are enough players to start a tournament.
        if(playingCount < plugin.duelEventManager().teamSize().minimumPlayers()) {
            ChatUtils.chat(sender, "&cError &8» &cThere are not enough players to start!");
            return;
        }

        Player player = (Player) sender;

        // Make sure the host is the one running the command.
        if(!player.equals(plugin.duelEventManager().host())) {
            ChatUtils.chat(sender, "&cError &8» &cOnly the host can run that command!!");
            return;
        }

        // Update bungeecord
        // TODO: Update bungeecord.

        ChatUtils.broadcast("&b&lTournament &8» &bGenerating Brackets");
        plugin.duelEventManager().create();
    }
}