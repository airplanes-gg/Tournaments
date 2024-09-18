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

import at.stefangeyer.challonge.Challonge;
import at.stefangeyer.challonge.exception.DataAccessException;
import at.stefangeyer.challonge.model.Credentials;
import at.stefangeyer.challonge.model.Match;
import at.stefangeyer.challonge.model.Participant;
import at.stefangeyer.challonge.model.Tournament;
import at.stefangeyer.challonge.model.enumeration.MatchState;
import at.stefangeyer.challonge.model.enumeration.TournamentType;
import at.stefangeyer.challonge.model.query.MatchQuery;
import at.stefangeyer.challonge.model.query.ParticipantQuery;
import at.stefangeyer.challonge.model.query.TournamentQuery;
import at.stefangeyer.challonge.rest.RestClient;
import at.stefangeyer.challonge.rest.retrofit.RetrofitRestClient;
import at.stefangeyer.challonge.serializer.Serializer;
import at.stefangeyer.challonge.serializer.gson.GsonSerializer;
import gg.airplaines.tournaments.TournamentsPlugin;
import gg.airplaines.tournaments.game.Game;
import gg.airplaines.tournaments.game.arena.Arena;
import gg.airplaines.tournaments.game.team.Team;
import gg.airplaines.tournaments.game.tournament.team.EventTeam;
import gg.airplaines.tournaments.game.tournament.team.EventTeamManager;
import gg.airplaines.tournaments.utils.MapUtils;
import gg.airplaines.tournaments.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class DuelEvent {
    private final TournamentsPlugin plugin;
    private final EventTeamManager eventTeamManager = new EventTeamManager();
    private final Challonge challonge;
    private Tournament tournament;
    private int taskID;

    public DuelEvent(final TournamentsPlugin plugin) {
        this.plugin = plugin;

        // Connects to Challonge
        Credentials credentials = new Credentials(plugin.settingsManager().getConfig().getString("challonge.username"), plugin.settingsManager().getConfig().getString("challonge.api-key"));
        Serializer serializer = new GsonSerializer();
        RestClient restClient = new RetrofitRestClient();
        challonge = new Challonge(credentials, serializer, restClient);

        // Starts the tournament.
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            createTournament();
            startEvent();
        });
    }

    public void addResults(Match match, Team winner, Team loser) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, ()-> {
            MatchQuery.MatchQueryBuilder builder;

            if(winner.eventTeam().challongeID().equals(match.getPlayer1Id())) {
                builder = MatchQuery.builder()
                        .winnerId(winner.eventTeam().challongeID())
                        .scoresCsv(winner.score() + "-" + loser.score());
            }
            else {
                builder = MatchQuery.builder()
                        .winnerId(winner.eventTeam().challongeID())
                        .scoresCsv(loser.score() + "-" + winner.score());
            }

            boolean sent = false;
            while(!sent) {
                try {
                    challonge.updateMatch(match, builder.build());
                    sent = true;
                    Thread.sleep(1000);
                }
                catch (DataAccessException | InterruptedException exception) {
                    exception.printStackTrace();
                }

            }
        });
    }

    public void broadcast(String message) {
        for(Player player : participants()) {
            ChatUtils.chat(player, message);
        }
    }

    private void createTournament() {
        try {
            // Create the tournament.
            {
                TournamentQuery.TournamentQueryBuilder builder = TournamentQuery.builder();
                builder.name("airplanes.gg - " + plugin.duelEventManager().host().getName() + "'s " + plugin.duelEventManager().kit().name() + " Tournament")
                        .gameName("Minecraft")
                        .description(plugin.duelEventManager().kit().name() + " tournament on airplanes.gg. Join us at play.airplanes.gg")
                        .holdThirdPlaceMatch(true);

                // Sets the tournament type of the tournament.
                switch (plugin.duelEventManager().eventType()) {
                    case SINGLE_ELIMINATION -> builder.tournamentType(TournamentType.SINGLE_ELIMINATION);
                    case DOUBLE_ELIMINATION -> builder.tournamentType(TournamentType.DOUBLE_ELIMINATION);
                }
                tournament = challonge.createTournament(builder.build());
            }

            // Add players to the tournament.
            {
                List<ParticipantQuery> queries = new ArrayList<>();

                // Create teams.
                switch (plugin.duelEventManager().teamSize()) {
                    case ONE_V_ONE -> {
                        for(Player player : plugin.getServer().getOnlinePlayers()) {

                            // If set, stops the host from participating.
                            if(player.equals(plugin.duelEventManager().host()) && !plugin.duelEventManager().hostPlaying()) {
                                continue;
                            }

                            eventTeamManager.createTeam(player);
                        }
                    }

                    case TWO_V_TWO_RANDOM -> {
                        List<Player> waitingPlayers = new ArrayList<>(plugin.getServer().getOnlinePlayers());
                        Collections.shuffle(waitingPlayers);

                        // If set, stops the host from participating.
                        if(!plugin.duelEventManager().hostPlaying()) {
                            waitingPlayers.remove(plugin.duelEventManager().host());
                        }

                        while (waitingPlayers.size() != 0) {

                            if (waitingPlayers.size() >= 2) {
                                Player one = waitingPlayers.get(0);
                                Player two = waitingPlayers.get(1);

                                waitingPlayers.remove(one);
                                waitingPlayers.remove(two);

                                List<Player> team = new ArrayList<>();
                                team.add(one);
                                team.add(two);
                                eventTeamManager.createTeam(team);
                            }
                            else {
                                Player one = waitingPlayers.get(0);

                                waitingPlayers.remove(one);
                                eventTeamManager.createTeam(one);
                            }
                        }
                    }

                    case THREE_V_THREE_RANDOM -> {
                        List<Player> waitingPlayers = new ArrayList<>(plugin.getServer().getOnlinePlayers());
                        Collections.shuffle(waitingPlayers);

                        // If set, stops the host from participating.
                        if(!plugin.duelEventManager().hostPlaying()) {
                            waitingPlayers.remove(plugin.duelEventManager().host());
                        }

                        while (waitingPlayers.size() != 0) {

                            if (waitingPlayers.size() >= 3) {
                                Player one = waitingPlayers.get(0);
                                Player two = waitingPlayers.get(1);
                                Player three = waitingPlayers.get(2);

                                waitingPlayers.remove(one);
                                waitingPlayers.remove(two);
                                waitingPlayers.remove(three);

                                List<Player> team = new ArrayList<>();
                                team.add(one);
                                team.add(two);
                                team.add(three);
                                eventTeamManager.createTeam(team);
                            }
                            else if(waitingPlayers.size() == 2) {
                                Player one = waitingPlayers.get(0);
                                Player two = waitingPlayers.get(1);

                                waitingPlayers.remove(one);
                                waitingPlayers.remove(two);

                                List<Player> team = new ArrayList<>();
                                team.add(one);
                                team.add(two);
                                eventTeamManager.createTeam(team);
                            }
                            else {
                                Player one = waitingPlayers.get(0);

                                waitingPlayers.remove(one);
                                eventTeamManager.createTeam(one);
                            }
                        }
                    }
                }

                // Create challonge participant queries.
                eventTeamManager.teams().forEach(team -> queries.add(ParticipantQuery.builder().name(team.name()).build()));

                // Update challonge participant ids.
                List<Participant> participants = challonge.bulkAddParticipants(tournament, queries);
                for(Participant participant : participants) {
                    eventTeamManager.team(participant.getName()).challongeID(participant.getId());
                }
            }
        }
        catch (DataAccessException exception) {
            ChatUtils.chat(plugin.duelEventManager().host(), "&c&lError &8» &cSomething went wrong while creating the tournament! Check console for details.");
            plugin.duelEventManager().reset();
        }
    }

    public Collection<Player> participants() {
        List<Player> players = new ArrayList<>();

        for(EventTeam team : eventTeamManager.teams()) {
            players.addAll(team.players());
        }

        return players;
    }

    private void startEvent() {
        // Set the status to RUNNING to update the scoreboard.
        plugin.duelEventManager().eventStatus(EventStatus.RUNNING);
        // TODO: Scoreboard, in the main thread.

        // Attempts to start the tournament through challonge.
        boolean started = false;
        while(!started) {
            try {
                challonge.startTournament(tournament);
                started = true;
            }
            catch (DataAccessException exception) {
                exception.printStackTrace();
                try {
                    Thread.sleep(100);
                }
                catch (InterruptedException exception2) {
                    exception2.printStackTrace();
                }
            }
        }

        plugin.getServer().getScheduler().runTask(plugin, () -> {
            // Broadcast start message.
            World world = Bukkit.getWorld("world");
            ChatUtils.broadcast(world, "&8&m+-----------------------***-----------------------+");
            ChatUtils.broadcast(world, ChatUtils.centerText("&b&l" + plugin.duelEventManager().host().getName() + "'s Tournament"));
            ChatUtils.broadcast(world, "");
            ChatUtils.broadcast(world, ChatUtils.centerText("&bKit: &f" + plugin.duelEventManager().kit().name()));
            ChatUtils.broadcast(world, ChatUtils.centerText("&bTeams: &f" + plugin.duelEventManager().teamSize().displayName() + " &7(" + plugin.duelEventManager().bestOf().toString() + "&7)"));
            ChatUtils.broadcast(world, "");
            ChatUtils.broadcast(world, ChatUtils.centerText("<aqua>Bracket: <white><click:open_url:'https://www.challonge.com/" + tournament.getUrl() + "'>https://challonge.com/" + tournament.getUrl() + "</click>"));
            ChatUtils.broadcast(world, "");
            ChatUtils.broadcast(world, "&8&m+-----------------------***-----------------------+");

            // TODO: Give spectator items.
        });

        // Repeatedly loops through the matches, starting any that are waiting to be started.
        taskID = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            boolean matchesObtained = false;
            int games = 0;
            while(!matchesObtained) {
                try {
                    // Gets all matches that aren't complete.
                    List<Match> matches = new ArrayList<>();
                    for(Match match : challonge.getMatches(tournament)) {
                        if (match.getState() == MatchState.COMPLETE) {
                            continue;
                        }
                        matches.add(match);
                    }

                    // Ends the event if there are no matches left.
                    if(matches.size() == 0) {
                        stopEvent();
                        return;
                    }

                    // Loops through all the matches waiting to be started.
                    for(Match match : matches) {
                        // Makes sure the match hasn't already been started.
                        if(match.getUnderwayAt() != null) {
                            continue;
                        }

                        // Makes sure match has 2 waiting players.
                        if(match.getPlayer1Id() == null || match.getPlayer2Id() == null) {
                            continue;
                        }

                        EventTeam team1 = eventTeamManager.team(match.getPlayer1Id());
                        EventTeam team2 = eventTeamManager.team(match.getPlayer2Id());

                        // Check that both teams are online.
                        if(team1.players().size() == 0) {
                            match.setForfeited(true);
                            match.setWinnerId(team2.challongeID());

                            MatchQuery query = MatchQuery.builder()
                                    .winnerId(team2.challongeID())
                                    .scoresCsv("0-" + plugin.duelEventManager().bestOf().neededWins())
                                    .build();
                            challonge.updateMatch(match, query);
                            continue;
                        }
                        else if(team2.players().size() == 0) {
                            match.setForfeited(true);
                            match.setWinnerId(team1.challongeID());

                            MatchQuery query = MatchQuery.builder()
                                    .winnerId(team1.challongeID())
                                    .scoresCsv(plugin.duelEventManager().bestOf().neededWins() + "-0")
                                    .build();
                            challonge.updateMatch(match, query);
                            continue;
                        }

                        if(games >= 14) {
                            continue;
                        }

                        // Tell challonge that the match is underway.
                        boolean sent = false;
                        while (!sent) {
                            try {
                                challonge.markMatchAsUnderway(match);
                                sent = true;
                                games++;
                                Thread.sleep(1000);
                            }
                            catch (DataAccessException | InterruptedException exception) {
                                exception.printStackTrace();
                            }
                        }

                        // Physically start the match, delayed by 2 ticks (100 ms)
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            List<Player> players = new ArrayList<>();
                            players.addAll(team1.players());
                            players.addAll(team2.players());

                            for(Player player : players) {
                                Game game = plugin.gameManager().game(player);

                                if(game == null) {
                                    continue;
                                }

                                if(game.spectators().contains(player)) {
                                    game.removePlayer(player);
                                }
                            }

                            List<Arena> arenas = new ArrayList<>(plugin.arenaManager().getArenas(plugin.duelEventManager().kit()));
                            Collections.shuffle(arenas);

                            plugin.gameManager().createGame(arenas.get(0), plugin.duelEventManager().kit(), match).thenAccept(game -> {
                                plugin.getServer().getScheduler().runTask(plugin, () -> {
                                    game.addPlayers(team1);
                                    game.addPlayers(team2);
                                    game.startGame();
                                });
                            });
                        }, 2);
                    }

                    // Mark that the matches have been obtained.
                    matchesObtained = true;
                }
                catch (DataAccessException exception) {
                    exception.printStackTrace();

                    // Wait before trying again.
                    try {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }, 0, 200).getTaskId();
    }

    public void stopEvent() {
        // Cancels the repeating task.
        Bukkit.getScheduler().cancelTask(taskID);

        // Finalizes the tournament
        boolean finished = false;
        while(!finished) {
            try {
                challonge.finalizeTournament(tournament);
                finished = true;
            }
            catch (DataAccessException exception) {
                exception.printStackTrace();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        // Loads all participants in the tournament.
        Map<Participant, Integer> results = new HashMap<>();
        List<Participant> participants = new ArrayList<>();
        boolean sent = false;
        while(!sent) {
            try {
                participants.addAll(challonge.getParticipants(tournament));
                sent = true;
            }
            catch (DataAccessException exception) {
                exception.printStackTrace();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        // Stores all participant's final rank.
        for(Participant participant : participants) {
            results.put(participant, participant.getFinalRank());
        }

        // Sorts the results to get final rankings.
        Map<Participant, Integer> rankings = MapUtils.sortByValue(results);
        List<Participant> top = new ArrayList<>(rankings.keySet());

        // Display the end message to all players in the tournament.
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            broadcast("&8&m+-----------------------***-----------------------+");
            broadcast(ChatUtils.centerText("&b&lTournament"));
            broadcast(ChatUtils.centerText("&bKit: &f" + plugin.duelEventManager().kit().name()));
            broadcast("");
            broadcast(ChatUtils.centerText("&6&l1st: &f" + top.get(0).getName()));
            broadcast(ChatUtils.centerText("&f&l2nd: &f" + top.get(1).getName()));

            if(top.size() >= 3) {
                broadcast(ChatUtils.centerText("&c&l3rd: &f" + top.get(2).getName()));
            }
            else {
                broadcast(ChatUtils.centerText("&c&l3rd: &fNone"));
            }
            broadcast("");
            broadcast(ChatUtils.centerText("<aqua>Bracket: <white><click:open_url:'https://www.challonge.com/" + tournament.getUrl() + "'>https://challonge.com/" + tournament.getUrl() + "</click>"));
            broadcast("&8&m+-----------------------***-----------------------+");
        });

        // Resets the event manager and teleports everyone back to the spawn.
        plugin.getServer().getScheduler().runTaskLater(plugin, ()-> {
            plugin.duelEventManager().reset();

            for(Player player : Bukkit.getOnlinePlayers()) {
                // TODO: Teleport to tournament spawn. player.teleport(LocationUtils.getSpawn(plugin));
                // TODO: Give regular items again. ItemUtils.giveLobbyItems(player);
            }
        }, 200);
    }

    public Tournament tournament() {
        return tournament;
    }
}