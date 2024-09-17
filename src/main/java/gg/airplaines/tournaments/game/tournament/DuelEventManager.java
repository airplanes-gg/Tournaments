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

import gg.airplaines.tournaments.TournamentsPlugin;
import gg.airplaines.tournaments.game.Game;
import gg.airplaines.tournaments.game.kit.Kit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages the current event and event settings.
 */
public class DuelEventManager {
    private final TournamentsPlugin plugin;
    private Player host;
    private EliminationType eventType;
    private DuelEvent activeEvent;
    private Kit kit;
    private EventStatus eventStatus;
    private BestOf bestOf;
    private boolean hostPlaying = true;
    private TeamType teamSize = TeamType.ONE_V_ONE;

    /**
     * Creates the Duel Event Manager.
     * @param plugin Plugin instance.
     */
    public DuelEventManager(final TournamentsPlugin plugin) {
        this.plugin = plugin;

        // Reset the event data
        reset();
    }

    /**
     * Get the active event.
     * @return active event.
     */
    public DuelEvent activeEvent() {
        return activeEvent;
    }

    /**
     * Change the active event.
     * @param activeEvent new active event.
     */
    public void activeEvent(DuelEvent activeEvent) {
        this.activeEvent = activeEvent;
    }

    /**
     * Get the current best of.
     * @return Current best of.
     */
    public BestOf bestOf() {
        return bestOf;
    }

    /**
     * Change the best of.
     * @param bestOf new best of.
     */
    public void bestOf(BestOf bestOf) {
        this.bestOf = bestOf;
    }

    /**
     * Creates a new event using the existing settings.
     */
    public void create() {
        activeEvent = new DuelEvent(plugin);
    }

    /**
     * Get the current event status.
     * @return current event status.
     */
    public EventStatus eventStatus() {
        return eventStatus;
    }

    /**
     * Change the event status.
     * @param eventStatus new event status.
     */
    public void eventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    /**
     * Get the current event type.
     * @return current event type.
     */
    public EliminationType eventType() {
        return eventType;
    }

    /**
     * Change the event type.
     * @param eventType new event type.
     */
    public void eventType(EliminationType eventType) {
        this.eventType = eventType;
    }

    /**
     * Get the current host.
     * Is null if there is none.
     * @return current host.
     */
    public Player host() {
        return host;
    }

    /**
     * Change the host.
     * @param host new host.
     */
    public void host(Player host) {
        this.host = host;
    }

    public boolean hostPlaying() {
        return hostPlaying;
    }

    public void hostPlaying(boolean hostPlaying) {
        this.hostPlaying = hostPlaying;
    }

    /**
     * Get the current kit selected.
     * Is null if not set.
     * @return current kit selected.
     */
    public Kit kit() {
        return kit;
    }

    /**
     * Change the current kit.
     * @param kit new kit.
     */
    public void kit(Kit kit) {
        this.kit = kit;
    }

    /**
     * Get all tournament players.
     * Both in the tournament world, and in tournament games.
     * @return All tournament players.
     */
    public Collection<Player> players() {
        Set<Player> players = new HashSet<>(plugin.getServer().getOnlinePlayers());

        for(Game game : plugin.gameManager().activeGames()) {
            players.addAll(game.players());
            players.addAll(game.spectators());
        }

        return players;
    }

    /**
     * Resets an event. Used if an event is canceled or ended.
     */
    public void reset() {
        activeEvent = null;
        eventType = EliminationType.NONE;
        eventStatus = EventStatus.NONE;
        host = null;
        kit = null;
    }

    public TeamType teamSize() {
        return teamSize;
    }

    public void teamSize(TeamType teamSize) {
        this.teamSize = teamSize;
    }
}