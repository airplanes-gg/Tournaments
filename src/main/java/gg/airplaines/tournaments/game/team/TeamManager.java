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

import gg.airplaines.tournaments.TournamentsPlugin;
import gg.airplaines.tournaments.game.tournament.team.EventTeam;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamManager {
    private final TournamentsPlugin plugin;
    private final List<Team> teams = new ArrayList<>();
    private final List<Team> aliveTeams = new ArrayList<>();
    private final List<TeamColor> availableColors = new ArrayList<>();

    /**
     * Creates the team manager.
     */
    public TeamManager(final TournamentsPlugin plugin) {
        this.plugin = plugin;

        availableColors.add(TeamColor.RED);
        availableColors.add(TeamColor.BLUE);
        availableColors.add(TeamColor.YELLOW);
        availableColors.add(TeamColor.GREEN);
        availableColors.add(TeamColor.PURPLE);
        availableColors.add(TeamColor.ORANGE);
        availableColors.add(TeamColor.AQUA);
        availableColors.add(TeamColor.PINK);
        availableColors.add(TeamColor.DARK_GREEN);
        availableColors.add(TeamColor.BLACK);
        availableColors.add(TeamColor.WHITE);
    }

    /**
     * Create a new team.
     * @param players Players to add to the team.
     * @return The new team.
     */
    public Team createTeam(List<Player> players) {
        Team team = new Team(players, availableColors.get(0));
        availableColors.remove(availableColors.get(0));
        teams().add(team);
        aliveTeams.add(team);

        team.id(teams.size() + 1);
        return team;
    }

    public Team createTeam(EventTeam eventTeam) {
        Team team = new Team(eventTeam.players(), availableColors.get(0), eventTeam);
        availableColors.remove(availableColors.get(0));
        teams().add(team);
        aliveTeams.add(team);

        team.id(teams.size() + 1);
        return team;
    }

    /**
     * Delete a team.
     * @param team Team to delete.
     */
    public void deleteTeam(Team team) {
        teams().remove(team);
        aliveTeams.remove(team);
    }

    /**
     * Get all teams that are still alive.
     * @return All alive teams.
     */
    public List<Team> aliveTeams() {
        return aliveTeams;
    }

    /**
     * Get the team of a specific player.
     * Returns null if no team.
     * @param player Player to get team of.
     * @return Team the player is in.
     */
    public Team team(Player player) {
        for(Team team : teams()) {
            if(team.players().contains(player)) {
                return team;
            }
        }

        return null;
    }

    /**
     * Get all existing teams in the manager.
     * @return All existing teams.
     */
    public List<Team> teams() {
        return teams;
    }

    /**
     * Kill a team.
     */
    public void killTeam(Team team) {
        aliveTeams.remove(team);
    }

    public void reset() {
        aliveTeams.clear();
        aliveTeams.addAll(teams);

        for(Team team : teams) {
            team.deadPlayers().clear();
            team.alivePlayers().clear();
            team.alivePlayers().addAll(team.players());
        }
    }
}