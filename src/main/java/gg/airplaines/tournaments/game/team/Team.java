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
package gg.airplaines.tournaments.game.team;

import gg.airplaines.tournaments.game.tournament.team.EventTeam;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a group of players
 * working together in a Game.
 */
public class Team {
    private final List<Player> players;
    private final Set<Player> alivePlayers;
    private final Set<Player> deadPlayers = new HashSet<>();
    private final TeamColor teamColor;
    private int score = 0;
    private final EventTeam eventTeam;
    private int id = 0;

    /**
     * Creates a new team with specific players.
     * @param players Players to add to the team.
     */
    public Team(List<Player> players, TeamColor teamColor) {
        this.players = players;
        this.alivePlayers = new HashSet<>(players);
        this.teamColor = teamColor;
        this.eventTeam = null;
    }

    public Team(List<Player> players, TeamColor teamColor, EventTeam eventTeam) {
        this.players = players;
        this.alivePlayers = new HashSet<>(players);
        this.teamColor = teamColor;
        this.eventTeam = eventTeam;
    }

    public void addPoint() {
        score++;
    }

    /**
     * Get all alive players on the team.
     * @return All alive players.
     */
    public Set<Player> alivePlayers() {
        return alivePlayers;
    }

    /**
     * Gets all dead players on the team.
     * @return All dead players.
     */
    public Set<Player> deadPlayers() {
        return deadPlayers;
    }

    public EventTeam eventTeam() {
        return eventTeam;
    }

    /**
     * Gets the color of the team.
     * @return TeamColor the team is assigned.
     */
    public TeamColor teamColor() {
        return teamColor;
    }

    /**
     * Gets all players on the team, alive and dead.
     * @return All players on the team.
     */
    public List<Player> players() {
        return players;
    }

    /**
     * Add a player to the dead list,
     * and remove them from the alive list.
     * @param player Player to make dead.
     */
    public void killPlayer(Player player) {
        alivePlayers.remove(player);
        deadPlayers.add(player);
    }

    /**
     * Remove a player from the team.
     * @param player Player to remove.
     */
    public void removePlayer(Player player) {
        players().remove(player);
        alivePlayers().remove(player);
        deadPlayers().remove(player);
    }

    public int score() {
        return score;
    }

    public int id() {
        return id;
    }

    public void id(int id) {
        this.id = id;
    }
}