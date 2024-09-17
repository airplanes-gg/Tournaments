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
package gg.airplaines.tournaments.game.tournament.team;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EventTeamManager {
    private final List<EventTeam> teams = new ArrayList<>();

    public EventTeam createTeam(Player player) {
        EventTeam team = new EventTeam();
        team.addPlayer(player);
        teams.add(team);
        return team;
    }

    public EventTeam createTeam(List<Player> players) {
        EventTeam team = new EventTeam();
        players.forEach(team::addPlayer);
        teams.add(team);
        return team;
    }

    public EventTeam team(Long challongeID) {
        for(EventTeam team : teams) {
            if(team.challongeID().equals(challongeID)) {
                return team;
            }
        }

        return null;
    }

    public EventTeam team(Player player) {
        for(EventTeam team : teams) {
            if(team.players().contains(player)) {
                return team;
            }
        }

        return null;
    }

    public EventTeam team(String name) {
        for(EventTeam team : teams) {
            if(team.name().equals(name)) {
                return team;
            }
        }

        return null;
    }

    public Collection<EventTeam> teams() {
        return teams;
    }
}