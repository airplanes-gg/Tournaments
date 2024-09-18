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
import gg.airplaines.tournaments.utils.chat.ChatUtils;
import org.bukkit.command.CommandSender;

public class BracketCMD extends AbstractCommand {
    private final TournamentsPlugin plugin;

    public BracketCMD(TournamentsPlugin plugin) {
        super("bracket", "", true);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(plugin.duelEventManager().activeEvent() == null) {
            ChatUtils.chat(sender, "&c&lError &8» &cThere is no tournament currently active!");
            return;
        }

        String url = "https://challonge.com/" + plugin.duelEventManager().activeEvent().tournament().getUrl();
        ChatUtils.chat(sender, "&b&lTournament&8» &bBracket: &f<click:open_url:'" + url + "'>" + url + "</click>");
    }
}