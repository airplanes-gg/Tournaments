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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventTeam {
    private final List<UUID> playerUUIDs = new ArrayList<>();
    private Long challongeID;
    private String name = "";

    public void addPlayer(Player player) {
        playerUUIDs.add(player.getUniqueId());

        StringBuilder nameBuilder = new StringBuilder(name);

        if(playerUUIDs.size() != 1) {
            nameBuilder.append(", ");
        }

        nameBuilder.append(player.getName());
        name = nameBuilder.toString();
    }

    public void challongeID(long challongeID) {
        this.challongeID = challongeID;
    }

    public Long challongeID() {
        return challongeID;
    }

    public String name() {
        return name;
    }

    public List<Player> players() {
        List<Player> teamPlayers = new ArrayList<>();

        for(UUID playerUUID : playerUUIDs) {
            Player player = Bukkit.getPlayer(playerUUID);

            if(player == null || !player.isOnline()) {
                continue;
            }

            teamPlayers.add(player);
        }

        return teamPlayers;
    }
}