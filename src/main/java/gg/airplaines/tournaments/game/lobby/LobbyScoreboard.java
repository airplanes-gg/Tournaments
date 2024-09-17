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
package gg.airplaines.tournaments.game.lobby;

import gg.airplaines.tournaments.TournamentsPlugin;
import gg.airplaines.tournaments.utils.DateUtils;
import gg.airplaines.tournaments.utils.scoreboard.CustomScoreboard;
import gg.airplaines.tournaments.utils.scoreboard.ScoreHelper;
import org.bukkit.entity.Player;

public class LobbyScoreboard extends CustomScoreboard {
    private final TournamentsPlugin plugin;

    public LobbyScoreboard(final TournamentsPlugin plugin, final Player player) {
        super(player);
        this.plugin = plugin;
    }

    public void update(final Player player) {
        final ScoreHelper helper;

        if(ScoreHelper.hasScore(player)) {
            helper = ScoreHelper.getByPlayer(player);
        }
        else {
            helper = ScoreHelper.createScore(player);
        }

        helper.setTitle("&5&lTournament");
        helper.setSlot(15, "&7" + DateUtils.currentDateToString());
        helper.setSlot(14, "");
        helper.removeSlot(13);
        helper.removeSlot(12);
        helper.removeSlot(11);
        helper.removeSlot(10);
        helper.removeSlot(9);
        helper.removeSlot(8);
        helper.removeSlot(7);
        helper.removeSlot(6);
        helper.setSlot(5, "Waiting for a host.");
        helper.setSlot(4, "");
        helper.setSlot(3, "&5Players: &7" + plugin.getServer().getOnlinePlayers().size());
        helper.setSlot(2, "");
        helper.setSlot(1, "&5play.airplanes.gg");
    }
}
