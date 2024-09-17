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
package gg.airplaines.tournaments.game.tournament;

/**
 * Represents the size of a team, and how the team is picked.
 */
public enum TeamType {
    ONE_V_ONE("1v1", 2),
    TWO_V_TWO_RANDOM("2v2 Random", 3),
    THREE_V_THREE_RANDOM("3v3 Random", 4),
    NONE("None", 0);

    private final String displayName;
    private final int minimumPlayers;

    /**
     * Creates the team size.
     * @param displayName The name of the team size.
     * @param minimumPlayers Minimum number of players.
     */
    TeamType(String displayName, int minimumPlayers) {
        this.displayName = displayName;
        this.minimumPlayers = minimumPlayers;
    }

    /**
     * Gets the display name of the team.
     * @return Team display name.
     */
    public String displayName() {
        return displayName;
    }

    /**
     * Gets the required players to start a tournament.
     * @return Minimum number of players.
     */
    public int minimumPlayers() {
        return minimumPlayers;
    }
}